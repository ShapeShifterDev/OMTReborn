package omtreborn.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import omtreborn.blocks.BlockTurretBase;
import omtreborn.config.OMTConfig;
import omtreborn.gui.TurretBaseMenu;
import omtreborn.init.ModBlockEntities;
import omtreborn.lib.tileentity.OMLTrustedMachineBlockEntity;
import omtreborn.lib.util.TargetingSettings;
import omtreborn.lib.util.WorldUtil;
import omtreborn.network.OMTNetwork;
import omtreborn.network.messages.MessageTurretBase;
import omtreborn.tileentity.turrets.TurretHeadBlockEntity;
import omtreborn.util.OMTUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TurretBaseBlockEntity extends OMLTrustedMachineBlockEntity implements MenuProvider {

    private static final int INVENTORY_SIZE = 13;

    private TargetingSettings targetingSettings;
    private boolean active = true;
    private boolean multiTargeting = false;
    private int kills = 0;
    private int playerKills = 0;

    // Tier 1 fuel system
    private final ItemStackHandler fuelInventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) { setChanged(); }
    };
    private int burnTime = 0;
    private int maxBurnTime = 0;

    private final ContainerData containerData = new ContainerData() {
        @Override public int get(int index) { return index == 0 ? burnTime : index == 1 ? maxBurnTime : 0; }
        @Override public void set(int index, int value) { if (index == 0) burnTime = value; else if (index == 1) maxBurnTime = value; }
        @Override public int getCount() { return 2; }
    };

    // Wraps the energy storage but blocks external RF input; used as the Tier 1 capability
    private final LazyOptional<IEnergyStorage> noReceiveEnergyOpt = LazyOptional.of(() -> new IEnergyStorage() {
        @Override public int receiveEnergy(int max, boolean simulate) { return 0; }
        @Override public boolean canReceive() { return false; }
        @Override public int extractEnergy(int max, boolean simulate) { return energyStorage.extractEnergy(max, simulate); }
        @Override public boolean canExtract() { return energyStorage.canExtract(); }
        @Override public int getEnergyStored() { return energyStorage.getEnergyStored(); }
        @Override public int getMaxEnergyStored() { return energyStorage.getMaxEnergyStored(); }
    });

    private final Map<Direction, ExpanderBlockEntity> expanderMap = new EnumMap<>(Direction.class);

    public TurretBaseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET_BASE.get(), pos, state,
                capacityForState(state), ioForState(state), ioForState(state));
        this.targetingSettings = new TargetingSettings(false, true, false, 0, 0);
        for (Direction d : Direction.values()) expanderMap.put(d, null);
    }

    private static int capacityForState(BlockState state) {
        int tier = state.hasProperty(BlockTurretBase.TIER) ? state.getValue(BlockTurretBase.TIER) : 1;
        return switch (tier) {
            case 2 -> OMTConfig.BASES.baseTierTwo.baseMaxCharge.get();
            case 3 -> OMTConfig.BASES.baseTierThree.baseMaxCharge.get();
            case 4 -> OMTConfig.BASES.baseTierFour.baseMaxCharge.get();
            case 5 -> OMTConfig.BASES.baseTierFive.baseMaxCharge.get();
            default -> OMTConfig.BASES.baseTierOne.baseMaxCharge.get();
        };
    }

    private static int ioForState(BlockState state) {
        int tier = state.hasProperty(BlockTurretBase.TIER) ? state.getValue(BlockTurretBase.TIER) : 1;
        return switch (tier) {
            case 2 -> OMTConfig.BASES.baseTierTwo.baseMaxIo.get();
            case 3 -> OMTConfig.BASES.baseTierThree.baseMaxIo.get();
            case 4 -> OMTConfig.BASES.baseTierFour.baseMaxIo.get();
            case 5 -> OMTConfig.BASES.baseTierFive.baseMaxIo.get();
            default -> OMTConfig.BASES.baseTierOne.baseMaxIo.get();
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY && getTier() == 1) {
            return noReceiveEnergyOpt.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        noReceiveEnergyOpt.invalidate();
    }

    @Override
    protected ItemStackHandler createInventory() {
        return new ItemStackHandler(INVENTORY_SIZE) {
            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (slot < 9 && !OMTUtil.isItemStackValidAmmo(stack)) return stack;
                return super.insertItem(slot, stack, simulate);
            }
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    public int getTier() {
        BlockState state = getBlockState();
        return state.hasProperty(BlockTurretBase.TIER) ? state.getValue(BlockTurretBase.TIER) : 1;
    }

    public ItemStackHandler getFuelInventory() { return fuelInventory; }
    public ContainerData getContainerData() { return containerData; }
    public int getBurnTime() { return burnTime; }
    public int getMaxBurnTime() { return maxBurnTime; }
    public boolean isBurning() { return burnTime > 0; }

    public void updateExpanders() {
        if (level != null) {
            for (Direction dir : Direction.values()) {
                var be = level.getBlockEntity(worldPosition.relative(dir));
                expanderMap.put(dir, be instanceof ExpanderBlockEntity exp ? exp : null);
            }
        }
    }

    public void updateMaxRange() {
        int newMaxRange = 0;
        for (var be : WorldUtil.getTouchingBlockEntities(level, worldPosition)) {
            if (be instanceof TurretHeadBlockEntity head) {
                int r = head.getTurretBaseRange() + omtreborn.turret.TurretHeadUtil.getRangeUpgrades(this, head);
                newMaxRange = Math.max(newMaxRange, r);
            }
        }
        int oldMaxRange = targetingSettings.getMaxRange();
        int oldRange = targetingSettings.getRange();
        targetingSettings.setMaxRange(newMaxRange);
        if (targetingSettings.getRange() == 0 && newMaxRange > 0) {
            targetingSettings.setRange(newMaxRange);
        }
        if (newMaxRange != oldMaxRange || targetingSettings.getRange() != oldRange) {
            informUpdate();
        }
    }

    public List<ItemStackHandler> getAmmoInventories() {
        List<ItemStackHandler> result = new ArrayList<>();
        result.add(inventory);
        for (ExpanderBlockEntity exp : expanderMap.values()) {
            if (exp != null && !exp.isPowerExpander()) {
                result.add(exp.getInventory());
            }
        }
        return result;
    }

    public List<Integer> getAddonSlots() {
        int tier = getTier();
        List<Integer> slots = new ArrayList<>();
        if (tier >= 2) {
            slots.add(9);
            slots.add(10);
        }
        return slots;
    }

    public List<Integer> getUpgradeSlots() {
        int tier = getTier();
        List<Integer> slots = new ArrayList<>();
        if (tier >= 2) slots.add(11);
        if (tier >= 5) slots.add(12);
        return slots;
    }

    public TargetingSettings getTargetingSettings() {
        return targetingSettings;
    }

    public void setTargetingSettings(TargetingSettings settings) {
        this.targetingSettings = settings;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isMultiTargeting() {
        return multiTargeting;
    }

    public void setMultiTargeting(boolean multiTargeting) {
        this.multiTargeting = multiTargeting;
    }

    public int getRange() {
        return targetingSettings.getRange();
    }

    public void setRange(int range) {
        targetingSettings.setRange(range);
    }

    public int getMaxRange() {
        return targetingSettings.getMaxRange();
    }

    public boolean isAttacksPlayers() {
        return targetingSettings.isTargetPlayers();
    }

    public void setAttacksPlayers(boolean v) {
        targetingSettings.setTargetPlayers(v);
    }

    public boolean isAttacksMobs() {
        return targetingSettings.isTargetMobs();
    }

    public void setAttacksMobs(boolean v) {
        targetingSettings.setTargetMobs(v);
    }

    public boolean isAttacksNeutrals() {
        return targetingSettings.isTargetPassive();
    }

    public void setAttacksNeutrals(boolean v) {
        targetingSettings.setTargetPassive(v);
    }

    public int getKills() { return kills; }
    public void setKills(int v) { kills = v; }
    public void increaseKillCounter() { kills++; }

    public int getPlayerKills() { return playerKills; }
    public void setPlayerKills(int v) { playerKills = v; }
    public void increasePlayerKillCounter() { playerKills++; }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.omtreborn.turret_base");
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
        if (player instanceof ServerPlayer sp) {
            OMTNetwork.sendToPlayer(sp, new MessageTurretBase(this));
        }
        return new TurretBaseMenu(windowId, inv, this);
    }

    public void informUpdate() {
        if (level instanceof ServerLevel sl) {
            OMTNetwork.sendToNearby(worldPosition, sl, new MessageTurretBase(this));
        }
        markBlockForUpdate();
        setChanged();
    }

    public void toggleMode() {
        active = !active;
        informUpdate();
    }

    public Map<Direction, ExpanderBlockEntity> getExpanderMap() {
        return expanderMap;
    }

    public static void tick(net.minecraft.world.level.Level level, BlockPos pos, BlockState state,
                             TurretBaseBlockEntity be) {
        if (level.isClientSide) return;

        long time = level.getGameTime();
        if (time % 5 == 0) {
            be.updateExpanders();
            be.updateMaxRange();
        }

        // Tier 1 fuel burning — 1 tick burn time = 1 RF
        if (be.getTier() == 1) {
            int stored = be.energyStorage.getEnergyStored();
            int cap = be.energyStorage.getMaxEnergyStored();
            if (be.burnTime > 0) {
                // Always finish a started item; only bank RF if the buffer has room
                be.burnTime--;
                if (stored < cap) {
                    be.setEnergyStored(stored + 1);
                }
                be.setChanged();
            } else if (stored < cap) {
                ItemStack fuel = be.fuelInventory.getStackInSlot(0);
                if (!fuel.isEmpty()) {
                    int bt = ForgeHooks.getBurnTime(fuel, RecipeType.SMELTING);
                    if (bt > 0) {
                        be.maxBurnTime = bt;
                        be.burnTime = bt;
                        Item fuelItem = fuel.getItem();
                        fuel.shrink(1);
                        if (fuel.isEmpty()) {
                            Item remainder = fuelItem.getCraftingRemainingItem();
                            be.fuelInventory.setStackInSlot(0,
                                    remainder != null ? new ItemStack(remainder) : ItemStack.EMPTY);
                        } else {
                            be.fuelInventory.setStackInSlot(0, fuel);
                        }
                        be.setChanged();
                    }
                }
            }
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        targetingSettings.writeToNBT(tag);
        tag.putBoolean("active", active);
        tag.putBoolean("multiTargeting", multiTargeting);
        tag.putInt("kills", kills);
        tag.putInt("playerKills", playerKills);
        tag.put("fuelInventory", fuelInventory.serializeNBT());
        tag.putInt("burnTime", burnTime);
        tag.putInt("maxBurnTime", maxBurnTime);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.targetingSettings = TargetingSettings.readFromNBT(tag);
        this.active = tag.getBoolean("active");
        this.multiTargeting = tag.getBoolean("multiTargeting");
        this.kills = tag.getInt("kills");
        this.playerKills = tag.getInt("playerKills");
        if (tag.contains("fuelInventory")) fuelInventory.deserializeNBT(tag.getCompound("fuelInventory"));
        this.burnTime = tag.getInt("burnTime");
        this.maxBurnTime = tag.getInt("maxBurnTime");
    }
}

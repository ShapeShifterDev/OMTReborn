package omtreborn.tileentity.turrets;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import omtreborn.config.OMTConfig;
import omtreborn.entity.projectiles.DisposableItemProjectile;
import omtreborn.entity.projectiles.TurretProjectile;
import omtreborn.init.ModBlockEntities;
import omtreborn.init.ModSounds;
import omtreborn.turret.TurretType;

public class DisposableItemTurretBlockEntity extends ProjectileTurretBlockEntity {

    public DisposableItemTurretBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET_DISPOSABLE_ITEM.get(), pos, state, 1);
    }

    @Override
    public TurretType getTurretType() {
        return TurretType.of("disposableItemTurret", OMTConfig.TURRETS.disposable_turret);
    }

    @Override
    public SoundEvent getLaunchSoundEffect() {
        return ModSounds.DISPOSABLE_LAUNCH.get();
    }

    @Override
    public boolean requiresAmmo() { return true; }

    @Override
    public boolean requiresSpecificAmmo() { return false; }

    @Override
    public float getProjectileGravity() { return 0.03f; }

    @Override
    public TurretProjectile createProjectile(Level level, Entity target, ItemStack ammo) {
        return new DisposableItemProjectile(level, this.base, ammo);
    }

    @Override
    public Integer[] getDefaultPriorities() {
        return new Integer[]{2, 1, 3, 0, 4};
    }

    @Override
    protected ItemStack getAmmoStack() {
        if (base == null || !OMTConfig.TURRETS.doTurretsNeedAmmo.get()) return ItemStack.EMPTY;
        // Accept any item from ammo slots (0-8) of the base, then expanders
        ItemStackHandler baseInv = base.getInventory();
        for (int i = 0; i < Math.min(9, baseInv.getSlots()); i++) {
            ItemStack stack = baseInv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                baseInv.extractItem(i, 1, false);
                return stack.copyWithCount(1);
            }
        }
        for (ItemStackHandler inv : base.getAmmoInventories()) {
            if (inv == baseInv) continue;
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    inv.extractItem(i, 1, false);
                    return stack.copyWithCount(1);
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DisposableItemTurretBlockEntity be) {
        tickDirected(level, pos, state, be);
    }
}

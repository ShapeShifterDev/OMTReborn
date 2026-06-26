package omtreborn.init;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import omtreborn.OpenModularTurrets;
import omtreborn.gui.ExpanderInvMenu;
import omtreborn.gui.TurretBaseMenu;
import omtreborn.tileentity.ExpanderBlockEntity;
import omtreborn.tileentity.TurretBaseBlockEntity;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, OpenModularTurrets.MODID);

    public static final RegistryObject<MenuType<TurretBaseMenu>> TURRET_BASE = MENUS.register("turret_base",
            () -> IForgeMenuType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                TurretBaseBlockEntity be = (TurretBaseBlockEntity) inv.player.level().getBlockEntity(pos);
                return new TurretBaseMenu(windowId, inv, be);
            }));

    public static final RegistryObject<MenuType<ExpanderInvMenu>> EXPANDER_INV = MENUS.register("expander_inv",
            () -> IForgeMenuType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                ExpanderBlockEntity be = (ExpanderBlockEntity) inv.player.level().getBlockEntity(pos);
                return new ExpanderInvMenu(windowId, inv, be);
            }));

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}

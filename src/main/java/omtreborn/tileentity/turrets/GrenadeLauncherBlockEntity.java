package omtreborn.tileentity.turrets;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import omtreborn.config.OMTConfig;
import omtreborn.entity.projectiles.GrenadeProjectile;
import omtreborn.entity.projectiles.TurretProjectile;
import omtreborn.init.ModBlockEntities;
import omtreborn.init.ModItems;
import omtreborn.init.ModSounds;
import omtreborn.turret.TurretType;

public class GrenadeLauncherBlockEntity extends ProjectileTurretBlockEntity {

    public GrenadeLauncherBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET_GRENADE_LAUNCHER.get(), pos, state, 2);
    }

    @Override
    public TurretType getTurretType() {
        return TurretType.of("grenadeLauncher", OMTConfig.TURRETS.grenade_turret);
    }

    @Override
    public SoundEvent getLaunchSoundEffect() {
        return ModSounds.GRENADE_LAUNCH.get();
    }

    @Override
    public boolean requiresAmmo() { return true; }

    @Override
    public boolean requiresSpecificAmmo() { return true; }

    @Override
    public ItemStack getAmmo() { return new ItemStack(ModItems.GRENADE.get()); }

    @Override
    protected float getLaunchSoundVolume() { return 2.0f; }

    @Override
    public float getProjectileGravity() { return 0.03f; }

    @Override
    public TurretProjectile createProjectile(Level level, Entity target, ItemStack ammo) {
        return new GrenadeProjectile(level, this.base, ammo);
    }

    @Override
    public Integer[] getDefaultPriorities() {
        return new Integer[]{2, 1, 3, 0, 4};
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GrenadeLauncherBlockEntity be) {
        tickDirected(level, pos, state, be);
    }
}

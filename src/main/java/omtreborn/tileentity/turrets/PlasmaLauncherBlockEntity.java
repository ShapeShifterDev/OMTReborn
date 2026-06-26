package omtreborn.tileentity.turrets;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import omtreborn.config.OMTConfig;
import omtreborn.entity.projectiles.PlasmaProjectile;
import omtreborn.entity.projectiles.TurretProjectile;
import omtreborn.init.ModBlockEntities;
import omtreborn.init.ModSounds;
import omtreborn.turret.TurretType;

public class PlasmaLauncherBlockEntity extends ProjectileTurretBlockEntity {

    public PlasmaLauncherBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET_PLASMA_LAUNCHER.get(), pos, state, 5);
    }

    @Override
    public TurretType getTurretType() {
        return TurretType.of("plasmaLauncher", OMTConfig.TURRETS.plasma_turret);
    }

    @Override
    public SoundEvent getLaunchSoundEffect() {
        return ModSounds.PLASMA_LAUNCH.get();
    }

    @Override
    public boolean requiresAmmo() { return false; }

    @Override
    public boolean requiresSpecificAmmo() { return false; }

    @Override
    protected float getLaunchSoundVolume() { return 4.0f; }

    @Override
    public float getProjectileGravity() { return 0.01f; }

    @Override
    public TurretProjectile createProjectile(Level level, Entity target, ItemStack ammo) {
        return new PlasmaProjectile(level, this.base);
    }

    @Override
    public Integer[] getDefaultPriorities() {
        return new Integer[]{2, 1, 3, 0, 4};
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PlasmaLauncherBlockEntity be) {
        tickDirected(level, pos, state, be);
    }
}

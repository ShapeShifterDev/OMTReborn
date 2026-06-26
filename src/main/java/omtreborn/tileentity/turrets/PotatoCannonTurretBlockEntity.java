package omtreborn.tileentity.turrets;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import omtreborn.config.OMTConfig;
import omtreborn.entity.projectiles.PotatoProjectile;
import omtreborn.entity.projectiles.TurretProjectile;
import omtreborn.init.ModBlockEntities;
import omtreborn.init.ModSounds;
import omtreborn.turret.TurretType;

public class PotatoCannonTurretBlockEntity extends ProjectileTurretBlockEntity {

    public PotatoCannonTurretBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET_POTATO_CANNON.get(), pos, state, 1);
    }

    @Override
    public TurretType getTurretType() {
        return TurretType.of("potatoCannonTurret", OMTConfig.TURRETS.potato_cannon_turret);
    }

    @Override
    public SoundEvent getLaunchSoundEffect() {
        return ModSounds.POTATO_LAUNCH.get();
    }

    @Override
    public boolean requiresAmmo() { return true; }

    @Override
    public boolean requiresSpecificAmmo() { return true; }

    @Override
    public ItemStack getAmmo() { return new ItemStack(Items.POTATO); }

    @Override
    public float getProjectileGravity() { return 0.03f; }

    @Override
    public TurretProjectile createProjectile(Level level, Entity target, ItemStack ammo) {
        return new PotatoProjectile(level, this.base, ammo);
    }

    @Override
    public Integer[] getDefaultPriorities() {
        return new Integer[]{2, 1, 3, 0, 4};
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PotatoCannonTurretBlockEntity be) {
        tickDirected(level, pos, state, be);
    }
}

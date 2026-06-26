package omtreborn.client.render.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import omtreborn.OpenModularTurrets;
import omtreborn.client.render.models.ModelLaserTurret;
import omtreborn.tileentity.turrets.LaserTurretBlockEntity;

@OnlyIn(Dist.CLIENT)
public class LaserTurretBER extends AbstractTurretBER<LaserTurretBlockEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(OpenModularTurrets.MODID, "textures/block/laser_turret.png");
    private final ModelLaserTurret model = new ModelLaserTurret();

    public LaserTurretBER(BlockEntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override protected ResourceLocation getTexture() { return TEXTURE; }

    @Override
    protected void renderModel(LaserTurretBlockEntity be, PoseStack ps, VertexConsumer vc, int light, int overlay) {
        applyPitchYaw(model, be);
        model.renderAll(ps, vc, light, overlay);
    }
}

package omtreborn.client.render.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import omtreborn.OpenModularTurrets;
import omtreborn.client.render.models.ModelRocketTurret;
import omtreborn.tileentity.turrets.RocketTurretBlockEntity;

@OnlyIn(Dist.CLIENT)
public class RocketTurretBER extends AbstractTurretBER<RocketTurretBlockEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(OpenModularTurrets.MODID, "textures/block/rocket_turret.png");
    private final ModelRocketTurret model = new ModelRocketTurret();

    public RocketTurretBER(BlockEntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override protected ResourceLocation getTexture() { return TEXTURE; }

    @Override
    protected void renderModel(RocketTurretBlockEntity be, PoseStack ps, VertexConsumer vc, int light, int overlay) {
        applyPitchYaw(model, be);
        model.renderAll(ps, vc, light, overlay);
    }
}

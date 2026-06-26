package omtreborn.client.render.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import omtreborn.OpenModularTurrets;
import omtreborn.client.render.models.ModelRelativisticTurret;
import omtreborn.tileentity.turrets.RelativisticTurretBlockEntity;

@OnlyIn(Dist.CLIENT)
public class RelativisticTurretBER extends AbstractTurretBER<RelativisticTurretBlockEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(OpenModularTurrets.MODID, "textures/block/relativistic_turret.png");
    private final ModelRelativisticTurret model = new ModelRelativisticTurret();

    public RelativisticTurretBER(BlockEntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override protected ResourceLocation getTexture() { return TEXTURE; }

    @Override
    protected void renderModel(RelativisticTurretBlockEntity be, PoseStack ps, VertexConsumer vc, int light, int overlay) {
        model.setAnimation(be.rotationAnimation);
        model.renderAll(ps, vc, light, overlay);
    }
}

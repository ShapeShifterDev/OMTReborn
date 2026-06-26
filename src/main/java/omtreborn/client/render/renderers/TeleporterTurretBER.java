package omtreborn.client.render.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import omtreborn.OpenModularTurrets;
import omtreborn.client.render.models.ModelTeleporterTurret;
import omtreborn.tileentity.turrets.TeleporterTurretBlockEntity;

@OnlyIn(Dist.CLIENT)
public class TeleporterTurretBER extends AbstractTurretBER<TeleporterTurretBlockEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(OpenModularTurrets.MODID, "textures/block/teleporter_turret.png");
    private final ModelTeleporterTurret model = new ModelTeleporterTurret();

    public TeleporterTurretBER(BlockEntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override protected ResourceLocation getTexture() { return TEXTURE; }

    @Override
    protected void renderModel(TeleporterTurretBlockEntity be, PoseStack ps, VertexConsumer vc, int light, int overlay) {
        model.setAnimation(be.rotationAnimation);
        model.renderAll(ps, vc, light, overlay);
    }
}

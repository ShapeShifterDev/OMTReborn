package omtreborn.client.render.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import omtreborn.OpenModularTurrets;
import omtreborn.client.render.models.ModelPotatoCannon;
import omtreborn.tileentity.turrets.PotatoCannonTurretBlockEntity;

@OnlyIn(Dist.CLIENT)
public class PotatoCannonBER extends AbstractTurretBER<PotatoCannonTurretBlockEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(OpenModularTurrets.MODID, "textures/block/potato_cannon_turret.png");
    private final ModelPotatoCannon model = new ModelPotatoCannon();

    public PotatoCannonBER(BlockEntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override protected ResourceLocation getTexture() { return TEXTURE; }

    @Override
    protected void renderModel(PotatoCannonTurretBlockEntity be, PoseStack ps, VertexConsumer vc, int light, int overlay) {
        applyPitchYaw(model, be);
        model.renderAll(ps, vc, light, overlay);
    }
}

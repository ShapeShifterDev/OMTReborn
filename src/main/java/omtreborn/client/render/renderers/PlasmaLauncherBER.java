package omtreborn.client.render.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import omtreborn.OpenModularTurrets;
import omtreborn.client.render.models.ModelGrenadeLauncher;
import omtreborn.tileentity.turrets.PlasmaLauncherBlockEntity;

@OnlyIn(Dist.CLIENT)
public class PlasmaLauncherBER extends AbstractTurretBER<PlasmaLauncherBlockEntity> {

    // Plasma launcher shares the grenade launcher model and texture
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(OpenModularTurrets.MODID, "textures/block/grenade_turret.png");
    private final ModelGrenadeLauncher model = new ModelGrenadeLauncher();

    public PlasmaLauncherBER(BlockEntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override protected ResourceLocation getTexture() { return TEXTURE; }

    @Override
    protected void renderModel(PlasmaLauncherBlockEntity be, PoseStack ps, VertexConsumer vc, int light, int overlay) {
        applyPitchYaw(model, be);
        model.renderAll(ps, vc, light, overlay);
    }
}

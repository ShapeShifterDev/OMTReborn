package omtreborn.client.render.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import omtreborn.OpenModularTurrets;
import omtreborn.client.render.models.ModelMachineGun;
import omtreborn.tileentity.turrets.GunTurretBlockEntity;

@OnlyIn(Dist.CLIENT)
public class GunTurretBER extends AbstractTurretBER<GunTurretBlockEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(OpenModularTurrets.MODID, "textures/block/machine_gun_turret.png");
    private final ModelMachineGun model = new ModelMachineGun();

    public GunTurretBER(BlockEntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override protected ResourceLocation getTexture() { return TEXTURE; }

    @Override
    protected void renderModel(GunTurretBlockEntity be, PoseStack ps, VertexConsumer vc, int light, int overlay) {
        applyPitchYaw(model, be);
        model.renderAll(ps, vc, light, overlay);
    }
}

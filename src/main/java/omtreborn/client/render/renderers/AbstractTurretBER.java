package omtreborn.client.render.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import omtreborn.blocks.turretheads.BlockAbstractTurretHead;
import omtreborn.client.render.models.AbstractTurretModel;
import omtreborn.tileentity.turrets.AbstractDirectedTurretBlockEntity;
import omtreborn.tileentity.turrets.TurretHeadBlockEntity;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractTurretBER<T extends TurretHeadBlockEntity> implements BlockEntityRenderer<T> {

    protected AbstractTurretBER(BlockEntityRendererProvider.Context ctx) {
    }

    protected abstract ResourceLocation getTexture();

    protected abstract void renderModel(T be, PoseStack ps, VertexConsumer vc, int light, int overlay);

    @Override
    public void render(T be, float partialTick, PoseStack ps, MultiBufferSource buf, int light, int overlay) {
        Direction facing = be.getBlockState().getValue(BlockAbstractTurretHead.FACING);

        ps.pushPose();
        ps.translate(0.5, 1.5, 0.5);
        switch (facing) {
            case UP    -> { /* default orientation */ }
            case DOWN  -> ps.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180));
            case NORTH -> {
                ps.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180));
                ps.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90));
            }
            case SOUTH -> ps.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90));
            case EAST  -> {
                ps.mulPose(com.mojang.math.Axis.YP.rotationDegrees(90));
                ps.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90));
            }
            case WEST  -> {
                ps.mulPose(com.mojang.math.Axis.YP.rotationDegrees(270));
                ps.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90));
            }
        }
        ps.scale(1.0f, -1.0f, -1.0f);

        renderModel(be, ps, buf.getBuffer(RenderType.entityCutoutNoCull(getTexture())), light, overlay);

        ps.popPose();
    }

    protected void applyPitchYaw(AbstractTurretModel model, T be) {
        if (be instanceof AbstractDirectedTurretBlockEntity dir) {
            // Negate pitch: positive pitch (above) → negative xRot → barrel tips up in world
            // Subtract 90° from yaw: barrel default faces south but yaw convention uses 0°=east
            model.setRotationForTarget(
                    -(float) Math.toRadians(dir.getPitch()),
                    (float) Math.toRadians(dir.getYaw() - 90f));
        }
    }
}

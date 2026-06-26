package omtreborn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import omtreborn.client.render.models.AbstractTurretModel;

@OnlyIn(Dist.CLIENT)
public class TurretHeadItemRenderer extends BlockEntityWithoutLevelRenderer {

    private final AbstractTurretModel model;
    private final ResourceLocation texture;

    public TurretHeadItemRenderer(AbstractTurretModel model, ResourceLocation texture) {
        super(null, null);
        this.model = model;
        this.texture = texture;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
                             MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.scale(1.0f, -1.0f, -1.0f);
        model.renderAll(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(texture)),
                packedLight, packedOverlay);
        poseStack.popPose();
    }
}

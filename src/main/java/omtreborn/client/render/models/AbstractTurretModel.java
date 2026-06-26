package omtreborn.client.render.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.PartPose;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractTurretModel {

    @Nullable protected final ModelPart pole;
    @Nullable protected final ModelPart boxUnder;

    protected AbstractTurretModel(@Nullable ModelPart pole, @Nullable ModelPart boxUnder) {
        this.pole = pole;
        this.boxUnder = boxUnder;
    }

    public abstract void setRotationForTarget(float pitchRad, float yawRad);

    public abstract void renderAll(PoseStack poseStack, VertexConsumer vc, int light, int overlay);

    protected static void addBase(PartDefinition root, int texOffU, int texOffV) {
        root.addOrReplaceChild("base",
                CubeListBuilder.create().texOffs(texOffU, texOffV).mirror()
                        .addBox(-6, 7, -6, 12, 1, 12),
                PartPose.offset(0, 16, 0));
    }

    protected static void addPole(PartDefinition root, int texOffU, int texOffV,
                                  float posY, float boxOffY) {
        root.addOrReplaceChild("pole",
                CubeListBuilder.create().texOffs(texOffU, texOffV).mirror()
                        .addBox(-2, boxOffY, -2, 4, 4, 4),
                PartPose.offset(0, posY, 0));
    }

    protected static void addBoxUnder(PartDefinition root, int texOffU, int texOffV) {
        root.addOrReplaceChild("boxUnder",
                CubeListBuilder.create().texOffs(texOffU, texOffV).mirror()
                        .addBox(-4, 3, -4, 8, 1, 8),
                PartPose.offset(0, 16, 0));
    }

    protected static void addBoxLeft(PartDefinition root, int texOffU, int texOffV) {
        root.addOrReplaceChild("boxLeft",
                CubeListBuilder.create().texOffs(texOffU, texOffV).mirror()
                        .addBox(-4, 4, -4, 8, 1, 8),
                PartPose.offsetAndRotation(0, 16, 0, 0, 0, 1.570796f));
    }

    protected static void addBoxRight(PartDefinition root, int texOffU, int texOffV) {
        root.addOrReplaceChild("boxRight",
                CubeListBuilder.create().texOffs(texOffU, texOffV).mirror()
                        .addBox(-4, -5, -4, 8, 1, 8),
                PartPose.offsetAndRotation(0, 16, 0, 0, 0, 1.570796f));
    }

    protected static void addCrossBar(PartDefinition root) {
        root.addOrReplaceChild("crossBar",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                        .addBox(-4, -2, 0, 8, 1, 1),
                PartPose.offset(0, 16, 0));
    }
}

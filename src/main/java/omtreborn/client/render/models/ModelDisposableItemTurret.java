package omtreborn.client.render.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelDisposableItemTurret extends AbstractTurretModel {

    private final ModelPart base, pole, boxUnder, boxLeft, boxRight, crossBar, cannon;

    public ModelDisposableItemTurret() {
        super(null, null);
        ModelPart root = createLayer().bakeRoot();
        base = root.getChild("base"); pole = root.getChild("pole");
        boxUnder = root.getChild("boxUnder"); boxLeft = root.getChild("boxLeft");
        boxRight = root.getChild("boxRight"); crossBar = root.getChild("crossBar");
        cannon = root.getChild("cannon");
    }

    private static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 37).mirror().addBox(-6, 7, -6, 12, 1, 12), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("pole", CubeListBuilder.create().texOffs(0, 28).mirror().addBox(-2, 4, -2, 4, 4, 4), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("boxUnder", CubeListBuilder.create().texOffs(0, 19).mirror().addBox(-4, 3, -4, 8, 1, 8), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("boxLeft", CubeListBuilder.create().texOffs(0, 19).mirror().addBox(-4, 4, -4, 8, 1, 8), PartPose.offsetAndRotation(0, 16, 0, 0, 0, 1.570796f));
        root.addOrReplaceChild("boxRight", CubeListBuilder.create().texOffs(0, 19).mirror().addBox(-4, -5, -4, 8, 1, 8), PartPose.offsetAndRotation(0, 16, 0, 0, 0, 1.570796f));
        root.addOrReplaceChild("crossBar", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4, -2, 0, 8, 1, 1), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("cannon", CubeListBuilder.create().texOffs(20, 0).mirror().addBox(-2, -3, -12, 4, 4, 14), PartPose.offset(0, 16, 0));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override public void setRotationForTarget(float pitchRad, float yawRad) {
        boxUnder.yRot = yawRad; boxLeft.xRot = yawRad; boxRight.xRot = yawRad;
        crossBar.xRot = pitchRad; crossBar.yRot = yawRad;
        cannon.xRot = pitchRad; cannon.yRot = yawRad;
    }

    @Override public void renderAll(PoseStack ps, VertexConsumer vc, int light, int overlay) {
        base.render(ps, vc, light, overlay); pole.render(ps, vc, light, overlay);
        boxUnder.render(ps, vc, light, overlay); boxLeft.render(ps, vc, light, overlay);
        boxRight.render(ps, vc, light, overlay); crossBar.render(ps, vc, light, overlay);
        cannon.render(ps, vc, light, overlay);
    }
}

package omtreborn.client.render.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelRocketTurret extends AbstractTurretModel {

    private final ModelPart base, pole, boxUnder, boxLeft, boxRight, crossBar, launcher, missile1, missile2;

    public ModelRocketTurret() {
        super(null, null);
        ModelPart root = createLayer().bakeRoot();
        base = root.getChild("base"); pole = root.getChild("pole");
        boxUnder = root.getChild("boxUnder"); boxLeft = root.getChild("boxLeft");
        boxRight = root.getChild("boxRight"); crossBar = root.getChild("crossBar");
        launcher = root.getChild("launcher");
        missile1 = root.getChild("missile1"); missile2 = root.getChild("missile2");
    }

    private static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 37).mirror().addBox(-6, 7, -6, 12, 1, 12), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("pole", CubeListBuilder.create().texOffs(0, 28).mirror().addBox(-2, 4, -2, 4, 4, 4), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("boxUnder", CubeListBuilder.create().texOffs(0, 15).mirror().addBox(-4, 3, -4, 8, 1, 8), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("boxLeft", CubeListBuilder.create().texOffs(0, 15).mirror().addBox(-4, 4, -4, 8, 1, 8), PartPose.offsetAndRotation(0, 16, 0, 0, 0, 1.570796f));
        root.addOrReplaceChild("boxRight", CubeListBuilder.create().texOffs(0, 15).mirror().addBox(-4, -5, -4, 8, 1, 8), PartPose.offsetAndRotation(0, 16, 0, 0, 0, 1.570796f));
        root.addOrReplaceChild("crossBar", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4, -2, 0, 8, 1, 1), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("launcher", CubeListBuilder.create().texOffs(36, 0).mirror().addBox(-2, -5, -2, 4, 8, 8), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("missile1", CubeListBuilder.create().texOffs(0, 6).mirror().addBox(-1, -5, -4, 2, 2, 2), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("missile2", CubeListBuilder.create().texOffs(0, 6).mirror().addBox(-1, -1, -4, 2, 2, 2), PartPose.offset(0, 16, 0));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override public void setRotationForTarget(float pitchRad, float yawRad) {
        boxUnder.yRot = yawRad; boxLeft.xRot = yawRad; boxRight.xRot = yawRad;
        crossBar.xRot = pitchRad; crossBar.yRot = yawRad;
        launcher.xRot = pitchRad; launcher.yRot = yawRad;
        missile1.xRot = pitchRad; missile1.yRot = yawRad;
        missile2.xRot = pitchRad; missile2.yRot = yawRad;
    }

    @Override public void renderAll(PoseStack ps, VertexConsumer vc, int light, int overlay) {
        base.render(ps, vc, light, overlay); pole.render(ps, vc, light, overlay);
        boxUnder.render(ps, vc, light, overlay); boxLeft.render(ps, vc, light, overlay);
        boxRight.render(ps, vc, light, overlay); crossBar.render(ps, vc, light, overlay);
        launcher.render(ps, vc, light, overlay);
        missile1.render(ps, vc, light, overlay); missile2.render(ps, vc, light, overlay);
    }
}

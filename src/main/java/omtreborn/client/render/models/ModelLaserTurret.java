package omtreborn.client.render.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelLaserTurret extends AbstractTurretModel {

    private final ModelPart base, pole, boxUnder, boxLeft, boxRight, crossBar,
            chamber, barUnder, barMiddle, barTop, counterWeight;

    public ModelLaserTurret() {
        super(null, null);
        ModelPart root = createLayer().bakeRoot();
        base = root.getChild("base"); pole = root.getChild("pole");
        boxUnder = root.getChild("boxUnder"); boxLeft = root.getChild("boxLeft");
        boxRight = root.getChild("boxRight"); crossBar = root.getChild("crossBar");
        chamber = root.getChild("chamber"); barUnder = root.getChild("barUnder");
        barMiddle = root.getChild("barMiddle"); barTop = root.getChild("barTop");
        counterWeight = root.getChild("counterWeight");
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
        root.addOrReplaceChild("chamber", CubeListBuilder.create().texOffs(20, 0).mirror().addBox(-2, -7, -3, 4, 7, 4), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("barUnder", CubeListBuilder.create().texOffs(37, 0).mirror().addBox(-1, -2, -12, 2, 1, 10), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("barMiddle", CubeListBuilder.create().texOffs(39, 26).mirror().addBox(-1, -4, -8, 2, 1, 7), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("barTop", CubeListBuilder.create().texOffs(37, 16).mirror().addBox(-1, -6, -6, 2, 1, 7), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("counterWeight", CubeListBuilder.create().texOffs(0, 4).mirror().addBox(-2, -6, 1, 4, 4, 4), PartPose.offset(0, 17, 0));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override public void setRotationForTarget(float pitchRad, float yawRad) {
        boxUnder.yRot = yawRad; boxLeft.xRot = yawRad; boxRight.xRot = yawRad;
        crossBar.xRot = pitchRad; crossBar.yRot = yawRad;
        chamber.xRot = pitchRad; chamber.yRot = yawRad;
        barUnder.xRot = pitchRad; barUnder.yRot = yawRad;
        barMiddle.xRot = pitchRad; barMiddle.yRot = yawRad;
        barTop.xRot = pitchRad; barTop.yRot = yawRad;
        counterWeight.xRot = pitchRad; counterWeight.yRot = yawRad;
    }

    @Override public void renderAll(PoseStack ps, VertexConsumer vc, int light, int overlay) {
        base.render(ps, vc, light, overlay); pole.render(ps, vc, light, overlay);
        boxUnder.render(ps, vc, light, overlay); boxLeft.render(ps, vc, light, overlay);
        boxRight.render(ps, vc, light, overlay); crossBar.render(ps, vc, light, overlay);
        chamber.render(ps, vc, light, overlay); barUnder.render(ps, vc, light, overlay);
        barMiddle.render(ps, vc, light, overlay); barTop.render(ps, vc, light, overlay);
        counterWeight.render(ps, vc, light, overlay);
    }
}

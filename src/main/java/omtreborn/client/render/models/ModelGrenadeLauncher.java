package omtreborn.client.render.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelGrenadeLauncher extends AbstractTurretModel {

    private final ModelPart base, pole, boxUnder, boxLeft, boxRight, crossBar, barrel, chamber, shape1;

    public ModelGrenadeLauncher() {
        super(null, null);
        ModelPart root = createLayer().bakeRoot();
        base = root.getChild("base"); pole = root.getChild("pole");
        boxUnder = root.getChild("boxUnder"); boxLeft = root.getChild("boxLeft");
        boxRight = root.getChild("boxRight"); crossBar = root.getChild("crossBar");
        barrel = root.getChild("barrel"); chamber = root.getChild("chamber");
        shape1 = root.getChild("shape1");
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
        root.addOrReplaceChild("barrel", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-2, -4, -8, 4, 4, 12), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("chamber", CubeListBuilder.create().texOffs(35, 20).mirror().addBox(-3, -5, 3, 6, 6, 6), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("shape1", CubeListBuilder.create().texOffs(0, 5).mirror().addBox(-1, -6, -7, 2, 2, 4), PartPose.offset(0, 16, 0));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override public void setRotationForTarget(float pitchRad, float yawRad) {
        boxUnder.yRot = yawRad; boxLeft.xRot = yawRad; boxRight.xRot = yawRad;
        crossBar.xRot = pitchRad; crossBar.yRot = yawRad;
        barrel.xRot = pitchRad; barrel.yRot = yawRad;
        chamber.xRot = pitchRad; chamber.yRot = yawRad;
        shape1.xRot = pitchRad; shape1.yRot = yawRad;
    }

    @Override public void renderAll(PoseStack ps, VertexConsumer vc, int light, int overlay) {
        base.render(ps, vc, light, overlay); pole.render(ps, vc, light, overlay);
        boxUnder.render(ps, vc, light, overlay); boxLeft.render(ps, vc, light, overlay);
        boxRight.render(ps, vc, light, overlay); crossBar.render(ps, vc, light, overlay);
        barrel.render(ps, vc, light, overlay); chamber.render(ps, vc, light, overlay);
        shape1.render(ps, vc, light, overlay);
    }
}

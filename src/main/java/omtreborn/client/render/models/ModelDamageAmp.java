package omtreborn.client.render.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelDamageAmp extends AbstractTurretModel {

    private final ModelPart bottomBar, rod1, rod2, rod3, rod4, rod5, rod6;

    public ModelDamageAmp() {
        super(null, null);
        ModelPart root = createLayer().bakeRoot();
        bottomBar = root.getChild("bottomBar");
        rod1 = root.getChild("rod1"); rod2 = root.getChild("rod2");
        rod3 = root.getChild("rod3"); rod4 = root.getChild("rod4");
        rod5 = root.getChild("rod5"); rod6 = root.getChild("rod6");
    }

    private static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("bottomBar", CubeListBuilder.create().texOffs(4, 15).mirror().addBox(-1.5f, 1, -13, 3, 1, 11), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("rod1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(1.001f, -5, -13, 1, 6, 1), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("rod2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(1.001f, -5, -11, 1, 6, 1), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("rod3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.001f, -5, -9, 1, 6, 1), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("rod4", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.001f, -5, -13, 1, 6, 1), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("rod5", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.001f, -5, -11, 1, 6, 1), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("rod6", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(1.001f, -5, -9, 1, 6, 1), PartPose.offset(0, 16, 0));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override public void setRotationForTarget(float pitchRad, float yawRad) {
        bottomBar.xRot = pitchRad; bottomBar.yRot = yawRad;
        rod1.xRot = pitchRad; rod1.yRot = yawRad;
        rod2.xRot = pitchRad; rod2.yRot = yawRad;
        rod3.xRot = pitchRad; rod3.yRot = yawRad;
        rod4.xRot = pitchRad; rod4.yRot = yawRad;
        rod5.xRot = pitchRad; rod5.yRot = yawRad;
        rod6.xRot = pitchRad; rod6.yRot = yawRad;
    }

    @Override public void renderAll(PoseStack ps, VertexConsumer vc, int light, int overlay) {
        bottomBar.render(ps, vc, light, overlay);
        rod1.render(ps, vc, light, overlay); rod2.render(ps, vc, light, overlay);
        rod3.render(ps, vc, light, overlay); rod4.render(ps, vc, light, overlay);
        rod5.render(ps, vc, light, overlay); rod6.render(ps, vc, light, overlay);
    }
}

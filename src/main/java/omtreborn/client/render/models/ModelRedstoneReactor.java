package omtreborn.client.render.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelRedstoneReactor extends AbstractTurretModel {

    private final ModelPart shape1, shape2, shape3, shape4, shape5;

    public ModelRedstoneReactor() {
        super(null, null);
        ModelPart root = createLayer().bakeRoot();
        shape1 = root.getChild("shape1"); shape2 = root.getChild("shape2");
        shape3 = root.getChild("shape3"); shape4 = root.getChild("shape4");
        shape5 = root.getChild("shape5");
    }

    private static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("shape2", CubeListBuilder.create().texOffs(5, 15).mirror().addBox(-1, 0, 0, 2, 1, 11), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("shape5", CubeListBuilder.create().texOffs(5, 15).mirror().addBox(-1, -3, 0, 2, 1, 11), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("shape1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3, -4, 9, 6, 6, 6), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("shape3", CubeListBuilder.create().texOffs(29, 0).mirror().addBox(-1, -6, 11, 2, 3, 1), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("shape4", CubeListBuilder.create().texOffs(29, 0).mirror().addBox(-1, -6, 13, 2, 3, 1), PartPose.offset(0, 15, 0));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override public void setRotationForTarget(float pitchRad, float yawRad) {
        shape1.xRot = pitchRad; shape1.yRot = yawRad;
        shape2.xRot = pitchRad; shape2.yRot = yawRad;
        shape3.xRot = pitchRad; shape3.yRot = yawRad;
        shape4.xRot = pitchRad; shape4.yRot = yawRad;
        shape5.xRot = pitchRad; shape5.yRot = yawRad;
    }

    @Override public void renderAll(PoseStack ps, VertexConsumer vc, int light, int overlay) {
        shape1.render(ps, vc, light, overlay); shape2.render(ps, vc, light, overlay);
        shape3.render(ps, vc, light, overlay); shape4.render(ps, vc, light, overlay);
        shape5.render(ps, vc, light, overlay);
    }
}

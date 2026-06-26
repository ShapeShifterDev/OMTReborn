package omtreborn.client.render.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelRailgun extends AbstractTurretModel {

    private final ModelPart base, barrelTop, barrelBot, barrelRight, barrelLeft,
            bodyBot, bodyTop, binder, rightGuard, leftGuard, guardBinder;

    public ModelRailgun() {
        super(null, null);
        ModelPart root = createLayer().bakeRoot();
        base = root.getChild("base");
        barrelTop = root.getChild("barrelTop"); barrelBot = root.getChild("barrelBot");
        barrelRight = root.getChild("barrelRight"); barrelLeft = root.getChild("barrelLeft");
        bodyBot = root.getChild("bodyBot"); bodyTop = root.getChild("bodyTop");
        binder = root.getChild("binder");
        rightGuard = root.getChild("rightGuard"); leftGuard = root.getChild("leftGuard");
        guardBinder = root.getChild("guardBinder");
    }

    private static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 37).mirror().addBox(-6, 7, -6, 12, 1, 12), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("barrelTop", CubeListBuilder.create().texOffs(25, 27).mirror().addBox(-1, 2, -16, 2, 1, 17), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("barrelBot", CubeListBuilder.create().texOffs(25, 27).mirror().addBox(-1, -1, -16, 2, 1, 17), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("barrelRight", CubeListBuilder.create().texOffs(25, 45).mirror().addBox(-2, -1, -16, 1, 2, 17), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("barrelLeft", CubeListBuilder.create().texOffs(25, 45).mirror().addBox(1, -1, -16, 1, 2, 17), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("bodyBot", CubeListBuilder.create().texOffs(0, 29).mirror().addBox(-3, 2, 0, 6, 2, 6), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("bodyTop", CubeListBuilder.create().texOffs(0, 37).mirror().addBox(-3, -3, 1, 6, 4, 6), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("binder", CubeListBuilder.create().texOffs(0, 21).mirror().addBox(-1, 1, 3, 2, 1, 1), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("rightGuard", CubeListBuilder.create().texOffs(0, 47).mirror().addBox(-6.1f, -5, -3, 1, 8, 8), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("leftGuard", CubeListBuilder.create().texOffs(0, 47).mirror().addBox(5.1f, -5, -3, 1, 8, 8), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("guardBinder", CubeListBuilder.create().texOffs(0, 25).mirror().addBox(-6, -0.9f, 0, 12, 1, 1), PartPose.offset(0, 15, 0));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override public void setRotationForTarget(float pitchRad, float yawRad) {
        barrelTop.xRot = pitchRad; barrelTop.yRot = yawRad;
        barrelBot.xRot = pitchRad; barrelBot.yRot = yawRad;
        barrelRight.xRot = pitchRad; barrelRight.yRot = yawRad;
        barrelLeft.xRot = pitchRad; barrelLeft.yRot = yawRad;
        bodyBot.xRot = pitchRad; bodyBot.yRot = yawRad;
        bodyTop.xRot = pitchRad; bodyTop.yRot = yawRad;
        guardBinder.xRot = pitchRad; guardBinder.yRot = yawRad;
        rightGuard.xRot = pitchRad; rightGuard.yRot = yawRad;
        leftGuard.xRot = pitchRad; leftGuard.yRot = yawRad;
    }

    @Override public void renderAll(PoseStack ps, VertexConsumer vc, int light, int overlay) {
        base.render(ps, vc, light, overlay);
        barrelTop.render(ps, vc, light, overlay); barrelBot.render(ps, vc, light, overlay);
        barrelRight.render(ps, vc, light, overlay); barrelLeft.render(ps, vc, light, overlay);
        bodyBot.render(ps, vc, light, overlay); bodyTop.render(ps, vc, light, overlay);
        binder.render(ps, vc, light, overlay);
        rightGuard.render(ps, vc, light, overlay); leftGuard.render(ps, vc, light, overlay);
        guardBinder.render(ps, vc, light, overlay);
    }
}

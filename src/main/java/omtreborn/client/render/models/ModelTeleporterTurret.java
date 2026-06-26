package omtreborn.client.render.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelTeleporterTurret extends AbstractTurretModel {

    private final ModelPart base, baseStand, pillarLarge, spinner1, spinner2, spinner3, spinner4;

    public ModelTeleporterTurret() {
        super(null, null);
        ModelPart root = createLayer().bakeRoot();
        base = root.getChild("base"); baseStand = root.getChild("baseStand");
        pillarLarge = root.getChild("pillarLarge");
        spinner1 = root.getChild("spinner1"); spinner2 = root.getChild("spinner2");
        spinner3 = root.getChild("spinner3"); spinner4 = root.getChild("spinner4");
    }

    private static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 37).mirror().addBox(-6, 7, -6, 12, 1, 12), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("baseStand", CubeListBuilder.create().texOffs(0, 51).mirror().addBox(-6, -1, -6, 12, 1, 12), PartPose.offset(0, 13, 0));
        root.addOrReplaceChild("pillarLarge", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2, 0, -2, 4, 10, 4), PartPose.offset(0, 13, 0));
        root.addOrReplaceChild("spinner1", CubeListBuilder.create().texOffs(0, 14).mirror().addBox(-5, 0, -2, 1, 8, 4), PartPose.offset(0, 14, 0));
        root.addOrReplaceChild("spinner2", CubeListBuilder.create().texOffs(0, 26).mirror().addBox(-2, 0, 4, 4, 8, 1), PartPose.offset(0, 14, 0));
        root.addOrReplaceChild("spinner3", CubeListBuilder.create().texOffs(0, 26).mirror().addBox(-2, 0, -5, 4, 8, 1), PartPose.offset(0, 14, 0));
        root.addOrReplaceChild("spinner4", CubeListBuilder.create().texOffs(0, 14).mirror().addBox(4, 0, -2, 1, 8, 4), PartPose.offset(0, 14, 0));
        return LayerDefinition.create(mesh, 64, 64);
    }

    /** Called each frame with the current animation angle (in radians, 0â€“2Ï€). */
    public void setAnimation(float rotAnim) {
        spinner1.yRot = rotAnim;
        spinner2.yRot = rotAnim;
        spinner3.yRot = rotAnim;
        spinner4.yRot = rotAnim;
        pillarLarge.yRot = -rotAnim;
    }

    @Override public void setRotationForTarget(float pitchRad, float yawRad) {
        // No targeting rotation for the teleporter turret
    }

    @Override public void renderAll(PoseStack ps, VertexConsumer vc, int light, int overlay) {
        base.render(ps, vc, light, overlay); baseStand.render(ps, vc, light, overlay);
        pillarLarge.render(ps, vc, light, overlay);
        spinner1.render(ps, vc, light, overlay); spinner2.render(ps, vc, light, overlay);
        spinner3.render(ps, vc, light, overlay); spinner4.render(ps, vc, light, overlay);
    }
}

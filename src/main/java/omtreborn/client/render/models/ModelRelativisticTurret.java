package omtreborn.client.render.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelRelativisticTurret extends AbstractTurretModel {

    private final ModelPart base, spike1, spike2, spike3, spike4, base2, crystal;

    public ModelRelativisticTurret() {
        super(null, null);
        ModelPart root = createLayer().bakeRoot();
        base = root.getChild("base");
        spike1 = root.getChild("spike1"); spike2 = root.getChild("spike2");
        spike3 = root.getChild("spike3"); spike4 = root.getChild("spike4");
        base2 = root.getChild("base2"); crystal = root.getChild("crystal");
    }

    private static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 37).mirror().addBox(-6, 7, -6, 12, 1, 12), PartPose.offset(0, 16, 0));
        root.addOrReplaceChild("spike1", CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-6, 0, -6, 1, 8, 1), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("spike2", CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-6, 0, 5, 1, 8, 1), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("spike3", CubeListBuilder.create().texOffs(24, 0).mirror().addBox(5, 0, -6, 1, 8, 1), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("spike4", CubeListBuilder.create().texOffs(24, 0).mirror().addBox(5, 0, 5, 1, 8, 1), PartPose.offset(0, 15, 0));
        root.addOrReplaceChild("base2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2, 6, -2, 4, 2, 4), PartPose.offset(0, 15, 0));
        // Crystal has initial rotation of 45Â° on all axes
        root.addOrReplaceChild("crystal", CubeListBuilder.create().texOffs(0, 25).mirror().addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(0, 15, 0, 0.7853982f, 0.7853982f, 0.7853982f));
        return LayerDefinition.create(mesh, 64, 64);
    }

    /** Called each frame with the current animation angle (in radians, 0â€“2Ï€). */
    public void setAnimation(float rotAnim) {
        crystal.xRot = rotAnim;
        crystal.yRot = rotAnim;
        crystal.zRot = rotAnim;
    }

    @Override public void setRotationForTarget(float pitchRad, float yawRad) {
        // No targeting rotation for the relativistic turret
    }

    @Override public void renderAll(PoseStack ps, VertexConsumer vc, int light, int overlay) {
        base.render(ps, vc, light, overlay);
        spike1.render(ps, vc, light, overlay); spike2.render(ps, vc, light, overlay);
        spike3.render(ps, vc, light, overlay); spike4.render(ps, vc, light, overlay);
        base2.render(ps, vc, light, overlay); crystal.render(ps, vc, light, overlay);
    }
}

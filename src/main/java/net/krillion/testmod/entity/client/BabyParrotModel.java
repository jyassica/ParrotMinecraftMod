package net.krillion.testmod.entity.client;

import net.krillion.testmod.entity.custom.BabyParrotEntity;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

import net.krillion.testmod.entity.animations.ModAnimationDefinitions;

import net.minecraft.client.model.HierarchicalModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class BabyParrotModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart babyParrot;
	private final ModelPart h_head;
	private final ModelPart body;
	private final ModelPart tail;
	private final ModelPart wings;
	private final ModelPart wingRight;
	private final ModelPart wingLeft;
	private final ModelPart footLeft;
	private final ModelPart footRight;
	private final ModelPart hitbox;

	public BabyParrotModel(ModelPart root) {
		this.babyParrot = root.getChild("babyParrot");
		this.h_head = this.babyParrot.getChild("h_head");
		this.body = this.babyParrot.getChild("body");
		this.tail = this.body.getChild("tail");
		this.wings = this.body.getChild("wings");
		this.wingRight = this.wings.getChild("wingRight");
		this.wingLeft = this.wings.getChild("wingLeft");
		this.footLeft = this.body.getChild("footLeft");
		this.footRight = this.body.getChild("footRight");
		this.hitbox = root.getChild("hitbox");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition babyParrot = partdefinition.addOrReplaceChild("babyParrot", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 1.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition body = babyParrot.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5171F, -4.2611F, -2.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 1.5708F, 1.4399F, 1.5708F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(10, 11).addBox(-1.0F, -2.5F, -3.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, -2.0F, -3.1416F, 0.0F, 3.1416F));

		PartDefinition wings = body.addOrReplaceChild("wings", CubeListBuilder.create(), PartPose.offset(-2.074F, -4.3948F, -0.8527F));

		PartDefinition wingRight = wings.addOrReplaceChild("wingRight", CubeListBuilder.create(), PartPose.offset(1.0F, 1.0F, 0.0F));

		PartDefinition wingr_r1 = wingRight.addOrReplaceChild("wingr_r1", CubeListBuilder.create().texOffs(24, 8).mirror().addBox(0.2274F, -0.3985F, -0.2506F, 3.0F, 2.0F, 0.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1787F, -0.2784F, 2.1543F));

		PartDefinition wingLeft = wings.addOrReplaceChild("wingLeft", CubeListBuilder.create(), PartPose.offset(3.148F, 1.0F, 0.0F));

		PartDefinition wingl_r1 = wingLeft.addOrReplaceChild("wingl_r1", CubeListBuilder.create().texOffs(21, 3).addBox(-3.2274F, -0.3985F, -0.2506F, 3.0F, 2.0F, 0.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1787F, 0.2784F, -2.1543F));

		PartDefinition footLeft = body.addOrReplaceChild("footLeft", CubeListBuilder.create().texOffs(0, 17).addBox(0.0F, 0.0F, -2.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, -1.0F));

		PartDefinition footRight = body.addOrReplaceChild("footRight", CubeListBuilder.create().texOffs(-2, 17).addBox(-2.0F, 0.0F, -3.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition h_head = babyParrot.addOrReplaceChild("h_head", CubeListBuilder.create().texOffs(0, 7).addBox(-1.0F, -3.1305F, -0.9914F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition beak_r1 = h_head.addOrReplaceChild("beak_r1", CubeListBuilder.create().texOffs(12, 3).addBox(-6.0F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -0.1305F, 0.0086F, -3.1416F, 0.0F, 3.1416F));

		PartDefinition beak2_r1 = h_head.addOrReplaceChild("beak2_r1", CubeListBuilder.create().texOffs(12, 0).addBox(-5.8F, -4.0695F, -0.5F, 1.0F, 2.0F, .9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.8695F, 0.0086F, -3.1416F, 0.0F, -2.9234F));

		PartDefinition eye_r1 = h_head.addOrReplaceChild("eye_r1", CubeListBuilder.create().texOffs(10, 7).mirror().addBox(-2.8F, -2.0F, -2.2086F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, -0.1305F, -2.0086F, 2.9234F, 0.0F, -3.1416F));

		PartDefinition eye_r2 = h_head.addOrReplaceChild("eye_r2", CubeListBuilder.create().texOffs(10, 7).addBox(0.8F, -2.0F, -2.2086F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -0.1305F, 2.0086F, -0.2182F, 0.0F, 0.0F));

		PartDefinition hitbox = partdefinition.addOrReplaceChild("hitbox", CubeListBuilder.create().texOffs(0, 21).addBox(-2.0F, -7.0F, -3.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}


	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.babyParrot.getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(ModAnimationDefinitions.BABYPARROT_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((BabyParrotEntity) entity).idleAnimationState, ModAnimationDefinitions.BABYPARROT_IDLE, ageInTicks, 1f);
		this.animate(((BabyParrotEntity) entity).sitAnimationState, ModAnimationDefinitions.BABYPARROT_SIT, ageInTicks, 1f);
	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.h_head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.h_head.zRot = pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int what) {
		babyParrot.render(poseStack, vertexConsumer, packedLight, packedOverlay);
	}



	@Override
	public ModelPart root() {
		return babyParrot;
	}
}
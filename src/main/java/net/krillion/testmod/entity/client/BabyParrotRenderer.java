package net.krillion.testmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.krillion.testmod.TestMod;
import net.krillion.testmod.entity.custom.BabyParrotEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BabyParrotRenderer extends MobRenderer<BabyParrotEntity, BabyParrotModel<BabyParrotEntity>> {

    public BabyParrotRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BabyParrotModel<>(pContext.bakeLayer(ModModelLayers.BABYPARROT_LAYER)), .5f);
    }

    public ResourceLocation getTextureLocation(BabyParrotEntity pEntity) {
        return ResourceLocation.fromNamespaceAndPath(TestMod.MOD_ID, "textures/entity/babyparrot.png");
    }

    @Override
    public void render(BabyParrotEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.scale(0.9f, 0.9f, 0.9f);

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
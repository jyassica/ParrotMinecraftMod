package net.krillion.testmod.event;

import net.krillion.testmod.TestMod;
import net.krillion.testmod.entity.client.ModModelLayers;
import net.krillion.testmod.entity.client.BabyParrotModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TestMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.BABYPARROT_LAYER, BabyParrotModel::createBodyLayer);
    }
}
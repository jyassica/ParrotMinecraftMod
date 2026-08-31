package net.krillion.testmod.event;

import net.krillion.testmod.TestMod;
import net.krillion.testmod.entity.ModEntities;
import net.krillion.testmod.entity.custom.BabyParrotEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TestMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.BABYPARROT.get(), BabyParrotEntity.createAttributes().build());
    }
}
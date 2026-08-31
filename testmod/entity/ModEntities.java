package net.krillion.testmod.entity;

import net.krillion.testmod.entity.custom.BabyParrotEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.krillion.testmod.TestMod;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TestMod.MOD_ID);

    public static final RegistryObject<EntityType<BabyParrotEntity>> BABYPARROT =
            ENTITY_TYPES.register("baby_parrot", () -> EntityType.Builder.of(BabyParrotEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 0.5f).build("baby_parrot"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}

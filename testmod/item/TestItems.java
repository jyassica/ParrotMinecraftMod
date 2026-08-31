package net.krillion.testmod.item;

import net.krillion.testmod.TestMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.krillion.testmod.entity.ModEntities;
import net.minecraftforge.common.ForgeSpawnEggItem;

public class TestItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TestMod.MOD_ID);

    public static final RegistryObject<Item> POOP = ITEMS.register("poop",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BABYPARROT_SPAWN_EGG = ITEMS.register("baby_parrot_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BABYPARROT, 0x7e9680, 0xc5d1c5, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}

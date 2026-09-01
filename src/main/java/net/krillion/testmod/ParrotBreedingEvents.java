package net.krillion.testmod;


import com.mojang.logging.LogUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

public class ParrotBreedingEvents {

    public static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onParrotSpawn(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Parrot)) {
            return;
        }

        Parrot parrot = (Parrot) event.getEntity();

        parrot.goalSelector.addGoal(0, new BreedGoal(parrot, 1.0D));
        parrot.goalSelector.addGoal(3,
                new TemptGoal(parrot, 1.2D, Ingredient.of(
                        Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.TORCHFLOWER_SEEDS
                ), false));

    }

    @SubscribeEvent
    public static void onParrotInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Parrot parrot)) {
            return;
        }

        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);

        // Only adults can be put into breeding mode.
        if (parrot.isBaby()) {
            return;
        }

        // Parrot must already be tamed.
        if (!parrot.isTame()) {
            return;
        }

        // Wheat seeds are our breeding food.
        if (!stack.is(Items.WHEAT_SEEDS)) {
            return;
        }


        if (!parrot.level().isClientSide) {
            parrot.setInLove(player);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        event.setCancellationResult(InteractionResult.sidedSuccess(parrot.level().isClientSide));
        event.setCanceled(true);
    }
}
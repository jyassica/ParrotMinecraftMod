package net.krillion.testmod.mixin;

import net.krillion.testmod.entity.ModEntities;
import net.krillion.testmod.entity.custom.BabyParrotEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Parrot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;


@Mixin(Parrot.class)
public class ParrotMixin {

    @Inject(
            method = "canMate",
            at = @At("RETURN"),
            cancellable = true
    )
    private void parrotbreeding$canMate(
            Animal other,
            CallbackInfoReturnable<Boolean> cir) {

        Parrot self = (Parrot)(Object)this;

        if (!(other instanceof Parrot otherParrot)) {
            cir.setReturnValue(false);
            return;
        }

        if (self.isBaby() || otherParrot.isBaby()) {
            cir.setReturnValue(false);
            return;
        }

        if (!self.isTame() || !otherParrot.isTame()) {
            cir.setReturnValue(false);
            return;
        }

        cir.setReturnValue(true);
    }

    @Inject(
            method = "getBreedOffspring",
            at = @At("RETURN"),
            cancellable = true
    )
    private void parrotbreeding$getBreedOffspring(
            ServerLevel level,
            AgeableMob other,
            CallbackInfoReturnable<AgeableMob> cir) {

        BabyParrotEntity baby = ModEntities.BABYPARROT.get().create(level);

        Parrot parent = (Parrot)other;
        Parrot.Variant inheritedVariant = parent.getVariant();

        baby.setVariantId(inheritedVariant.getId());

        UUID ownerUUID = null;

        if (parent.isTame()) {
            UUID parentOwner = parent.getOwnerUUID();

            if (parentOwner != null) {
                ownerUUID = parentOwner;
            }
        }

        if (ownerUUID != null) {
            baby.setTame(true, false);
            baby.setOwnerUUID(ownerUUID);
        }

        cir.setReturnValue(baby);
    }
}
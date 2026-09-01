package net.krillion.testmod.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.Mob;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;


public class BabyParrotEntity extends TamableAnimal {
    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(
                    BabyParrotEntity.class,
                    EntityDataSerializers.INT
            );
    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(
                    BabyParrotEntity.class,
                    EntityDataSerializers.BOOLEAN
            );

    public BabyParrotEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        refreshDimensions();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        builder.define(VARIANT, 0);
        builder.define(SITTING, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.1);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState sitAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int sitAnimationTimeout = 0;


    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide()) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        // idle animation, perpetually play
        if(this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
        // sitting behavior
        if(this.isSitting() && this.sitAnimationTimeout <= 0){
            this.sitAnimationState.start(this.tickCount);
            this.sitAnimationTimeout += 1;
        } else if (!this.isSitting() && this.sitAnimationTimeout > 0) {
            this.sitAnimationState.stop();
            this.sitAnimationTimeout = 0;
        }
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        // lets try to tame it (wild + has food)
        if (!this.isTame() && itemstack.is(ItemTags.PARROT_FOOD)) {
            itemstack.consume(1, pPlayer);
            // play sound if you dare
            if (!this.isSilent()) {
                this.level()
                        .playSound(
                                null,
                                this.getX(),
                                this.getY(),
                                this.getZ(),
                                SoundEvents.PARROT_EAT,
                                this.getSoundSource(),
                                1.0F,
                                1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F
                        );
            }

            if (!this.level().isClientSide) {
                if (this.random.nextInt(10) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, pPlayer)) {
                    this.tame(pPlayer);
                    this.level().broadcastEntityEvent(this, (byte)7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte)6);
                }
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (!itemstack.is(ItemTags.PARROT_POISONOUS_FOOD)) { // if it is not poisonous food
            if (this.isTame() && this.isOwnedBy(pPlayer)) {
                if (!this.level().isClientSide) {
//                    this.setSitting(!this.isOrderedToSit());
                    this.setOrderedToSit(!this.isOrderedToSit());
                }

                return InteractionResult.sidedSuccess(this.level().isClientSide);
            } else {
                return super.mobInteract(pPlayer, pHand);
            }
        } else { // POISON
            itemstack.consume(1, pPlayer);
            this.addEffect(new MobEffectInstance(MobEffects.POISON, 900));
            if (pPlayer.isCreative() || !this.isInvulnerable()) {
                this.hurt(this.damageSources().playerAttack(pPlayer), Float.MAX_VALUE);
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
    }

    public int getVariantId() {
        return this.entityData.get(VARIANT);
    }

    public void setVariantId(int variantId) {
        this.entityData.set(VARIANT, variantId);
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if(this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1f);
        } else {
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        // sets the Variant part of the compund to the variant id, since i did away with the variant enum
        pCompound.putInt("Variant", this.getVariantId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        // reset the variant, and also "remember" that was sitting for animations
        this.setVariantId(pCompound.getInt("Variant"));
        this.setSitting(pCompound.getBoolean("Sitting"));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new BreedGoal(this, 1.15D));
        this.goalSelector.addGoal(1, new TamableAnimal.TamableAnimalPanicGoal(1.5));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3,
                new TemptGoal(this, 1.2D, Ingredient.of(
                        ItemTags.PARROT_FOOD
                ), false));

        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));

        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @ Override
    public void setInSittingPose(boolean pSitting) {
        super.setInSittingPose(pSitting);
        this.setSitting(pSitting);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (!level().isClientSide && getAge() >= 0) {
            growIntoAdult();
        }
    }

    private void copyToAdult(ServerLevel pLevel, Parrot adult) {
        if (adult == null) {
            return;
        }

        // where it at
        adult.moveTo(
                this.getX(),
                this.getY(),
                this.getZ(),
                this.getYRot(),
                this.getXRot()
        );

        // looking in same direction
        adult.setYHeadRot(this.getYHeadRot());

        // if its tamed or not
        if (this.isTame()) {
            adult.setTame(true, false);
            adult.setOwnerUUID(this.getOwnerUUID());
        }
        // sitting?
        adult.setOrderedToSit(this.isOrderedToSit());
        // parrot type
        adult.setVariant(
                Parrot.Variant.byId(this.getVariantId())
        );

        // if it was named
        if (this.hasCustomName()) {
            adult.setCustomName(this.getCustomName());
            adult.setCustomNameVisible(this.isCustomNameVisible());
        }

        adult.setPersistenceRequired();

        // how much health it had
        adult.setHealth(Math.min(
                this.getHealth(),
                adult.getMaxHealth()
        ));

    }

    private void growIntoAdult() {
        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Parrot parrot = EntityType.PARROT.create(serverLevel);

        if (parrot == null) {
            return;
        }

        serverLevel.addFreshEntity(parrot);

        parrot.copyPosition(this);

        copyToAdult(serverLevel, parrot);

        this.discard();
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
    }


    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(ItemTags.PARROT_FOOD);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PARROT_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.PARROT_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PARROT_DEATH;
    }



}

package dev.stashy.extrasounds.mc1_16_3.mixin.effect;

import dev.stashy.extrasounds.logics.impl.EntitySoundHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * For player's effect add/remove sound.
 */
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends ExtendLivingEntityMixin {
    @Unique
    private final EntitySoundHandler soundHandler = new EntitySoundHandler();

    @Override
    protected void extrasounds$invokeOnStatusEffectApplied_AtHead(StatusEffectInstance effect, CallbackInfo ci) {
        super.extrasounds$invokeOnStatusEffectApplied_AtHead(effect, ci);
        if (!effect.shouldShowIcon() || effect.getDuration() < 1) {
            return;
        }
        this.soundHandler.onEffectChanged(effect.getEffectType(), EntitySoundHandler.EffectType.ADD);
    }

    @Inject(method = "removeStatusEffectInternal", at = @At("HEAD"))
    private void extrasounds$effectRemoved(StatusEffect statusEffect, CallbackInfoReturnable<StatusEffectInstance> cir) {
        StatusEffectInstance effect = getActiveStatusEffects().get(statusEffect);
        if (effect == null || !effect.shouldShowIcon()) {
            return;
        }
        this.soundHandler.onEffectChanged(effect.getEffectType(), EntitySoundHandler.EffectType.REMOVE);
    }
}

package dev.stashy.extrasounds.mc1_16_3.mixin.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class ExtendLivingEntityMixin {
    @Shadow
    public abstract Map<StatusEffect, StatusEffectInstance> getActiveStatusEffects();

    @Inject(method = "onStatusEffectApplied", at = @At("HEAD"))
    protected void extrasounds$invokeOnStatusEffectApplied_AtHead(StatusEffectInstance effect, CallbackInfo ci) {
        // Empty body for overrideable injection point
    }
}

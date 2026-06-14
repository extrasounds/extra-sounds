package dev.stashy.extrasounds.mc1_15_1.mixin.action.entity;

import dev.stashy.extrasounds.logics.impl.EntitySoundHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Unique
    private final EntitySoundHandler soundHandler = new EntitySoundHandler();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "updatePostDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;remove()V"))
    private void extrasounds$poofSound(CallbackInfo ci) {
        if (!this.world.isClient) {
            return;
        }

        this.soundHandler.onDeath(LivingEntity.class.cast(this), this.getBlockPos());
    }
}

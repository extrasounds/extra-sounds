package dev.stashy.extrasounds.logics.mixin.debug;

import dev.stashy.extrasounds.logics.entry.SoundPackLoader;
import net.minecraft.client.sound.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {
    @Unique
    private boolean isFirstTime = true;

    @Inject(method = "init", at = @At("RETURN"))
    private void extrasounds$checkSoundPlayable(CallbackInfo ci) {
        if (this.isFirstTime) {
            SoundPackLoader.checkSoundPlayable();
            this.isFirstTime = false;
        }
    }
}

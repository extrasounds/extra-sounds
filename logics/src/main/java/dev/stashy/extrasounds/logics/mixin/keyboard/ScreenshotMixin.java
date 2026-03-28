package dev.stashy.extrasounds.logics.mixin.keyboard;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.sounds.SoundType;
import dev.stashy.extrasounds.sounds.Sounds;
import net.minecraft.client.Screenshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screenshot.class)
public abstract class ScreenshotMixin {
    @Inject(method = "grab(Ljava/io/File;Ljava/lang/String;Lcom/mojang/blaze3d/pipeline/RenderTarget;ILjava/util/function/Consumer;)V", at = @At("HEAD"))
    private static void extrasounds$screenshotSound(CallbackInfo ci) {
        if (!ExtraSounds.MANAGER.isMuted(SoundType.SCREENSHOT)) {
            ExtraSounds.MANAGER.playSound(Sounds.SCREENSHOT, SoundType.SCREENSHOT);
        }
    }
}

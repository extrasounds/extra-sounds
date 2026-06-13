package dev.stashy.extrasounds.logics.mixin.keyboard;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.sounds.SoundType;
import dev.stashy.extrasounds.sounds.Sounds;
import net.minecraft.client.util.ScreenshotRecorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenshotRecorder.class)
public abstract class ScreenshotRecorderMixin {
    @Inject(method = {
            "method_22690(Ljava/io/File;Ljava/lang/String;Lnet/minecraft/class_276;Ljava/util/function/Consumer;)V",    // saveScreenshot(File, String, Framebuffer, Consumer)
            "method_22690(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/class_276;Ljava/util/function/Consumer;)V",  // saveScreenshot(File, String, int, int, Framebuffer, Consumer)
            "method_22690(Ljava/io/File;Ljava/lang/String;Lnet/minecraft/class_276;ILjava/util/function/Consumer;)V"    // saveScreenshot(File, String, Framebuffer, int, Consumer)
    }, at = @At("HEAD"))
    private static void extrasounds$screenshotSound(CallbackInfo ci) {
        if (!ExtraSounds.MANAGER.isMuted(SoundType.SCREENSHOT)) {
            ExtraSounds.MANAGER.playSound2D(Sounds.SCREENSHOT, SoundType.SCREENSHOT);
        }
    }
}

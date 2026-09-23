package dev.stashy.extrasounds.logics.mixin.resource;

import dev.stashy.extrasounds.logics.entry.SoundPackLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Unique
    private static boolean extrasounds$bFirstTime = true;

    @Inject(method = "handleLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setLevel(Lnet/minecraft/client/multiplayer/ClientLevel;)V", shift = At.Shift.AFTER))
    private void extrasounds$initPacks(CallbackInfo ci) {
        SoundPackLoader.init().thenAcceptAsync((status) -> {
            final CompletableFuture<Void> task;
            if (status == SoundPackLoader.PackStatus.REGENERATED || extrasounds$bFirstTime) {
                extrasounds$bFirstTime = false;
                task = Minecraft.getInstance().reloadResourcePacks();
            } else {
                task = new CompletableFuture<>();
                task.complete(null);
            }
            task.thenRunAsync(SoundPackLoader::checkSoundPlayable);
        });
    }
}

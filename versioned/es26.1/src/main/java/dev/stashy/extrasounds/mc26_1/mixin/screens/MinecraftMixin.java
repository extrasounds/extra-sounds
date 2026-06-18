package dev.stashy.extrasounds.mc26_1.mixin.screens;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.Mixers;
import dev.stashy.extrasounds.sounds.Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * For Screen open/close sound.
 */
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    @Nullable
    public Screen screen;

    @Inject(at = @At("HEAD"), method = "setScreen")
    private void extrasounds$screenChange(@Nullable Screen target, CallbackInfo ci) {
        if (this.screen != target && target instanceof AbstractContainerScreen && !(target instanceof CreativeModeInventoryScreen)) {
            ExtraSounds.MANAGER.playSoundUI(Sounds.INVENTORY_OPEN, Mixers.SCREENS);
        } else if (target == null && this.screen instanceof AbstractContainerScreen) {
            ExtraSounds.MANAGER.playSoundUI(Sounds.INVENTORY_CLOSE, Mixers.SCREENS);
        }
    }
}

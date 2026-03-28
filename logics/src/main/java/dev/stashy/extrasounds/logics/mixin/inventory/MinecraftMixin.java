package dev.stashy.extrasounds.logics.mixin.inventory;

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
    private void extrasounds$screenChange(@Nullable Screen screen, CallbackInfo ci) {
        if (this.screen != screen && screen instanceof AbstractContainerScreen && !(screen instanceof CreativeModeInventoryScreen)) {
            ExtraSounds.MANAGER.playSound(Sounds.INVENTORY_OPEN, 1f, Mixers.INVENTORY);
        } else if (screen == null && this.screen instanceof AbstractContainerScreen) {
            ExtraSounds.MANAGER.playSound(Sounds.INVENTORY_CLOSE, 1f, Mixers.INVENTORY);
        }
    }
}

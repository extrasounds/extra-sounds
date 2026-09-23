package dev.stashy.extrasounds.mc26_4.mixin.typing;

import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ExtendScreenMixin {
    @Inject(method = "changeFocus", at = @At("HEAD"))
    protected void extrasounds$injectChangeFocus_AtHead(ComponentPath componentPath, CallbackInfo ci) {
    }
}

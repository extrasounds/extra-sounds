package dev.stashy.extrasounds.logics.compat.mixin.midnightcontrols;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.impl.VersionedHotbarSoundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseMixin {
    @Unique
    private int currentHotbarSlot = -1;
    @Unique
    private final VersionedHotbarSoundHandler soundHandler = ExtraSounds.MANAGER.getHotbarSoundHandler();

    /**
     * The lambda in 3rd arg of {@code InputUtil#setMouseCallbacks()}
     */
    @Unique
    private static final String METHOD_SIGN_SETUP_CALLBACK_LAMBDA = "method_22684";

    @Inject(method = METHOD_SIGN_SETUP_CALLBACK_LAMBDA, at = @At("HEAD"))
    private void extrasounds$storeHotbarIndex_integrateMidnightControls(CallbackInfo ci) {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        this.currentHotbarSlot = this.soundHandler.getPlayerInventorySlot(player);
    }

    @Inject(method = METHOD_SIGN_SETUP_CALLBACK_LAMBDA, at = @At("RETURN"))
    private void extrasounds$touchHotbar_integrateMidnightControls(CallbackInfo ci) {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        final int selectedSlot = this.soundHandler.getPlayerInventorySlot(player);
        if (selectedSlot != this.currentHotbarSlot) {
            this.soundHandler.onChange();
        }
    }
}

package dev.stashy.extrasounds.logics.mixin.screens;

import com.llamalad7.mixinextras.sugar.Local;
import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.sounds.SoundType;
import dev.stashy.extrasounds.sounds.Sounds;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.container.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

/**
 * For {@link net.minecraft.container.SlotActionType#QUICK_CRAFT} sound on Inventory.
 */
@Mixin(ContainerScreen.class)
public abstract class HandledScreenMixin {
    @Shadow
    protected @Final Set<Slot> cursorDragSlots;

    @Inject(
            method = {  // mouseDragged
                    "mouseDragged(DDIDD)Z",
                    "method_25403(DDIDD)Z",  // >=MC1.16
                    "method_25403(Lnet/minecraft/class_11909;DD)Z",  // >=MC1.21.9
                    "mouseDragged(Lnet/minecraft/client/gui/Click;DD)Z"  // >=MC1.21.9
            },
            at = @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z")
    )
    private void extrasounds$quickCraftSound(CallbackInfoReturnable<Boolean> cir, @Local Slot slot) {
        if (!cursorDragSlots.contains(slot) && !cursorDragSlots.isEmpty()) {
            ExtraSounds.MANAGER.playSound2D(Sounds.ITEM_DRAG, SoundType.PLACE);
        }
    }
}

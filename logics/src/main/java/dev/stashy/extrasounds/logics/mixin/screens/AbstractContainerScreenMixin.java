package dev.stashy.extrasounds.logics.mixin.screens;

import com.llamalad7.mixinextras.sugar.Local;
import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.sounds.SoundType;
import dev.stashy.extrasounds.sounds.Sounds;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

/**
 * For {@link net.minecraft.world.inventory.ContainerInput#QUICK_CRAFT} sound on Inventory.
 */
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {
    @Shadow
    protected @Final Set<Slot> quickCraftSlots;

    @Inject(method = "mouseDragged(Lnet/minecraft/client/input/MouseButtonEvent;DD)Z", at = @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"))
    private void extrasounds$quickCraftSound(CallbackInfoReturnable<Boolean> cir, @Local Slot slot) {
        if (!quickCraftSlots.contains(slot) && !quickCraftSlots.isEmpty()) {
            ExtraSounds.MANAGER.playSound2D(Sounds.ITEM_DRAG, SoundType.PLACE);
        }
    }
}

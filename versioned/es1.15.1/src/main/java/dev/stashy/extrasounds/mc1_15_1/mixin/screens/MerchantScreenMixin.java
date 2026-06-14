package dev.stashy.extrasounds.mc1_15_1.mixin.screens;

import dev.stashy.extrasounds.logics.impl.ScreenScrollHandler;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.container.MerchantContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * For Merchant screen scroll sound.
 */
@Mixin(MerchantScreen.class)
public abstract class MerchantScreenMixin extends AbstractContainerScreen<MerchantContainer> {
    @Unique
    private final ScreenScrollHandler soundHandler = new ScreenScrollHandler();

    @Shadow
    int indexStartOffset;

    public MerchantScreenMixin(MerchantContainer container, PlayerInventory inventory, Text title) {
        super(container, inventory, title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void extrasounds$merchantScreenInit(CallbackInfo ci) {
        this.soundHandler.resetScrollPos();
    }

    @Inject(
            method = {"mouseScrolled", "mouseDragged"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I",
                    shift = At.Shift.AFTER
            )
    )
    private void extrasounds$merchantScreenScroll(CallbackInfoReturnable<Boolean> cir) {
        final int max = this.container.getRecipes().size() - 7;
        this.soundHandler.onScroll(MathHelper.clamp(this.indexStartOffset, 0, max));
    }
}

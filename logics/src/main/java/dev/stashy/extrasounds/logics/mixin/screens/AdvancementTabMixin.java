package dev.stashy.extrasounds.logics.mixin.screens;

import dev.stashy.extrasounds.logics.impl.ScreenScrollHandler;
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AdvancementTab.class)
public abstract class AdvancementTabMixin {
    @Shadow
    double field_2690;
    @Shadow
    double field_2689;

    @Unique
    private final ScreenScrollHandler scrollHandler = new ScreenScrollHandler();
    @Unique
    private static final double SCROLL_THRESHOLD = 16.;

    @Inject(method = "method_2313", at = @At("RETURN"))
    private void extrasounds$advancementScreenScroll(CallbackInfo ci) {
        this.scrollHandler.onScroll((int) (this.field_2690 / SCROLL_THRESHOLD), (int) (this.field_2689 / SCROLL_THRESHOLD));
    }
}

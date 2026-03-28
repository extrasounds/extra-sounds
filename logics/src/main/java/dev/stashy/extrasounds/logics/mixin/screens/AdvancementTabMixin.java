package dev.stashy.extrasounds.logics.mixin.screens;

import dev.stashy.extrasounds.logics.impl.ScreenScrollHandler;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AdvancementTab.class)
public abstract class AdvancementTabMixin {
    @Shadow
    double scrollX;
    @Shadow
    double scrollY;

    @Unique
    private final ScreenScrollHandler scrollHandler = new ScreenScrollHandler();
    @Unique
    private static final double SCROLL_THRESHOLD = 16.;

    @Inject(method = "scroll", at = @At("RETURN"))
    private void extrasounds$advancementScreenScroll(CallbackInfo ci) {
        this.scrollHandler.onScroll((int) (this.scrollX / SCROLL_THRESHOLD), (int) (this.scrollY / SCROLL_THRESHOLD));
    }
}

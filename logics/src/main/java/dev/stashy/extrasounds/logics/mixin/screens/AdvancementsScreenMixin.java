package dev.stashy.extrasounds.logics.mixin.screens;

import com.llamalad7.mixinextras.sugar.Local;
import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.mixin.access.AdvancementTabAccessor;
import dev.stashy.extrasounds.sounds.SoundType;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AdvancementsScreen.class)
public abstract class AdvancementsScreenMixin {
    @Shadow
    @Nullable AdvancementTab selectedTab;

    @Unique
    private static AdvancementTab currentTab;

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientAdvancements;setSelectedTab(Lnet/minecraft/advancements/AdvancementHolder;Z)V"))
    private void extrasounds$changeAdvancementsTab(CallbackInfoReturnable<Boolean> cir, @Local AdvancementTab tab) {
        if (currentTab != tab && tab instanceof AdvancementTabAccessor accessor) {
            ExtraSounds.MANAGER.playSound2D(accessor.getIcon().getItem(), SoundType.DEFAULT);
        }
    }

    @Inject(method = "onSelectedTabChanged", at = @At("RETURN"))
    private void extrasounds$obtainSelectedTab(CallbackInfo ci) {
        currentTab = this.selectedTab;
    }
}

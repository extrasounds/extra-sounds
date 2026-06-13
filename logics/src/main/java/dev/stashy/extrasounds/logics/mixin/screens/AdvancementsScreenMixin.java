package dev.stashy.extrasounds.logics.mixin.screens;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.mixin.access.AdvancementTabAccessor;
import dev.stashy.extrasounds.sounds.SoundType;
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AdvancementsScreen.class)
public abstract class AdvancementsScreenMixin {
    @Shadow
    @Nullable AdvancementTab selectedTab;

    @Unique
    private static AdvancementTab currentTab;

    @WrapOperation(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/advancement/AdvancementTab;isClickOnTab(IIDD)Z"))
    private boolean extrasounds$changeAdvancementsTab(AdvancementTab instance, int screenX, int screenY, double mouseX, double mouseY, Operation<Boolean> original, @Local AdvancementTab tab) {
        final boolean result = original.call(instance, screenX, screenY, mouseX, mouseY);
        if (result && currentTab != tab && tab instanceof AdvancementTabAccessor) {
            AdvancementTabAccessor accessor = (AdvancementTabAccessor) tab;
            ExtraSounds.MANAGER.playSound2D(accessor.getIcon().getItem(), SoundType.DEFAULT);
        }
        return result;
    }

    @Inject(method = "selectTab", at = @At("RETURN"))
    private void extrasounds$obtainSelectedTab(CallbackInfo ci) {
        currentTab = this.selectedTab;
    }
}

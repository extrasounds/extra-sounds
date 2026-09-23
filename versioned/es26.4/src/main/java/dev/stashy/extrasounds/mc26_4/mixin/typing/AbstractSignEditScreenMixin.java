package dev.stashy.extrasounds.mc26_4.mixin.typing;

import dev.stashy.extrasounds.logics.impl.TextFieldHandler;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSignEditScreen.class)
public abstract class AbstractSignEditScreenMixin extends ExtendScreenMixin {
    @Unique
    private final TextFieldHandler soundHandler = new TextFieldHandler();

    @Inject(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractSignEditScreen;setFocused(Lnet/minecraft/client/gui/components/events/GuiEventListener;)V"))
    private void extrasounds$moveRow(CallbackInfoReturnable<Boolean> cir) {
        this.soundHandler.onKey(TextFieldHandler.KeyType.CURSOR);
    }

    @Override
    protected void extrasounds$injectChangeFocus_AtHead(ComponentPath componentPath, CallbackInfo ci) {
        super.extrasounds$injectChangeFocus_AtHead(componentPath, ci);

        final GuiEventListener targetComponent = componentPath.leafComponent();
        if (targetComponent.getClass().getCanonicalName().equals("net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen.SignLineEditBox")) {
            this.soundHandler.onKey(TextFieldHandler.KeyType.CURSOR);
        }
    }
}

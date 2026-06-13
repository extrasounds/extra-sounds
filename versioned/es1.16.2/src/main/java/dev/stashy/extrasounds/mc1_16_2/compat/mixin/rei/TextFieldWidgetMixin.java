package dev.stashy.extrasounds.mc1_16_2.compat.mixin.rei;

import dev.stashy.extrasounds.logics.impl.TextFieldHandler;
import me.shedaniel.rei.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(TextFieldWidget.class)
public abstract class TextFieldWidgetMixin {
    @Unique
    private final TextFieldHandler soundHandler = new TextFieldHandler();

    @Shadow(remap = false)
    protected int selectionStart;
    @Shadow(remap = false)
    protected int selectionEnd;
    @Shadow(remap = false)
    public abstract String getText();
    @Shadow(remap = false)
    public abstract String getSelectedText();

    @Inject(method = "erase", at = @At("HEAD"), remap = false)
    private void extrasounds$eraseStrHead(int offset, CallbackInfo ci) {
        this.soundHandler.onCharErase(offset, this.getText().length(), this.selectionStart, this.selectionEnd);
    }

    @Inject(method = "erase", at = @At("RETURN"), remap = false)
    private void extrasounds$eraseStrReturn(CallbackInfo ci) {
        this.soundHandler.setCursor(this.selectionEnd);
    }

    @Inject(method = "charTyped", at = @At("RETURN"))
    private void extrasounds$appendChar(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || !this.soundHandler.isPosUpdated(this.selectionStart, this.selectionEnd)) {
            return;
        }
        this.soundHandler.onKey(TextFieldHandler.KeyType.INSERT);
        this.soundHandler.setCursor(this.selectionEnd);
    }

    @Inject(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Keyboard;setClipboard(Ljava/lang/String;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void extrasounds$cutAction(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!Screen.isCut(keyCode) || this.getSelectedText().isEmpty()) {
            return;
        }
        this.soundHandler.onKey(TextFieldHandler.KeyType.CUT);
        this.soundHandler.setCursor(this.selectionEnd);
    }

    @Inject(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Keyboard;getClipboard()Ljava/lang/String;",
                    shift = At.Shift.AFTER
            )
    )
    private void extrasounds$pasteAction(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!Screen.isPaste(keyCode) || !this.soundHandler.isPosUpdated(this.selectionStart, this.selectionEnd)) {
            return;
        }
        this.soundHandler.onKey(TextFieldHandler.KeyType.PASTE);
        this.soundHandler.setCursor(this.selectionEnd);
    }

    @Inject(method = "keyPressed",
            at = {
                    @At(value = "INVOKE", target = "Lme/shedaniel/rei/gui/widget/TextFieldWidget;moveCursor(I)V", shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = "Lme/shedaniel/rei/gui/widget/TextFieldWidget;setCursor(I)V", shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = "Lme/shedaniel/rei/gui/widget/TextFieldWidget;setCursorToStart()V", shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = "Lme/shedaniel/rei/gui/widget/TextFieldWidget;setCursorToEnd()V", shift = At.Shift.AFTER)
            }
    )
    private void extrasounds$cursorMoveKeyTyped(CallbackInfoReturnable<Boolean> cir) {
        this.soundHandler.onCursorChanged(this.selectionStart, this.selectionEnd);
    }

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lme/shedaniel/rei/gui/widget/TextFieldWidget;setCursor(I)V", shift = At.Shift.AFTER))
    private void extrasounds$clickEvent(CallbackInfoReturnable<Boolean> cir) {
        this.soundHandler.onCursorChanged(this.selectionStart, this.selectionEnd);
    }
}

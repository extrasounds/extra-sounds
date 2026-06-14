package dev.stashy.extrasounds.mc1_15_2.compat.mixin.rei;

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
    protected int cursorMax;
    @Shadow(remap = false)
    protected int cursorMin;
    @Shadow(remap = false)
    public abstract String getText();
    @Shadow(remap = false)
    public abstract String getSelectedText();

    @Unique
    private int extrasounds$getCursorStart() {
        return Math.min(this.cursorMin, this.cursorMax);
    }

    @Unique
    private int extrasounds$getCursorEnd() {
        return Math.max(this.cursorMin, this.cursorMax);
    }

    @Inject(method = "method_16873", at = @At("HEAD"), remap = false)
    private void extrasounds$eraseStrHead(int offset, CallbackInfo ci) {
        this.soundHandler.onCharErase(offset, this.getText().length(), this.extrasounds$getCursorStart(), this.extrasounds$getCursorEnd());
    }

    @Inject(method = "method_16873", at = @At("RETURN"), remap = false)
    private void extrasounds$eraseStrReturn(CallbackInfo ci) {
        this.soundHandler.setCursor(this.extrasounds$getCursorEnd());
    }

    @Inject(method = "charTyped", at = @At("RETURN"))
    private void extrasounds$appendChar(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || !this.soundHandler.isPosUpdated(this.extrasounds$getCursorStart(), this.extrasounds$getCursorEnd())) {
            return;
        }
        this.soundHandler.onKey(TextFieldHandler.KeyType.INSERT);
        this.soundHandler.setCursor(this.extrasounds$getCursorEnd());
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
        this.soundHandler.setCursor(this.extrasounds$getCursorEnd());
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
        if (!Screen.isPaste(keyCode) || !this.soundHandler.isPosUpdated(this.extrasounds$getCursorStart(), this.extrasounds$getCursorEnd())) {
            return;
        }
        this.soundHandler.onKey(TextFieldHandler.KeyType.PASTE);
        this.soundHandler.setCursor(this.extrasounds$getCursorEnd());
    }

    @Inject(method = "keyPressed",
            at = {
                    @At(value = "INVOKE", target = "Lme/shedaniel/rei/gui/widget/TextFieldWidget;moveCursor(IZ)V", shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = "Lme/shedaniel/rei/gui/widget/TextFieldWidget;moveCursorTo(IZ)V", shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = "Lme/shedaniel/rei/gui/widget/TextFieldWidget;moveCursorToHead()V", shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = "Lme/shedaniel/rei/gui/widget/TextFieldWidget;moveCursorToEnd()V", shift = At.Shift.AFTER)
            }
    )
    private void extrasounds$cursorMoveKeyTyped(CallbackInfoReturnable<Boolean> cir) {
        this.soundHandler.onCursorChanged(this.extrasounds$getCursorStart(), this.extrasounds$getCursorEnd());
    }

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lme/shedaniel/rei/gui/widget/TextFieldWidget;moveCursorTo(IZ)V", shift = At.Shift.AFTER))
    private void extrasounds$clickEvent(CallbackInfoReturnable<Boolean> cir) {
        this.soundHandler.onCursorChanged(this.extrasounds$getCursorStart(), this.extrasounds$getCursorEnd());
    }
}

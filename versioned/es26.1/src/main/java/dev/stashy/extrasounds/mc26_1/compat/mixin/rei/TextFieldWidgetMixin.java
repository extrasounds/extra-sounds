package dev.stashy.extrasounds.mc26_1.compat.mixin.rei;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.stashy.extrasounds.logics.impl.TextFieldHandler;
import me.shedaniel.rei.api.client.gui.widgets.TextField;
import me.shedaniel.rei.impl.client.gui.widget.basewidgets.TextFieldWidget;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(TextFieldWidget.class)
public abstract class TextFieldWidgetMixin implements TextField {
    @Unique
    private final TextFieldHandler soundHandler = new TextFieldHandler();

    @Shadow
    protected int cursorPos;
    @Shadow
    protected int highlightPos;

    @WrapMethod(method = "erase")
    private void extrasounds$eraseStr(int offset, Operation<Void> original) {
        this.soundHandler.onCharErase(offset, this.getText().length(), this.cursorPos, this.highlightPos);
        original.call(offset);
        this.soundHandler.setCursor(this.highlightPos);
    }

    @Inject(method = "charTyped", at = @At("RETURN"))
    private void extrasounds$appendChar(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || !this.soundHandler.isPosUpdated(this.cursorPos, this.highlightPos)) {
            return;
        }
        this.soundHandler.onKey(TextFieldHandler.KeyType.INSERT);
        this.soundHandler.setCursor(this.highlightPos);
    }

    @Inject(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/KeyboardHandler;setClipboard(Ljava/lang/String;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void extrasounds$cutAction(KeyEvent input, CallbackInfoReturnable<Boolean> cir) {
        if (!input.isCut() || this.getSelectedText().isEmpty()) {
            return;
        }
        this.soundHandler.onKey(TextFieldHandler.KeyType.CUT);
        this.soundHandler.setCursor(this.highlightPos);
    }

    @WrapOperation(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lme/shedaniel/rei/impl/client/gui/widget/basewidgets/TextFieldWidget;addText(Ljava/lang/String;)V"
            )
    )
    private void extrasounds$pasteAction(TextFieldWidget instance, String clipboard, Operation<Void> original, KeyEvent input) {
        original.call(instance, clipboard);
        if (input.isPaste() && !clipboard.isEmpty()) {
            this.soundHandler.onKey(TextFieldHandler.KeyType.PASTE);
        }
        this.soundHandler.setCursor(this.highlightPos);
    }

    @Inject(method = "keyPressed",
            at = {
                    @At(value = "INVOKE", target = "Lme/shedaniel/rei/impl/client/gui/widget/basewidgets/TextFieldWidget;moveCursor(I)V", shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = "Lme/shedaniel/rei/impl/client/gui/widget/basewidgets/TextFieldWidget;moveCursorTo(I)V", shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = "Lme/shedaniel/rei/impl/client/gui/widget/basewidgets/TextFieldWidget;moveCursorToStart()V", shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = "Lme/shedaniel/rei/impl/client/gui/widget/basewidgets/TextFieldWidget;moveCursorToEnd()V", shift = At.Shift.AFTER)
            }
    )
    private void extrasounds$cursorMoveKeyTyped(CallbackInfoReturnable<Boolean> cir) {
        this.soundHandler.onCursorChanged(this.cursorPos, this.highlightPos);
    }

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lme/shedaniel/rei/impl/client/gui/widget/basewidgets/TextFieldWidget;moveCursorTo(I)V", shift = At.Shift.AFTER))
    private void extrasounds$clickEvent(CallbackInfoReturnable<Boolean> cir) {
        this.soundHandler.onCursorChanged(this.cursorPos, this.highlightPos);
    }
}

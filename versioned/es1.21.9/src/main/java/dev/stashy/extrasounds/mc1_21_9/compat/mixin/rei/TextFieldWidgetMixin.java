package dev.stashy.extrasounds.mc1_21_9.compat.mixin.rei;

import dev.stashy.extrasounds.logics.impl.TextFieldHandler;
import dev.stashy.extrasounds.logics.runtime.RecordKeyInputProvider;
import me.shedaniel.rei.api.client.gui.widgets.TextField;
import me.shedaniel.rei.impl.client.gui.widget.basewidgets.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Pseudo
@Mixin(TextFieldWidget.class)
public abstract class TextFieldWidgetMixin implements TextField {
    @Unique
    private final TextFieldHandler soundHandler = new TextFieldHandler();
    @Unique
    @NotNull
    private static final RecordKeyInputProvider KEY_INPUT_PROVIDER = Objects.requireNonNull(RecordKeyInputProvider.INSTANCE);

    @Shadow(remap = false)
    protected int cursorPos;

    @Inject(method = "erase", at = @At("HEAD"), remap = false)
    private void extrasounds$eraseStrHead(int offset, CallbackInfo ci) {
        this.soundHandler.onCharErase(offset, this.getText().length(), this.cursorPos, this.cursorPos);
    }

    @Inject(method = "erase", at = @At("RETURN"), remap = false)
    private void extrasounds$eraseStrReturn(CallbackInfo ci) {
        this.soundHandler.setCursor(this.cursorPos);
    }

    @Inject(method = "charTyped", at = @At("RETURN"))
    private void extrasounds$appendChar(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || !this.soundHandler.isPosUpdated(this.cursorPos, this.cursorPos)) {
            return;
        }
        this.soundHandler.onKey(TextFieldHandler.KeyType.INSERT);
        this.soundHandler.setCursor(this.cursorPos);
    }

    @Inject(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Keyboard;setClipboard(Ljava/lang/String;)V",
                    shift = At.Shift.AFTER
            ), require = 0
    )
    private void extrasounds$cutAction(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        try {
            if (!KEY_INPUT_PROVIDER.invokeKeyInput$isCut(input) || this.getSelectedText().isEmpty()) {
                return;
            }
            this.soundHandler.onKey(TextFieldHandler.KeyType.CUT);
            this.soundHandler.setCursor(this.cursorPos);
        } catch (Exception ignored) {
        }
    }

    @Inject(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Keyboard;getClipboard()Ljava/lang/String;",
                    shift = At.Shift.AFTER
            ), require = 0
    )
    private void extrasounds$pasteAction(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        try {
            if (!KEY_INPUT_PROVIDER.invokeKeyInput$isPaste(input) || !this.soundHandler.isPosUpdated(this.cursorPos, this.cursorPos)) {
                return;
            }
            this.soundHandler.onKey(TextFieldHandler.KeyType.PASTE);
            this.soundHandler.setCursor(this.cursorPos);
        } catch (Exception ignored) {
        }
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
        this.soundHandler.onCursorChanged(this.cursorPos, this.cursorPos);
    }

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lme/shedaniel/rei/impl/client/gui/widget/basewidgets/TextFieldWidget;moveCursorTo(I)V", shift = At.Shift.AFTER))
    private void extrasounds$clickEvent(CallbackInfoReturnable<Boolean> cir) {
        this.soundHandler.onCursorChanged(this.cursorPos, this.cursorPos);
    }
}

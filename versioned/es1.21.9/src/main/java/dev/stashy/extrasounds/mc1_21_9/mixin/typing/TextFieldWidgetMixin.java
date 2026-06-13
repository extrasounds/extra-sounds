package dev.stashy.extrasounds.mc1_21_9.mixin.typing;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.impl.TextFieldHandler;
import dev.stashy.extrasounds.logics.runtime.RecordKeyInputProvider;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(TextFieldWidget.class)
public abstract class TextFieldWidgetMixin {
    @Unique
    @NotNull
    private static final RecordKeyInputProvider KEY_INPUT_PROVIDER = Objects.requireNonNull(RecordKeyInputProvider.INSTANCE);
    @Unique
    private final TextFieldHandler soundHandler = new TextFieldHandler();

    // #region method signatures
    // <editor-fold desc="method signatures">
    @Unique
    private static final String METHOD_SIGN_SET_CURSOR = "Lnet/minecraft/client/gui/widget/TextFieldWidget;setCursor(IZ)V";
    @Unique
    private static final String METHOD_SIGN_MOVE_CURSOR = "Lnet/minecraft/client/gui/widget/TextFieldWidget;moveCursor(IZ)V";
    @Unique
    private static final String METHOD_SIGN_CURSOR_TO_START = "Lnet/minecraft/client/gui/widget/TextFieldWidget;setCursorToStart(Z)V";
    @Unique
    private static final String METHOD_SIGN_CURSOR_TO_END = "Lnet/minecraft/client/gui/widget/TextFieldWidget;setCursorToEnd(Z)V";
    // </editor-fold>
    // #endregion

    @Shadow
    private int selectionStart;
    @Shadow
    private int selectionEnd;

    @Shadow
    public abstract String getSelectedText();

    @Shadow
    public abstract String getText();

    @Inject(method = "erase", at = @At("HEAD"))
    private void extrasounds$eraseStrHead(int offset, boolean shiftDown, CallbackInfo ci) {
        this.soundHandler.onCharErase(offset, this.getText().length(), this.selectionStart, this.selectionEnd);
    }

    @Inject(method = "erase", at = @At("RETURN"))
    private void extrasounds$eraseStrReturn(CallbackInfo ci) {
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
    private void extrasounds$cutAction(KeyInput keyInput, CallbackInfoReturnable<Boolean> cir) {
        try {
            if (!KEY_INPUT_PROVIDER.invokeKeyInput$isCut(keyInput) || this.getSelectedText().isEmpty()) {
                return;
            }
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Cannot invoke KeyInput#isCut", ex);
        }
        this.soundHandler.onKey(TextFieldHandler.KeyType.CUT);
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
                    target = "Lnet/minecraft/client/Keyboard;getClipboard()Ljava/lang/String;",
                    shift = At.Shift.AFTER
            )
    )
    private void extrasounds$pasteAction(KeyInput keyInput, CallbackInfoReturnable<Boolean> cir) {
        try {
            if (!KEY_INPUT_PROVIDER.invokeKeyInput$isPaste(keyInput) || !this.soundHandler.isPosUpdated(this.selectionStart, this.selectionEnd)) {
                return;
            }
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Cannot invoke KeyInput#isPaste", ex);
        }
        this.soundHandler.onKey(TextFieldHandler.KeyType.PASTE);
        this.soundHandler.setCursor(this.selectionEnd);
    }

    @Inject(method = "keyPressed",
            at = {
                    @At(value = "INVOKE", target = METHOD_SIGN_SET_CURSOR, shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = METHOD_SIGN_MOVE_CURSOR, shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = METHOD_SIGN_CURSOR_TO_START, shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = METHOD_SIGN_CURSOR_TO_END, shift = At.Shift.AFTER)
            }
    )
    private void extrasounds$cursorMoveKeyTyped(CallbackInfoReturnable<Boolean> cir) {
        this.soundHandler.onCursorChanged(this.selectionStart, this.selectionEnd);
    }

    @Inject(method = {"onClick", "onDrag"}, at = @At("RETURN"))
    private void extrasounds$clickEvent(CallbackInfo ci) {
        this.soundHandler.onCursorChanged(this.selectionStart, this.selectionEnd);
    }

    @Inject(method = "setText", at = @At(value = "INVOKE", target = METHOD_SIGN_CURSOR_TO_END))
    private void extrasounds$autoComplete(CallbackInfo ci) {
        this.soundHandler.setCursor(this.getText().length());
    }
}

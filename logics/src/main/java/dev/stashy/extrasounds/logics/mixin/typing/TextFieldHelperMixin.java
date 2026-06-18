package dev.stashy.extrasounds.logics.mixin.typing;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.stashy.extrasounds.logics.impl.TextFieldHandler;
import net.minecraft.client.gui.font.TextFieldHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(TextFieldHelper.class)
public abstract class TextFieldHelperMixin {
    @Unique
    private final TextFieldHandler soundHandler = new TextFieldHandler();
    @Unique
    private boolean bPasteAction = false;

    @Shadow
    private int cursorPos;
    @Shadow
    private int selectionPos;
    @Shadow
    private @Final Supplier<String> getMessageFn;

    @WrapMethod(method = "removeCharsFromCursor(I)V")
    private void extrasounds$beforeDelete(int count, Operation<Void> original) {
        final String text = this.getMessageFn.get();
        this.soundHandler.onCharErase(count, text.length(), this.cursorPos, this.selectionPos);
        original.call(count);
        this.soundHandler.setCursor(this.selectionPos);
    }

    @WrapMethod(method = "cut")
    private void extrasounds$cutAction(Operation<Void> original) {
        if (this.cursorPos != this.selectionPos) {
            this.soundHandler.onKey(TextFieldHandler.KeyType.CUT);
        }
        original.call();
        this.soundHandler.setCursor(this.selectionPos);
    }

    @Inject(method = "insertText(Ljava/lang/String;Ljava/lang/String;)V", at = @At("RETURN"))
    private void extrasounds$appendChar(String string, String insertion, CallbackInfo ci) {
        if (!this.soundHandler.isPosUpdated(this.cursorPos, this.selectionPos)) {
            return;
        }
        if (this.bPasteAction) {
            this.soundHandler.onKey(TextFieldHandler.KeyType.PASTE);
            this.bPasteAction = false;
        } else if (insertion.equals("\n")) {
            this.soundHandler.onKey(TextFieldHandler.KeyType.RETURN);
        } else {
            this.soundHandler.onKey(TextFieldHandler.KeyType.INSERT);
        }
        this.soundHandler.setCursor(this.selectionPos);
    }

    @Inject(method = "paste", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/font/TextFieldHelper;insertText(Ljava/lang/String;Ljava/lang/String;)V"))
    private void extrasounds$pasteAction(CallbackInfo ci) {
        this.bPasteAction = true;
    }

    @Inject(method = "resetSelectionIfNeeded(Z)V", at = @At("RETURN"))
    private void extrasounds$moveCursor(boolean shiftDown, CallbackInfo ci) {
        if (!this.soundHandler.isPosUpdated(this.cursorPos, this.selectionPos)) {
            return;
        }
        this.soundHandler.onKey(TextFieldHandler.KeyType.CURSOR);
        this.soundHandler.setCursorStart(this.cursorPos);
        this.soundHandler.setCursorEnd(this.selectionPos);
    }
}

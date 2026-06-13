package dev.stashy.extrasounds.mc1_21_6.mixin.typing;

import dev.stashy.extrasounds.logics.impl.TextFieldHandler;
import net.minecraft.client.gui.EditBox;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EditBox.class)
public abstract class EditBoxMixin {
    @Unique
    private final TextFieldHandler soundHandler = new TextFieldHandler();
    @Unique
    private boolean bPasteAction = false;
    @Unique
    private boolean bCutAction = false;

    @Shadow
    private int cursor;
    @Shadow
    private int selectionEnd;
    @Shadow
    private String text;

    @Shadow
    public abstract boolean hasSelection();

    @Inject(method = "delete(I)V", at = @At("HEAD"))
    private void extrasounds$delete(int offset, CallbackInfo ci) {
        this.soundHandler.onCharErase(offset, this.text.length(), this.cursor, this.selectionEnd);
    }

    @Inject(method = "replaceSelection(Ljava/lang/String;)V", at = @At("HEAD"))
    private void extrasounds$replaceSelection(String replacement, CallbackInfo ci) {
        if (!replacement.isEmpty()) {
            if (this.bPasteAction) {
                this.soundHandler.onKey(TextFieldHandler.KeyType.PASTE);
                this.bPasteAction = false;
            } else if (replacement.equals("\n")) {
                this.soundHandler.onKey(TextFieldHandler.KeyType.RETURN);
            } else {
                this.soundHandler.onKey(TextFieldHandler.KeyType.INSERT);
            }
        } else if (this.bCutAction && this.hasSelection()) {
            this.soundHandler.onKey(TextFieldHandler.KeyType.CUT);
            this.bCutAction = false;
        }
        this.soundHandler.setCursor(this.selectionEnd);
    }

    @Inject(method = "handleSpecialKey", at = @At("HEAD"))
    private void extrasounds$specialAction(int keyCode, CallbackInfoReturnable<Boolean> cir) {
        this.bPasteAction = Screen.isPaste(keyCode);
        this.bCutAction = Screen.isCut(keyCode);
    }

    @Inject(method = "moveCursor(Lnet/minecraft/client/input/CursorMovement;I)V", at = @At("RETURN"))
    private void extrasounds$moveCursor(CallbackInfo ci) {
        if (!this.soundHandler.isPosUpdated(this.cursor, this.selectionEnd)) {
            return;
        }
        this.soundHandler.onKey(TextFieldHandler.KeyType.CURSOR);
        this.soundHandler.setCursorStart(this.cursor);
        this.soundHandler.setCursorEnd(this.selectionEnd);
    }
}

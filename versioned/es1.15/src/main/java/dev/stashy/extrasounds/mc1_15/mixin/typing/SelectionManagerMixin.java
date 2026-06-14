package dev.stashy.extrasounds.mc1_15.mixin.typing;

import dev.stashy.extrasounds.logics.impl.TextFieldHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.SelectionManager;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(SelectionManager.class)
public abstract class SelectionManagerMixin {
    @Unique
    private final TextFieldHandler soundHandler = new TextFieldHandler();
    @Unique
    private boolean bPasteAction = false;
    @Unique
    private boolean bMoveAction = false;
    @Unique
    private boolean bDeleteAction = false;

    @Unique
    private static final String METHOD_SIGN_DELETE = "delete(I)V";

    @Shadow
    private int selectionStart;
    @Shadow
    private int selectionEnd;
    @Shadow
    private @Final Supplier<String> stringGetter;

    @Unique
    private static boolean extrasounds$isMoveKey(int keyCode) {
        return keyCode == GLFW.GLFW_KEY_LEFT ||
                keyCode == GLFW.GLFW_KEY_RIGHT ||
                keyCode == GLFW.GLFW_KEY_UP ||
                keyCode == GLFW.GLFW_KEY_DOWN ||
                keyCode == GLFW.GLFW_KEY_HOME ||
                keyCode == GLFW.GLFW_KEY_END;
    }

    @Inject(method = "handleSpecialKey", at = @At("HEAD"))
    private void extrasounds$specialAction(int keyCode, CallbackInfoReturnable<Boolean> cir) {
        if (Screen.isCut(keyCode) && this.selectionStart != this.selectionEnd) {
            this.soundHandler.onKey(TextFieldHandler.KeyType.CUT);
        } else if (Screen.isPaste(keyCode)) {
            this.bPasteAction = true;
        } else if (extrasounds$isMoveKey(keyCode)) {
            this.bMoveAction = true;
        } else if (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE) {
            final int offset = (keyCode == GLFW.GLFW_KEY_BACKSPACE) ? -1 : 1;
            final String text = this.stringGetter.get();
            this.soundHandler.onCharErase(offset, text.length(), this.selectionStart, this.selectionEnd);
            this.bDeleteAction = true;
        }
    }

    @Inject(method = "handleSpecialKey", at = @At("RETURN"))
    private void extrasounds$afterAction(CallbackInfoReturnable<Boolean> cir) {
        if (this.bMoveAction) {
            if (this.soundHandler.isPosUpdated(this.selectionStart, this.selectionEnd)) {
                this.soundHandler.onKey(TextFieldHandler.KeyType.CURSOR);
                this.soundHandler.setCursor(this.selectionEnd);
            }
            this.bMoveAction = false;
        }
        if (this.bDeleteAction) {
            this.soundHandler.setCursor(this.selectionEnd);
            this.bDeleteAction = false;
        }
    }

    @Inject(method = "insert(Ljava/lang/String;)V", at = @At("RETURN"))
    private void extrasounds$appendChar(String insertion, CallbackInfo ci) {
        if (!this.soundHandler.isPosUpdated(this.selectionStart, this.selectionEnd)) {
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
        this.soundHandler.setCursor(this.selectionEnd);
    }
}

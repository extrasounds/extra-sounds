package dev.stashy.extrasounds.mc1_14.mixin.typing;

import dev.stashy.extrasounds.logics.impl.TextFieldHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin {
    @Unique
    private final TextFieldHandler soundHandler = new TextFieldHandler();
    @Unique
    private boolean bPasteAction = false;
    @Unique
    private boolean bMoveAction = false;
    @Unique
    private boolean bDeleteAction = false;

    @Shadow
    private int cursorIndex;

    @Shadow
    abstract String getCurrentPageContent();

    @Shadow
    abstract String getHighlightedText();

    @Unique
    private static boolean extrasounds$isMoveKey(int keyCode) {
        return keyCode == GLFW.GLFW_KEY_LEFT ||
                keyCode == GLFW.GLFW_KEY_RIGHT ||
                keyCode == GLFW.GLFW_KEY_UP ||
                keyCode == GLFW.GLFW_KEY_DOWN ||
                keyCode == GLFW.GLFW_KEY_HOME ||
                keyCode == GLFW.GLFW_KEY_END;
    }

    @Inject(method = "keyPressedEditMode", at = @At("HEAD"))
    private void extrasounds$specialAction(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (Screen.isCut(keyCode) && !this.getHighlightedText().isEmpty()) {
            this.soundHandler.onKey(TextFieldHandler.KeyType.CUT);
        } else if (Screen.isPaste(keyCode)) {
            this.bPasteAction = true;
        } else if (extrasounds$isMoveKey(keyCode)) {
            this.bMoveAction = true;
        } else if (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE) {
            final int offset = (keyCode == GLFW.GLFW_KEY_BACKSPACE) ? -1 : 1;
            final String text = this.getCurrentPageContent();
            this.soundHandler.onCharErase(offset, text.length(), this.cursorIndex, this.cursorIndex);
            this.bDeleteAction = true;
        }
    }

    @Inject(method = "keyPressedEditMode", at = @At("RETURN"))
    private void extrasounds$afterAction(CallbackInfoReturnable<Boolean> cir) {
        if (this.bMoveAction) {
            if (this.soundHandler.isPosUpdated(this.cursorIndex, this.cursorIndex)) {
                this.soundHandler.onKey(TextFieldHandler.KeyType.CURSOR);
                this.soundHandler.setCursor(this.cursorIndex);
            }
            this.bMoveAction = false;
        }
        if (this.bDeleteAction) {
            this.soundHandler.setCursor(this.cursorIndex);
            this.bDeleteAction = false;
        }
        if (this.bPasteAction) {
            if (this.soundHandler.isPosUpdated(this.cursorIndex, this.cursorIndex)) {
                this.soundHandler.onKey(TextFieldHandler.KeyType.PASTE);
                this.soundHandler.setCursor(this.cursorIndex);
            }
            this.bPasteAction = false;
        }
    }

    @Inject(method = "writeString", at = @At("RETURN"))
    private void extrasounds$appendChar(String insertion, CallbackInfo ci) {
        if (!this.soundHandler.isPosUpdated(this.cursorIndex, this.cursorIndex)) {
            return;
        }
        if (insertion.equals("\n")) {
            this.soundHandler.onKey(TextFieldHandler.KeyType.RETURN);
        } else {
            this.soundHandler.onKey(TextFieldHandler.KeyType.INSERT);
        }
        this.soundHandler.setCursor(this.cursorIndex);
    }

    @Inject(method = {"mouseClicked", "mouseDragged"}, at = @At("RETURN"))
    private void extrasounds$moveCursor(CallbackInfoReturnable<Boolean> cir) {
        if (this.soundHandler.isPosUpdated(this.cursorIndex, this.cursorIndex)) {
            this.soundHandler.onKey(TextFieldHandler.KeyType.CURSOR);
            this.soundHandler.setCursor(this.cursorIndex);
        }
    }
}

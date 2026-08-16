package dev.stashy.extrasounds.logics.impl;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.debug.DebugUtils;
import dev.stashy.extrasounds.sounds.SoundType;
import dev.stashy.extrasounds.sounds.Sounds;
import me.lonefelidae16.groominglib.api.PrefixableMessageFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Helper class for managing {@link net.minecraft.client.gui.components.EditBox} and its inherited class.
 */
public final class TextFieldHandler {
    public enum KeyType {
        ERASE,
        CUT,
        INSERT,
        PASTE,
        RETURN,
        CURSOR
    }

    private static final Logger LOGGER = LogManager.getLogger(
            TextFieldHandler.class,
            new PrefixableMessageFactory("%s/%s".formatted(ExtraSounds.class.getSimpleName(), TextFieldHandler.class.getSimpleName()))
    );

    /**
     * The start position in the text.
     */
    private int cursorStart = 0;
    /**
     * The end position in the text.
     */
    private int cursorEnd = 0;

    /**
     * Triggers the erase action.
     *
     * @param offset         -1 or +1
     * @param length         A text length.
     * @param selectionStart Current position of the start of the selection.
     * @param selectionEnd   Current position of the end of the selection.
     */
    public void onCharErase(int offset, int length, int selectionStart, int selectionEnd) {
        final boolean bHeadBackspace = offset < 0 && selectionStart <= 0;
        final boolean bTailDelete = offset > 0 && selectionEnd >= length;
        if ((bHeadBackspace || bTailDelete) && selectionStart == selectionEnd) {
            return;
        }
        this.onKey(KeyType.ERASE);
    }

    /**
     * Triggers the cursor move action.
     *
     * @param selectionStart Current position of the start of the selection.
     * @param selectionEnd   Current position of the end of the selection.
     */
    public void onCursorChanged(int selectionStart, int selectionEnd) {
        if (!isPosUpdated(selectionStart, selectionEnd)) {
            return;
        }
        this.onKey(KeyType.CURSOR);
        this.cursorStart = selectionStart;
        this.cursorEnd = selectionEnd;
    }

    /**
     * Checks if cursor position has moved.
     *
     * @param selectionStart Current position of the start of the selection.
     * @param selectionEnd   Current position of the end of the selection.
     * @return {@code true} if the movement of the cursor position is detected.
     */
    public boolean isPosUpdated(int selectionStart, int selectionEnd) {
        return this.cursorStart != selectionStart || this.cursorEnd != selectionEnd;
    }

    public void setCursor(int pos) {
        this.cursorStart = this.cursorEnd = pos;
    }

    public void setCursorStart(int cursorStart) {
        this.cursorStart = cursorStart;
    }

    public void setCursorEnd(int cursorEnd) {
        this.cursorEnd = cursorEnd;
    }

    public void onKey(KeyType type) {
        if (type == null) {
            ExtraSounds.LOGGER.error("Null argument of type '{}' was passed!",
                    KeyType.class.getSimpleName(),
                    new IllegalArgumentException("'type' must be non-null.")
            );
            return;
        }

        if (DebugUtils.DEBUG) {
            StackWalker.getInstance().walk(frames -> {
                frames.dropWhile(frame -> frame.getClassName().equals(TextFieldHandler.class.getCanonicalName())).findFirst()
                        .ifPresentOrElse(frame -> {
                            LOGGER.info("Caller class: {}, keyType: {}", frame.getClassName(), type.name());
                        }, () -> {
                            LOGGER.info("Caller class: <UNKNOWN>, keyType: {}", type.name());
                        });
                return null;
            });
        }

        switch (type) {
            case ERASE -> ExtraSounds.MANAGER.playSoundUI(Sounds.KEYBOARD_ERASE, SoundType.TYPING);
            case CUT -> ExtraSounds.MANAGER.playSoundUI(Sounds.KEYBOARD_CUT, SoundType.TYPING);
            case CURSOR, RETURN -> ExtraSounds.MANAGER.playSoundUI(Sounds.KEYBOARD_MOVE, SoundType.TYPING);
            case INSERT -> ExtraSounds.MANAGER.playSoundUI(Sounds.KEYBOARD_TYPE, SoundType.TYPING);
            case PASTE -> ExtraSounds.MANAGER.playSoundUI(Sounds.KEYBOARD_PASTE, SoundType.TYPING);
        }
    }
}

package dev.stashy.extrasounds.logics.impl;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.Mixers;
import dev.stashy.extrasounds.sounds.Sounds;

/**
 * Helper class for managing {@link net.minecraft.client.gui.screens.Screen} and its inherited class.
 */
public final class ScreenScrollHandler {
    /**
     * Latest scroll time in milliseconds.
     */
    private long lastScrollTime;
    /**
     * Current horizontal scroll position.
     */
    private int lastScrollPosX;
    /**
     * Current vertical scroll position.
     */
    private int lastScrollPosY;

    public ScreenScrollHandler() {
        this.resetScrollPos();
    }

    /**
     * Resets scroll states.
     */
    public void resetScrollPos() {
        this.lastScrollTime = 0;
        this.lastScrollPosX = 0;
        this.lastScrollPosY = 0;
    }

    /**
     * Triggers the screen scroll action.
     *
     * @param row Target screen Y offset.
     */
    public void onScroll(int row) {
        this.onScroll(this.lastScrollPosX, row);
    }

    public void onScroll(int x, int y) {
        final long now = System.currentTimeMillis();
        final long timeDiff = now - this.lastScrollTime;
        if (timeDiff > 20 && (this.lastScrollPosX != x || this.lastScrollPosY != y)) {
            ExtraSounds.MANAGER.playSound2D(
                    Sounds.SCREEN_SCROLL,
                    Mixers.SCREENS,
                    (1f - 0.1f + 0.1f * Math.min(1, 50f / timeDiff))
            );
            this.lastScrollTime = now;
            this.lastScrollPosX = x;
            this.lastScrollPosY = y;
        }
    }
}

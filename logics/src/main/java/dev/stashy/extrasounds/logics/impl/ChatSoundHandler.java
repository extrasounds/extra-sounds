package dev.stashy.extrasounds.logics.impl;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.runtime.VersionedText;
import dev.stashy.extrasounds.sounds.SoundType;
import dev.stashy.extrasounds.sounds.Sounds;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Objects;

public final class ChatSoundHandler {
    private int currentLines = 0;

    public void onMessage(PlayerEntity player, String text) {
        boolean containsPlName = false;
        boolean containsScreenshot = text.matches(VersionedText.INSTANCE.translatable("screenshot.success", ".+?").getString());
        try {
            containsPlName |= text.contains("@" + player.getName().getString());
            containsPlName |= text.contains("@" + Objects.requireNonNull(player.getDisplayName()).getString());
        } catch (Exception ignore) {
        }

        if (containsPlName && !ExtraSounds.MANAGER.isMuted(SoundType.CHAT_MENTION)) {
            ExtraSounds.MANAGER.playSound2D(Sounds.CHAT_MENTION, SoundType.CHAT_MENTION);
        } else if (!containsScreenshot || ExtraSounds.MANAGER.isMuted(SoundType.SCREENSHOT)) {
            ExtraSounds.MANAGER.playSound2D(Sounds.CHAT, SoundType.CHAT);
        }
    }

    public void onScroll(int line) {
        if (line != this.currentLines) {
            ExtraSounds.MANAGER.playSound2D(Sounds.SCREEN_SCROLL, SoundType.CHAT);
            this.currentLines = line;
        }
    }

    public void resetScroll() {
        this.currentLines = 0;
    }
}

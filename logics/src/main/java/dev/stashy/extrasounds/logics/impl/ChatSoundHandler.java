package dev.stashy.extrasounds.logics.impl;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.sounds.SoundType;
import dev.stashy.extrasounds.sounds.Sounds;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public final class ChatSoundHandler {
    private int currentLines = 0;

    public void onMessage(Player player, String text) {
        boolean containsPlName = false;
        boolean containsScreenshot = text.matches(Component.translatable("screenshot.success", ".+?").getString());
        try {
            containsPlName |= text.contains("@" + player.getName().getString());
            containsPlName |= text.contains("@" + Objects.requireNonNull(player.getDisplayName()).getString());
        } catch (Exception ignore) {
        }

        if (containsPlName && !ExtraSounds.MANAGER.isMuted(SoundType.CHAT_MENTION)) {
            ExtraSounds.MANAGER.playSoundUI(Sounds.CHAT_MENTION, SoundType.CHAT_MENTION);
        } else if (!containsScreenshot || ExtraSounds.MANAGER.isMuted(SoundType.SCREENSHOT)) {
            ExtraSounds.MANAGER.playSoundUI(Sounds.CHAT, SoundType.CHAT);
        }
    }

    public void onScroll(int line) {
        if (line != this.currentLines) {
            ExtraSounds.MANAGER.playSoundUI(Sounds.SCREEN_SCROLL, SoundType.CHAT);
            this.currentLines = line;
        }
    }

    public void resetScroll() {
        this.currentLines = 0;
    }
}

package dev.stashy.extrasounds.mc26_1.impl;

import dev.stashy.extrasounds.logics.impl.VersionedHotbarSoundHandler;
import net.minecraft.world.entity.player.Player;

public class HotbarSoundHandler extends VersionedHotbarSoundHandler {
    @Override
    public int getPlayerInventorySlot(Player player) {
        if (player == null) {
            return INVALID_HOTBAR_SLOT;
        }
        return player.getInventory().getSelectedSlot();
    }
}

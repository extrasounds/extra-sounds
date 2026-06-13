package dev.stashy.extrasounds.mc1_16_1.impl;

import dev.stashy.extrasounds.logics.impl.VersionedHotbarSoundHandler;
import net.minecraft.entity.player.PlayerEntity;

public class HotbarSoundHandler extends VersionedHotbarSoundHandler {
    @Override
    public int getPlayerInventorySlot(PlayerEntity player) {
        return player.inventory.selectedSlot;
    }
}

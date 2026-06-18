package dev.stashy.extrasounds.logics.impl;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.Mixers;
import dev.stashy.extrasounds.sounds.SoundType;
import dev.stashy.extrasounds.sounds.Sounds;
import me.lonefelidae16.groominglib.api.McVersionInterchange;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class VersionedHotbarSoundHandler {
    public static final int FORCE_HOTBAR_CHANGE = -1;
    public static final int INVALID_HOTBAR_SLOT = -9999;

    @Nullable
    private Item pickingItem;

    public abstract int getPlayerInventorySlot(Player player);

    public static VersionedHotbarSoundHandler newInstance() {
        try {
            Class<VersionedHotbarSoundHandler> clazz = McVersionInterchange.getCompatibleClass(ExtraSounds.BASE_PACKAGE, "impl.HotbarSoundHandler");
            return clazz.getConstructor().newInstance();
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Failed to initialize 'HotbarSoundHandler'", ex);
        }
        return null;
    }

    public void onSwap(ItemStack mainHand, ItemStack offHand) {
        if (!offHand.isEmpty()) {
            ExtraSounds.MANAGER.playSoundUI(offHand, SoundType.HOTBAR);
        } else if (!mainHand.isEmpty()) {
            ExtraSounds.MANAGER.playSoundUI(mainHand, SoundType.HOTBAR);
        }
    }

    public void onChange() {
        this.onChange(FORCE_HOTBAR_CHANGE);
    }

    public void onChange(int newSlot) {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        final int target = (newSlot == FORCE_HOTBAR_CHANGE) ? this.getPlayerInventorySlot(player) : newSlot;
        ExtraSounds.MANAGER.hotbar(target);
    }

    public void spectatorHotbar() {
        ExtraSounds.MANAGER.playSoundUI(Sounds.HOTBAR_SCROLL, SoundType.HOTBAR);
    }

    public void doItemPick(Item item) {
        this.storePickingItem(item);
        this.onItemPick();
    }

    public void storePickingItem(Item item) {
        if (item != Items.AIR) {
            this.pickingItem = item;
        } else {
            this.pickingItem = null;
        }
    }

    public void onItemPick() {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        final Optional<Item> pickingItem = this.popPickingItem();
        pickingItem.ifPresent(item -> {
            if (!player.getMainHandItem().is(item)) {
                ExtraSounds.MANAGER.playSoundUI(item.getDefaultInstance(), SoundType.HOTBAR);
            }
        });
    }

    public Optional<Item> popPickingItem() {
        final Item result = this.pickingItem;
        this.pickingItem = null;
        return Optional.ofNullable(result);
    }

    public void onThrow(ItemStack itemStack) {
        ExtraSounds.MANAGER.playThrow(itemStack, Mixers.HOTBAR);
    }
}

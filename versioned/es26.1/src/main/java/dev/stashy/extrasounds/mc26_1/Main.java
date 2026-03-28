package dev.stashy.extrasounds.mc26_1;

import dev.stashy.extrasounds.logics.VersionedMain;
import dev.stashy.extrasounds.logics.impl.state.InventoryClickState;
import dev.stashy.extrasounds.logics.runtime.VersionedSoundEventWrapper;
import dev.stashy.extrasounds.sounds.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.IdMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class Main extends VersionedMain {
    @Override
    public Identifier getItemId(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    @Override
    public VersionedSoundEventWrapper generateSoundEvent(Identifier id) {
        return VersionedSoundEventWrapper.newInstance(id);
    }

    @Override
    public IdMap<Item> getItemRegistry() {
        return BuiltInRegistries.ITEM;
    }

    @Override
    public boolean canItemsCombine(ItemStack stack1, ItemStack stack2) {
        return ItemStack.isSameItemSameComponents(stack1, stack2);
    }

    @Override
    public void playSound(SoundInstance instance) {
        final Minecraft client = Minecraft.getInstance();
        client.executeIfPossible(() -> client.getSoundManager().play(instance));
    }

    @Override
    public boolean shouldIgnoreItemSound(Item cursorItem, Item slotItem, InventoryClickState state) {
        if (cursorItem instanceof BundleItem) {
            if ((!state.isRMB && slotItem != Items.AIR) || (state.isRMB && slotItem == Items.AIR)) {
                return true;
            }
        }
        if (slotItem instanceof BundleItem) {
            if (state.isCreativeSlot()) {
                return false;
            }
            return (state.isRMB && cursorItem == Items.AIR) || (!state.isRMB && cursorItem != Items.AIR);
        }

        return false;
    }

    @Override
    public float getSoundVolume(SoundSource soundCategory) {
        return Minecraft.getInstance().options.getSoundSourceVolume(soundCategory);
    }

    @Override
    public void stopSound(VersionedSoundEventWrapper event, SoundType type) {
        Minecraft.getInstance().getSoundManager().stop(event.getId(), type.category);
    }
}

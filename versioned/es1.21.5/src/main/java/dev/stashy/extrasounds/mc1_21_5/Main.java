package dev.stashy.extrasounds.mc1_21_5;

import dev.stashy.extrasounds.logics.VersionedMain;
import dev.stashy.extrasounds.logics.impl.LockableSlotConnector;
import dev.stashy.extrasounds.logics.impl.state.InventoryClickState;
import dev.stashy.extrasounds.logics.runtime.VersionedSoundEventWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.IndexedIterable;

public final class Main extends VersionedMain {
    @Override
    public Identifier generateIdentifier(String namespace, String path) {
        return Identifier.of(namespace, path);
    }

    @Override
    public Identifier getItemId(Item item) {
        return Registries.ITEM.getId(item);
    }

    @Override
    public VersionedSoundEventWrapper generateSoundEvent(Identifier id) {
        return VersionedSoundEventWrapper.newInstance(id);
    }

    @Override
    public IndexedIterable<Item> getItemRegistry() {
        return Registries.ITEM;
    }

    @Override
    public boolean canItemsCombine(ItemStack stack1, ItemStack stack2) {
        return ItemStack.areItemsAndComponentsEqual(stack1, stack2);
    }

    @Override
    public void playSound(SoundInstance instance) {
        final MinecraftClient client = MinecraftClient.getInstance();
        client.executeSync(() -> client.getSoundManager().play(instance));
    }

    @Override
    public boolean shouldIgnoreItemSound(Item cursorItem, Item slotItem, InventoryClickState state) {
        if (cursorItem instanceof BundleItem) {
            if ((!state.isRMB && slotItem != Items.AIR) || (state.isRMB && slotItem == Items.AIR)) {
                return true;
            }
        }

        if (slotItem instanceof BundleItem) {
            if (state.slot instanceof LockableSlotConnector && ((LockableSlotConnector) state.slot).extrasounds$isCreativeSlot()) {
                return false;
            }
            return (state.isRMB && cursorItem == Items.AIR) || (!state.isRMB && cursorItem != Items.AIR);
        }

        return false;
    }

    @Override
    public float getSoundVolume(SoundCategory soundCategory) {
        return MinecraftClient.getInstance().options.getSoundVolume(soundCategory);
    }

    @Override
    public Identifier getItemIdWithComponents(ItemStack itemStack) {
        ComponentMap map = itemStack.getComponents();
        if (map.contains(DataComponentTypes.ITEM_MODEL)) {
            return map.get(DataComponentTypes.ITEM_MODEL);
        } else {
            return this.getItemId(itemStack.getItem());
        }
    }

    @Override
    public PlayerInventory getPlayerInventory(PlayerEntity player) {
        return player.getInventory();
    }
}

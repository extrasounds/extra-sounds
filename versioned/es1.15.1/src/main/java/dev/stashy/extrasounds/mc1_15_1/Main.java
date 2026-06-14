package dev.stashy.extrasounds.mc1_15_1;

import dev.stashy.extrasounds.logics.VersionedMain;
import dev.stashy.extrasounds.logics.impl.state.InventoryClickState;
import dev.stashy.extrasounds.logics.runtime.VersionedSoundEventWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class Main extends VersionedMain {
    @Override
    public Identifier generateIdentifier(String namespace, String path) {
        return new Identifier(namespace, path);
    }

    @Override
    public Identifier getItemId(Item item) {
        return Registry.ITEM.getId(item);
    }

    @Override
    public VersionedSoundEventWrapper generateSoundEvent(Identifier id) {
        return VersionedSoundEventWrapper.newInstance(id);
    }

    @Override
    public Iterable<Item> getItemRegistry() {
        return Registry.ITEM;
    }

    @Override
    public boolean canItemsCombine(ItemStack stack1, ItemStack stack2) {
        return ItemStack.areItemsEqual(stack1, stack2) && ItemStack.areTagsEqual(stack1, stack2);
    }

    @Override
    public void playSound(SoundInstance instance) {
        final MinecraftClient client = MinecraftClient.getInstance();
        client.send(() -> client.getSoundManager().play(instance));
    }

    @Override
    public boolean shouldIgnoreItemSound(Item cursorItem, Item slotItem, InventoryClickState state) {
        return false;
    }

    @Override
    public float getSoundVolume(SoundCategory soundCategory) {
        return MinecraftClient.getInstance().options.getSoundVolume(soundCategory);
    }

    @Override
    public PlayerInventory getPlayerInventory(PlayerEntity player) {
        return player.inventory;
    }
}

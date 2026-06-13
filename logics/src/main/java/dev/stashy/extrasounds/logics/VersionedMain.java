package dev.stashy.extrasounds.logics;

import dev.stashy.extrasounds.logics.impl.state.InventoryClickState;
import dev.stashy.extrasounds.logics.runtime.VersionedSoundEventWrapper;
import me.lonefelidae16.groominglib.api.McVersionInterchange;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.IndexedIterable;

public abstract class VersionedMain {
    public static VersionedMain newInstance() {
        try {
            Class<VersionedMain> clazz = McVersionInterchange.getCompatibleClass(ExtraSounds.BASE_PACKAGE, "Main");
            return clazz.getConstructor().newInstance();
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Cannot initialize 'Main'", ex);
        }
        return null;
    }

    public abstract Identifier generateIdentifier(String namespace, String path);

    public abstract Identifier getItemId(Item item);

    public abstract VersionedSoundEventWrapper generateSoundEvent(Identifier id);

    public abstract IndexedIterable<Item> getItemRegistry();

    public abstract boolean canItemsCombine(ItemStack stack1, ItemStack stack2);

    public abstract void playSound(SoundInstance instance);

    public abstract boolean shouldIgnoreItemSound(Item cursorItem, Item slotItem, InventoryClickState state);

    public abstract float getSoundVolume(SoundCategory soundCategory);

    public Identifier getItemIdWithComponents(ItemStack itemStack) {
        return this.getItemId(itemStack.getItem());
    }
}

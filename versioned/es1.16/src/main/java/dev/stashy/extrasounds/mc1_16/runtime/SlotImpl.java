package dev.stashy.extrasounds.mc1_16.runtime;

import dev.stashy.extrasounds.logics.runtime.VersionedSlotWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SlotImpl extends VersionedSlotWrapper {
    private final Slot slot;

    public SlotImpl(Object slot) {
        super(slot);
        this.slot = (Slot) slot;
    }

    @Override
    public ItemStack getStack() {
        return this.slot.getStack();
    }

    @Override
    public boolean canInsert(ItemStack cursorStack) {
        return this.slot.canInsert(cursorStack);
    }

    @Override
    public boolean hasStack() {
        return this.slot.hasStack();
    }
}

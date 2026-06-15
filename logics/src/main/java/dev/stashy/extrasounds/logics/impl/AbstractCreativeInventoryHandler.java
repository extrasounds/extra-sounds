package dev.stashy.extrasounds.logics.impl;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.impl.state.InventoryClickState;
import dev.stashy.extrasounds.logics.impl.state.InventoryTabType;
import dev.stashy.extrasounds.logics.impl.state.SlotActionTypeImpl;
import dev.stashy.extrasounds.logics.runtime.VersionedSlotWrapper;
import dev.stashy.extrasounds.sounds.SoundType;
import dev.stashy.extrasounds.sounds.Sounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public abstract class AbstractCreativeInventoryHandler {

    protected abstract InventoryTabType getTabType();

    protected abstract boolean isCreativeInventorySlot(VersionedSlotWrapper slot);

    protected abstract VersionedSlotWrapper getDeleteItemSlot();

    public void onClick(PlayerEntity player, VersionedSlotWrapper slot, int slotId, int button, SlotActionTypeImpl actionType, ItemStack cursor) {
        final boolean bOnHotbar = slot.getInstance() != null && !this.isCreativeInventorySlot(slot);
        final boolean bMatchDeleteSlot = slot.getInstance() != null && slot.getInstance() == this.getDeleteItemSlot().getInstance();
        final InventoryClickState state = new InventoryClickState(slot, slotId, cursor, actionType, button, this.getTabType());
        final boolean bOnCreativeTab = state.isOnCreativeTab();

        if (player == null) {
            return;
        }

        // <editor-fold desc="Exception Procedures on Creative Inventory Screen">

        if (actionType == SlotActionTypeImpl.THROW) {
            // When CreativeInventory is opened, can drop items from any slots while holding an item on cursor.
            final ItemStack slotStack = state.getSlotStack();
            if (button == 0) {
                slotStack.setCount(1);
            } else if (button == 1) {
                // With holding the Ctrl key; (slotActionType == THROW && button == 1)
                slotStack.setCount(slotStack.getMaxCount());
            }
            ExtraSounds.MANAGER.playThrow(slotStack);
            return;
        }

        if (actionType == SlotActionTypeImpl.QUICK_MOVE) {
            // With holding the Shift key; (slotActionType == QUICK_MOVE)
            if (bOnCreativeTab && bOnHotbar && slot.hasStack()) {
                // Quick move from Hotbar to Creative slots; stack will be deleted.
                ExtraSounds.MANAGER.playSound2D(Sounds.ITEM_DELETE_PARTIAL, SoundType.GRAB);
                return;
            }
            if (bMatchDeleteSlot) {
                // Shift + Click on deleteItemSlot; clearing Inventory.
                ExtraSounds.MANAGER.playSound2D(Sounds.ITEM_DELETE_ALL, SoundType.GRAB);
                return;
            }
        }

        if (!state.cursorStack.isEmpty()) {
            if (bMatchDeleteSlot) {
                // Clicked deleteItemSlot.
                ExtraSounds.MANAGER.playSound2D(Sounds.ITEM_DELETE_PARTIAL, SoundType.GRAB);
                return;
            }

            if (state.isEmptySpaceClicked() && !bOnCreativeTab) {
                // On Inventory tab, entire stack will be thrown regardless of mouse buttons.
                ExtraSounds.MANAGER.playThrow(state.cursorStack);
                return;
            }

            if (bOnCreativeTab && !bOnHotbar) {
                if (ExtraSounds.MAIN.canItemsCombine(state.getSlotStack(), state.cursorStack) && !state.isRMB) {
                    // Left Mouse Clicked on the same slot in CreativeInventory tab except Hotbar.
                    ExtraSounds.MANAGER.playSound2D(state.cursorStack, SoundType.GRAB);
                    return;
                } else if (slotId >= 0) {
                    // Clicking on another slot will delete or decrement the cursor stack.
                    ExtraSounds.MANAGER.playSound2D(Sounds.ITEM_DELETE_PARTIAL, SoundType.GRAB);
                    return;
                }
            }
        }

        // </editor-fold>

        ExtraSounds.MANAGER.handleInventorySlot(player, state);
    }
}

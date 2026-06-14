package dev.stashy.extrasounds.logics.impl.state;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.runtime.VersionedSlotWrapper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Helper class that handles click events on Inventory screens.
 */
public final class InventoryClickState {
    /**
     * Indicates clicked slot. If null, screen border or off-screen area was clicked.
     * Or {@code QuickCrafting} is finished - mouse button has been released.
     */
    private final VersionedSlotWrapper slot;
    /**
     * Indicates the index of slot. There are two ways it can be less than zero - {@code -1} shows screen border,
     * or {@link ScreenHandler#EMPTY_SPACE_SLOT_INDEX} shows off-screen area.
     */
    public final int slotIndex;
    /**
     * Stores the copy of the stack holding on the cursor.
     */
    public final ItemStack cursorStack;
    /**
     * Indicates the type of action to be taken on the slot.
     */
    public final SlotActionTypeImpl actionType;
    /**
     * Indicates mouse buttons, or slot index when {@link #actionType} is {@link SlotActionTypeImpl#SWAP}.
     * Includes {@code QuickCraftStage} while dragging.
     */
    public final int button;
    /**
     * If {@code true}, Right Mouse Button was clicked reliably.
     */
    public final boolean isRMB;
    /**
     * Stores the current {@link InventoryTabType}.
     */
    public final InventoryTabType tabType;

    public InventoryClickState(VersionedSlotWrapper slot, int slotIndex, ItemStack cursor, SlotActionTypeImpl actionType, int button, InventoryTabType inventoryTabType) {
        this.slot = slot;
        this.slotIndex = slotIndex;
        this.cursorStack = cursor.copy();
        this.actionType = actionType;
        this.button = button;
        this.tabType = inventoryTabType;
        this.isRMB = this.isRightClick();
    }

    public int getQuickCraftButton() {
        return unpackQuickCraftButton(this.button);
    }

    public boolean isQuickCrafting() {
        return this.actionType == SlotActionTypeImpl.QUICK_CRAFT && unpackQuickCraftStage(this.button) < 2;
    }

    @Nullable
    public Object getSlot() {
        return this.slot.getInstance();
    }

    private static int unpackQuickCraftButton(int button) {
        return button >> 2 & 3;
    }

    private static int unpackQuickCraftStage(int button) {
        return button & 3;
    }

    /**
     * Returns the copy of {@link ItemStack} in the slot.
     *
     * @return {@link ItemStack} in the slot. {@link ItemStack#EMPTY} if slot is null.
     * Both are passed through to the {@link ItemStack#copy()} method.
     */
    public ItemStack getSlotStack() {
        return (this.getSlot() == null) ? ItemStack.EMPTY.copy() : this.slot.getStack().copy();
    }

    /**
     * @return {@code true} if off-screen area was clicked.
     */
    public boolean isEmptySpaceClicked() {
        return this.slotIndex == -999 && this.actionType != SlotActionTypeImpl.QUICK_CRAFT;
    }

    private boolean isRightClick() {
        return (this.actionType != SlotActionTypeImpl.THROW && this.actionType != SlotActionTypeImpl.SWAP) && this.button == 1 ||
                this.actionType == SlotActionTypeImpl.QUICK_CRAFT && this.getQuickCraftButton() == 1;
    }

    /**
     * @return {@code true} if the cursor item cannot be inserted in this slot.
     */
    public boolean isSlotBlocked() {
        if (this.getSlot() == null || this.cursorStack.isEmpty()) {
            return false;
        }

        return !this.slot.canInsert(this.cursorStack) && !ExtraSounds.MAIN.canItemsCombine(this.slot.getStack(), this.cursorStack);
    }

    public boolean isOnCreativeTab() {
        return this.tabType == InventoryTabType.CREATIVE;
    }

    /**
     * Returns the copy of {@link ItemStack} on the cursor.
     * If {@link #actionType} is {@link SlotActionTypeImpl#SWAP}, returns slot stack instead of {@link #cursorStack}.
     *
     * @param player An instance of a {@link PlayerEntity} that owns inventory where this event triggered.
     * @return The copy of {@link ItemStack}.
     */
    public ItemStack getCursorStack(PlayerEntity player) {
        final ItemStack result;
        if (this.actionType == SlotActionTypeImpl.SWAP) {
            // Swap event.
            if (PlayerInventory.isValidHotbarIndex(this.button)) {
                // Pressed hotbar key.
                result = ExtraSounds.MAIN.getPlayerInventory(player).getInvStack(this.button).copy();
            } else {
                // Pressed offhand key.
                result = player.getOffHandStack().copy();
            }
        } else {
            result = this.cursorStack;
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("slot = %s, slotIndex = %d, cursorStack = %s, action = %s, button = %d",
                (this.slot == null) ? "null" : this.slot.getClass(),
                this.slotIndex,
                this.cursorStack.toString(),
                this.actionType,
                this.button
        );
    }
}

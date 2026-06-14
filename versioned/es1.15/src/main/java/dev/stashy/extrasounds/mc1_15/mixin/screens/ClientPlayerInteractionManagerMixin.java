package dev.stashy.extrasounds.mc1_15.mixin.screens;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.impl.state.InventoryClickState;
import dev.stashy.extrasounds.logics.impl.state.InventoryTabType;
import dev.stashy.extrasounds.logics.impl.state.SlotActionTypeImpl;
import dev.stashy.extrasounds.logics.runtime.VersionedSlotWrapper;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * For Inventory screen sounds.
 */
@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
    @Inject(method = "clickSlot", at = @At("HEAD"))
    private void extrasounds$inventoryClickEvent(int syncId, int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        if (player == null) {
            return;
        }
        Container container = player.container;
        if (container == null) {
            return;
        }

        Slot slot = (slotIndex >= 0) ? container.slotList.get(slotIndex) : null;
        SlotActionTypeImpl wrapped = SlotActionTypeImpl.Wrapper.INSTANCE.wrap(actionType);
        ExtraSounds.MANAGER.handleInventorySlot(player, new InventoryClickState(VersionedSlotWrapper.newInstance(slot), slotIndex, player.inventory.getCursorStack(), wrapped, button, InventoryTabType.SURVIVAL));
    }
}

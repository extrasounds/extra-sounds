package dev.stashy.extrasounds.mc1_16.mixin.screens;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.impl.state.InventoryClickState;
import dev.stashy.extrasounds.logics.impl.state.InventoryTabType;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
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
        ScreenHandler screenHandler = player.currentScreenHandler;
        if (screenHandler == null) {
            return;
        }

        Slot slot = (slotIndex >= 0) ? screenHandler.slots.get(slotIndex) : null;
        ExtraSounds.MANAGER.handleInventorySlot(player, new InventoryClickState(slot, slotIndex, player.inventory.getCursorStack(), actionType, button, InventoryTabType.SURVIVAL));
    }
}

package dev.stashy.extrasounds.logics.mixin.inventory;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.impl.state.InventoryClickState;
import dev.stashy.extrasounds.logics.impl.state.InventoryTabType;
import dev.stashy.extrasounds.logics.impl.state.SlotActionType;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * For Inventory screen sounds.
 */
@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
    @Inject(method = "handleContainerInput", at = @At("HEAD"))
    private void extrasounds$inventoryClickEvent(int syncId, int slotIndex, int button, ContainerInput input, Player player, CallbackInfo ci) {
        if (player == null) {
            return;
        }
        AbstractContainerMenu screenHandler = player.containerMenu;

        Slot slot = (slotIndex >= 0) ? screenHandler.slots.get(slotIndex) : null;
        SlotActionType actionType = switch (input) {
            case PICKUP -> SlotActionType.PICKUP;
            case QUICK_MOVE -> SlotActionType.QUICK_MOVE;
            case SWAP -> SlotActionType.SWAP;
            case THROW -> SlotActionType.THROW;
            case PICKUP_ALL -> SlotActionType.PICKUP_ALL;
            case CLONE -> SlotActionType.CLONE;
            case QUICK_CRAFT ->  SlotActionType.QUICK_CRAFT;
        };
        ExtraSounds.MANAGER.handleInventorySlot(player, new InventoryClickState(slot, slotIndex, screenHandler.getCarried(), actionType, button, InventoryTabType.SURVIVAL));
    }
}

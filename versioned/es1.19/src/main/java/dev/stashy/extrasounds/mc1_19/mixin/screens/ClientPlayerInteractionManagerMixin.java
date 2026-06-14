package dev.stashy.extrasounds.mc1_19.mixin.screens;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.impl.state.InventoryClickState;
import dev.stashy.extrasounds.logics.impl.state.InventoryTabType;
import dev.stashy.extrasounds.logics.impl.state.SlotActionTypeImpl;
import dev.stashy.extrasounds.logics.runtime.VersionedSlotWrapper;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * For Inventory screen sounds.
 */
@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
    @Inject(method = "clickSlot", at = @At("HEAD"))
    private void extrasounds$inventoryClickEvent(int syncId, int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (player == null) {
            return;
        }
        ScreenHandler screenHandler = player.currentScreenHandler;
        if (screenHandler == null) {
            return;
        }

        Slot slot = (slotIndex >= 0) ? screenHandler.slots.get(slotIndex) : null;
        SlotActionTypeImpl wrapped = SlotActionTypeImpl.Wrapper.INSTANCE.wrap(actionType);
        ExtraSounds.MANAGER.handleInventorySlot(player, new InventoryClickState(VersionedSlotWrapper.newInstance(slot), slotIndex, screenHandler.getCursorStack(), wrapped, button, InventoryTabType.SURVIVAL));
    }
}

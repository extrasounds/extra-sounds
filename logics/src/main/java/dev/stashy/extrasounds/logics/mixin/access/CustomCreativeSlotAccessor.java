package dev.stashy.extrasounds.logics.mixin.access;

import dev.stashy.extrasounds.logics.impl.access.CustomCreativeSlotConnector;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen$CustomCreativeSlot")
public abstract class CustomCreativeSlotAccessor implements CustomCreativeSlotConnector {
    @Override
    public boolean extrasounds$isCreativeSlot() {
        return true;
    }
}

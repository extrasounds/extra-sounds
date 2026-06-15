package dev.stashy.extrasounds.logics.mixin.access;

import dev.stashy.extrasounds.logics.impl.LockableSlotConnector;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net/minecraft/client/gui/screen/ingame/CreativeInventoryScreen$class_482")
public abstract class LockableSlotAccessor implements LockableSlotConnector {
    @Override
    public boolean extrasounds$isCreativeSlot() {
        return true;
    }
}

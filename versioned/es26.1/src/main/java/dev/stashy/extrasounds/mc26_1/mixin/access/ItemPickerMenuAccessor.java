package dev.stashy.extrasounds.mc26_1.mixin.access;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "net/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen$ItemPickerMenu")
public interface ItemPickerMenuAccessor {
    @Invoker("getRowIndexForScroll")
    int invokeGetRowIndexForScroll(float offset);
}

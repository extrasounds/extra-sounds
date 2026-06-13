package dev.stashy.extrasounds.logics.mixin.access;

import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AdvancementTab.class)
public interface AdvancementTabAccessor {
    @Accessor("icon")
    ItemStack getIcon();
}

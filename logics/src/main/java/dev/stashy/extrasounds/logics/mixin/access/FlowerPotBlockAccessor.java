package dev.stashy.extrasounds.logics.mixin.access;

import dev.stashy.extrasounds.logics.impl.FlowerPotBlockConnector;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FlowerPotBlock.class)
public abstract class FlowerPotBlockAccessor implements FlowerPotBlockConnector {
    @Shadow
    public abstract Block getContent();

    @Override
    public boolean isContentEmpty() {
        return this.getContent() == Blocks.AIR;
    }
}

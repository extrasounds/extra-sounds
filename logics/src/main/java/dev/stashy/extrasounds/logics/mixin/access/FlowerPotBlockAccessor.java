package dev.stashy.extrasounds.logics.mixin.access;

import net.minecraft.world.level.block.FlowerPotBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FlowerPotBlock.class)
public interface FlowerPotBlockAccessor {
    @Invoker("isEmpty")
    boolean invokeIsEmpty();
}

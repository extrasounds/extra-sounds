package dev.stashy.extrasounds.logics.mixin.access;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.MobBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobBucketItem.class)
public interface MobBucketItemAccessor {
    @Accessor("emptySound")
    SoundEvent extrasounds$access_getEmptySound();
}

package dev.stashy.extrasounds.logics.mixin.access;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.SolidBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SolidBucketItem.class)
public interface SolidBucketItemAccessor {
    @Accessor("placeSound")
    SoundEvent extrasounds$access_getPlaceSound();
}

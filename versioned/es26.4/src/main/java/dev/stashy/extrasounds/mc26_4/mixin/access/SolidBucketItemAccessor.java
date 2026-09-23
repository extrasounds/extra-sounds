package dev.stashy.extrasounds.mc26_4.mixin.access;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.SolidBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SolidBucketItem.class)
public interface SolidBucketItemAccessor {
    @Accessor("placeSound")
    Holder<SoundEvent> extrasounds$access_getPlaceSound();
}

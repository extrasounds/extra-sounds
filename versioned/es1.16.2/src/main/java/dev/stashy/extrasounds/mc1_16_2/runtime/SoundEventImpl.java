package dev.stashy.extrasounds.mc1_16_2.runtime;

import dev.stashy.extrasounds.logics.runtime.VersionedSoundEventWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundEventImpl extends VersionedSoundEventWrapper {
    private final SoundEvent instance;

    public SoundEventImpl(Identifier identifier) {
        this.instance = new SoundEvent(identifier);
    }

    public SoundEventImpl(BlockState blockState) {
        this.instance = blockState.getSoundGroup().getPlaceSound();
    }

    public SoundEventImpl(Object soundEvent) {
        this.instance = (SoundEvent) soundEvent;
    }

    @Override
    public Object getInstance() {
        return this.instance;
    }

    @Override
    public Identifier getId() {
        return this.instance.getId();
    }
}

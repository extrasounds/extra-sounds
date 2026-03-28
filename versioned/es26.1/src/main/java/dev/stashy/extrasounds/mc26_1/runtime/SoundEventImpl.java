package dev.stashy.extrasounds.mc26_1.runtime;

import dev.stashy.extrasounds.logics.runtime.VersionedSoundEventWrapper;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class SoundEventImpl extends VersionedSoundEventWrapper {
    private final SoundEvent instance;

    public SoundEventImpl(Identifier identifier) {
        this.instance = new SoundEvent(identifier, Optional.empty());
    }

    public SoundEventImpl(BlockState blockState) {
        this.instance = blockState.getSoundType().getPlaceSound();
    }

    @Override
    public Object getInstance() {
        return this.instance;
    }

    @Override
    public Identifier getId() {
        return this.instance.location();
    }
}

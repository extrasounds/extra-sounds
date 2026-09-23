package dev.stashy.extrasounds.mc26_4.runtime;

import dev.stashy.extrasounds.logics.runtime.VersionedSoundEventWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.sounds.BlockSoundSet;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SoundEventImpl extends VersionedSoundEventWrapper {
    private final @Nullable SoundEvent instance;

    public SoundEventImpl(Identifier identifier) {
        this.instance = new SoundEvent(identifier, Optional.empty());
    }

    public SoundEventImpl(BlockState blockState) {
        final LevelReader levelReader = Minecraft.getInstance().level;
        if (levelReader == null) {
            this.instance = null;
        } else {
            final BlockSoundSet soundSet = blockState.getSoundsAsHolder(levelReader).map(Holder::value).orElse(BlockSoundSet.EMPTY);
            this.instance = soundSet.placeSound().map(Holder::value).orElse(null);
        }
    }

    @Override
    public Object getInstance() {
        return this.instance;
    }

    @Override
    public Identifier getId() {
        if (this.instance == null) {
            return null;
        }
        return this.instance.location();
    }
}

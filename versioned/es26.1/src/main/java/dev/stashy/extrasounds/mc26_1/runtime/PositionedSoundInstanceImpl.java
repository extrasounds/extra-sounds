package dev.stashy.extrasounds.mc26_1.runtime;

import dev.stashy.extrasounds.logics.runtime.VersionedPositionedSoundInstanceWrapper;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class PositionedSoundInstanceImpl extends SimpleSoundInstance implements VersionedPositionedSoundInstanceWrapper {
    private static final RandomSource RANDOM = RandomSource.create();

    public PositionedSoundInstanceImpl(Identifier id, SoundSource category, float volume, float pitch, RandomSource random, boolean repeat, int repeatDelay, Attenuation attenuationType, double x, double y, double z, boolean relative) {
        super(id, category, volume, pitch, random, repeat, repeatDelay, attenuationType, x, y, z, relative);
    }

    public static PositionedSoundInstanceImpl init(Identifier id, SoundSource category, float volume, float pitch, boolean repeat, int repeatDelay, Attenuation attenuationType, double x, double y, double z, boolean relative) {
        return new PositionedSoundInstanceImpl(id, category, volume, pitch, RANDOM, repeat, repeatDelay, attenuationType, x, y, z, relative);
    }
}

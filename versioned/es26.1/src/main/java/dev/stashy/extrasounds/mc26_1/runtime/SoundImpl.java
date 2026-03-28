package dev.stashy.extrasounds.mc26_1.runtime;

import dev.stashy.extrasounds.logics.runtime.VersionedSoundWrapper;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.resources.Identifier;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.SampledFloat;

public class SoundImpl extends Sound implements VersionedSoundWrapper {
    public SoundImpl(Identifier id, SampledFloat volume, SampledFloat pitch, int weight, Type registrationType, boolean stream, boolean preload, int attenuation) {
        super(id, volume, pitch, weight, registrationType, stream, preload, attenuation);
    }

    public static SoundImpl init(Identifier id, float volume, float pitch, int weight, Type registrationType, boolean stream, boolean preload, int attenuation) {
        return new SoundImpl(id, ConstantFloat.of(volume), ConstantFloat.of(pitch), weight, registrationType, stream, preload, attenuation);
    }

    @Override
    public Identifier getIdentifierImpl() {
        return this.getLocation();
    }

    @Override
    public Object getVolumeImpl() {
        return this.getVolume();
    }

    @Override
    public Object getPitchImpl() {
        return this.getPitch();
    }

    @Override
    public int getWeightImpl() {
        return this.getWeight();
    }

    @Override
    public Type getRegistrationTypeImpl() {
        return this.getType();
    }

    @Override
    public boolean isStreamedImpl() {
        return this.shouldStream();
    }

    @Override
    public boolean isPreloadedImpl() {
        return this.shouldPreload();
    }

    @Override
    public int getAttenuationImpl() {
        return this.getAttenuationDistance();
    }
}

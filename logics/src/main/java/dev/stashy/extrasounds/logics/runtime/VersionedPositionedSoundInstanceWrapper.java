package dev.stashy.extrasounds.logics.runtime;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.Mixers;
import me.lonefelidae16.groominglib.api.McVersionInterchange;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;

import java.lang.reflect.Method;
import java.util.Objects;

public interface VersionedPositionedSoundInstanceWrapper extends SoundInstance {
    String METHOD_KEY_INIT = VersionedPositionedSoundInstanceWrapper.class.getCanonicalName() + "#init";

    static VersionedPositionedSoundInstanceWrapper createDummy(Identifier id) {
        return newInstance(id, Mixers.MASTER, 1, 1, false, 0, Attenuation.NONE, 0, 0, 0, true);
    }

    static VersionedPositionedSoundInstanceWrapper newInstance(Identifier id, SoundSource category, float volume, float pitch, boolean repeat, int repeatDelay, SoundInstance.Attenuation attenuationType, double x, double y, double z, boolean relative) {
        Method init = ExtraSounds.CACHED_METHOD_MAP.getOrDefault(METHOD_KEY_INIT, null);

        if (init == null) {
            try {
                Class<VersionedPositionedSoundInstanceWrapper> clazz = McVersionInterchange.getCompatibleClass(ExtraSounds.BASE_PACKAGE, "runtime.PositionedSoundInstanceImpl");
                init = clazz.getMethod("init", Identifier.class, SoundSource.class, float.class, float.class, boolean.class, int.class, SoundInstance.Attenuation.class, double.class, double.class, double.class, boolean.class);
                ExtraSounds.CACHED_METHOD_MAP.put(METHOD_KEY_INIT, Objects.requireNonNull(init));
            } catch (Exception ex) {
                ExtraSounds.LOGGER.error("Failed to find 'PositionedSoundInstance' class.", ex);
            }
        }

        try {
            return (VersionedPositionedSoundInstanceWrapper) Objects.requireNonNull(init).invoke(null, id, category, volume, pitch, repeat, repeatDelay, attenuationType, x, y, z, relative);
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Cannot instantiate 'PositionedSoundInstance'", ex);
        }

        return null;
    }
}

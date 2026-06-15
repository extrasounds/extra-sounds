package dev.stashy.extrasounds.mc1_21_2.runtime;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.runtime.VersionedBlockStateWrapper;
import dev.stashy.extrasounds.logics.runtime.VersionedSoundEventWrapper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.lang.reflect.Method;

public class SoundEventImpl extends VersionedSoundEventWrapper {
    private final SoundEvent instance;

    private static final Class<SoundEvent> SOUND_EVENT_CLASS = SoundEvent.class;
    private static final String METHOD_KEY_OF = SoundEvent.class.getCanonicalName() + "#of";
    private static final String METHOD_KEY_ID = SoundEvent.class.getCanonicalName() + "#id";

    public SoundEventImpl(Identifier identifier) {
        SoundEvent event = null;
        Method $of = ExtraSounds.CACHED_METHOD_MAP.getOrDefault(METHOD_KEY_OF, null);
        try {
            if ($of == null) {
                if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                    $of = SOUND_EVENT_CLASS.getMethod("of", Identifier.class);
                } else {
                    $of = SOUND_EVENT_CLASS.getMethod("method_47908", Identifier.class);
                }
                ExtraSounds.CACHED_METHOD_MAP.put(METHOD_KEY_OF, $of);
            }
            event = (SoundEvent) $of.invoke(SOUND_EVENT_CLASS, identifier);
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Cannot invoke SoundEvent#of(Identifier)", ex);
        }
        this.instance = event;
    }

    public SoundEventImpl(VersionedBlockStateWrapper blockState) {
        this.instance = blockState.getSoundGroupImpl().getPlaceSound();
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
        Method $id = ExtraSounds.CACHED_METHOD_MAP.getOrDefault(METHOD_KEY_ID, null);
        try {
            if ($id == null) {
                if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                    $id = SOUND_EVENT_CLASS.getMethod("id");
                } else {
                    $id = SOUND_EVENT_CLASS.getMethod("comp_3319");
                }
                ExtraSounds.CACHED_METHOD_MAP.put(METHOD_KEY_ID, $id);
            }
            return (Identifier) $id.invoke(this.instance);
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Cannot invoke SoundEvent#id", ex);
        }
        return null;
    }
}

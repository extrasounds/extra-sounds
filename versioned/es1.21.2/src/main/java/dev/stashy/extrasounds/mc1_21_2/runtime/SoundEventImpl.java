package dev.stashy.extrasounds.mc1_21_2.runtime;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.runtime.VersionedSoundEventWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.lang.reflect.Method;

public class SoundEventImpl extends VersionedSoundEventWrapper {
    private final SoundEvent instance;

    private static final Class<SoundEvent> SOUND_EVENT_CLASS = SoundEvent.class;

    public SoundEventImpl(Identifier identifier) {
        SoundEvent event = null;
        try {
            Method $of = SOUND_EVENT_CLASS.getMethod("of", Identifier.class);
            event = (SoundEvent) $of.invoke(SOUND_EVENT_CLASS, identifier);
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Cannot invoke SoundEvent#of(Identifier)", ex);
        }
        this.instance = event;
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
        try {
            Method $id = SOUND_EVENT_CLASS.getMethod("id");
            return (Identifier) $id.invoke(this.instance);
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Cannot invoke SoundEvent#id", ex);
        }
        return null;
    }
}

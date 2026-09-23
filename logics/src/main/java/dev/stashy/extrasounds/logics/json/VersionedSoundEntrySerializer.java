package dev.stashy.extrasounds.logics.json;

import com.google.gson.JsonSerializer;
import dev.stashy.extrasounds.logics.ExtraSounds;
import me.lonefelidae16.groominglib.api.McVersionInterchange;
import net.minecraft.client.resources.sounds.SoundEventRegistration;

public abstract class VersionedSoundEntrySerializer implements JsonSerializer<SoundEventRegistration> {
    public static VersionedSoundEntrySerializer newInstance() {
        try {
            Class<VersionedSoundEntrySerializer> clazz = McVersionInterchange.getCompatibleClass(ExtraSounds.BASE_PACKAGE, "json.SoundEntrySerializer");
            return clazz.getConstructor().newInstance();
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Cannot initialize 'SoundEntrySerializer'", ex);
        }
        return null;
    }
}

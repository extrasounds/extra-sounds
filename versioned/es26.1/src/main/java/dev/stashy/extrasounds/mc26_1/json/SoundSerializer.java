package dev.stashy.extrasounds.mc26_1.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.stashy.extrasounds.logics.json.VersionedSoundSerializer;
import dev.stashy.extrasounds.logics.runtime.VersionedSoundWrapper;
import net.minecraft.client.resources.sounds.Sound;

import java.lang.reflect.Type;

public class SoundSerializer extends VersionedSoundSerializer {
    @Override
    public JsonElement serialize(VersionedSoundWrapper src, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", src.getIdentifierImpl().toString());
        if (src.getVolumeImpl() instanceof Float value && value != 1) {
            obj.addProperty("volume", value);
        }
        if (src.getPitchImpl() instanceof Float value && value != 1) {
            obj.addProperty("pitch", value);
        }
        if (src.getWeightImpl() != 1) {
            obj.addProperty("weight", src.getWeightImpl());
        }
        if (src.getRegistrationTypeImpl() != Sound.Type.FILE) {
            obj.addProperty("type", "event");
        }
        if (src.isStreamedImpl()) {
            obj.addProperty("stream", src.isStreamedImpl());
        }
        if (src.isPreloadedImpl()) {
            obj.addProperty("preload", src.isPreloadedImpl());
        }
        if (src.getAttenuationImpl() != 16) {
            obj.addProperty("attenuation_distance", src.getAttenuationImpl());
        }
        return obj;
    }
}

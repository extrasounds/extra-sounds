package dev.stashy.extrasounds.mc26_4.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.stashy.extrasounds.logics.json.VersionedSoundEntrySerializer;
import net.minecraft.client.resources.sounds.SoundEventRegistration;

import java.lang.reflect.Type;

public class SoundEntrySerializer extends VersionedSoundEntrySerializer {
    @Override
    public JsonElement serialize(SoundEventRegistration src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject element = new JsonObject();
        JsonArray sounds = new JsonArray();
        src.getSounds().stream().filter(sound -> sound.getLocation() != null).forEach(sound -> {
            sounds.add(context.serialize(sound));
        });
        element.add("sounds", sounds);
        if (src.isReplace()) {
            element.addProperty("replace", src.isReplace());
        }
        if (src.getSubtitle() != null) {
            element.addProperty("subtitle", src.getSubtitle().getString());
        }
        return element;
    }
}

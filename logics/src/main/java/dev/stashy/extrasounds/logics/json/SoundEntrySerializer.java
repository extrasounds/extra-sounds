package dev.stashy.extrasounds.logics.json;

import com.google.gson.*;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundEventRegistration;

import java.lang.reflect.Type;
import java.util.Objects;

public class SoundEntrySerializer implements JsonSerializer<SoundEventRegistration> {
    @Override
    public JsonElement serialize(SoundEventRegistration src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject element = new JsonObject();
        JsonArray sounds = new JsonArray();
        for (Sound snd : src.getSounds()) {
            sounds.add(context.serialize(snd));
        }
        element.add("sounds", sounds);
        if (src.isReplace()) {
            element.addProperty("replace", src.isReplace());
        }
        if (!Objects.equals(src.getSubtitle(), "")) {
            element.addProperty("subtitle", src.getSubtitle());
        }
        return element;
    }
}

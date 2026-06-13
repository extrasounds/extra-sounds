package dev.stashy.extrasounds.mc1_19.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.stashy.extrasounds.logics.json.VersionedSoundSerializer;
import dev.stashy.extrasounds.logics.runtime.VersionedSoundWrapper;
import net.minecraft.client.sound.Sound;
import net.minecraft.util.math.floatprovider.FloatSupplier;
import net.minecraft.util.math.random.Random;

import java.lang.reflect.Type;

public class SoundSerializer extends VersionedSoundSerializer {
    private static final Random MC_RANDOM = Random.create();

    @Override
    public JsonElement serialize(VersionedSoundWrapper src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", src.getIdentifierImpl().toString());
        if (src.getVolumeImpl() instanceof FloatSupplier) {
            final float volume = ((FloatSupplier) src.getVolumeImpl()).get(MC_RANDOM);
            if (volume != 1) {
                obj.addProperty("volume", volume);
            }
        }
        if (src.getPitchImpl() instanceof FloatSupplier) {
            final float pitch = ((FloatSupplier) src.getPitchImpl()).get(MC_RANDOM);
            if (pitch != 1) {
                obj.addProperty("pitch", pitch);
            }
        }
        if (src.getWeightImpl() != 1) {
            obj.addProperty("weight", src.getWeightImpl());
        }
        if (src.getRegistrationTypeImpl() != Sound.RegistrationType.FILE) {
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

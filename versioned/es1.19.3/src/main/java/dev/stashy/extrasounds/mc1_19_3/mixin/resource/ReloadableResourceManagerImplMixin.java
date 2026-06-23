package dev.stashy.extrasounds.mc1_19_3.mixin.resource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.entry.SoundPackLoader;
import dev.stashy.extrasounds.logics.runtime.VersionedClientResource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Mixin(ReloadableResourceManagerImpl.class)
public abstract class ReloadableResourceManagerImplMixin {
    @Shadow
    private @Final ResourceType type;

    @ModifyVariable(method = "reload", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/LifecycledResourceManager;close()V", shift = At.Shift.AFTER), ordinal = 0)
    private List<ResourcePack> extrasounds$registerResPack(List<ResourcePack> arg3) {
        if (this.type != ResourceType.CLIENT_RESOURCES) {
            return arg3;
        }

        ExtraSounds.LOGGER.info("Registering Runtime ResPack");
        List<ResourcePack> modifiable = new LinkedList<>(arg3);
        modifiable.add(0, (ResourcePack) SoundPackLoader.EXTRA_SOUNDS_RESOURCE);
        return modifiable;
    }

    @Inject(method = "reload", at = @At("RETURN"))
    private void extrasounds$afterReload(CallbackInfoReturnable<ResourceReload> cir) {
        final Set<String> soundEvents = new HashSet<>();
        for (Resource pack : MinecraftClient.getInstance().getResourceManager().getAllResources(SoundPackLoader.SOUNDS_JSON_ID)) {
            if (pack.getResourcePackName().equals(VersionedClientResource.PACK_NAME) || pack.getResourcePackName().equals(ExtraSounds.MOD_NAME)) {
                // Avoid auto-gen resource via SoundPackLoader.
                continue;
            }
            try (InputStream stream = pack.getInputStream()) {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(stream)));
                final JsonObject jsonObject = JsonParser.parseString(reader.lines().collect(Collectors.joining())).getAsJsonObject();
                soundEvents.addAll(jsonObject.keySet());
            } catch (Exception ignored) {
            }
        }

        SoundPackLoader.registerExternalSoundEvent(soundEvents);
    }
}

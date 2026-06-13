package dev.stashy.extrasounds.mc1_16.mixin.resource;

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

    @ModifyVariable(method = "beginMonitoredReload", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManagerImpl;clear()V", shift = At.Shift.AFTER), ordinal = 0)
    private List<ResourcePack> extrasounds$registerResPack(List<ResourcePack> arg3) {
        if (this.type != ResourceType.CLIENT_RESOURCES) {
            return arg3;
        }

        ExtraSounds.LOGGER.info("registering Runtime ResPack");
        List<ResourcePack> modifiable = new LinkedList<>(arg3);
        modifiable.add(0, (ResourcePack) SoundPackLoader.EXTRA_SOUNDS_RESOURCE);
        return modifiable;
    }

    @Inject(method = "beginMonitoredReload", at = @At("RETURN"))
    private void extrasounds$afterReload(CallbackInfoReturnable<ResourceReloadMonitor> cir) {
        try {
            final Set<String> soundEvents = new HashSet<>();
            for (Resource pack : MinecraftClient.getInstance().getResourceManager().getAllResources(SoundPackLoader.SOUNDS_JSON_ID)) {
                if (pack.getResourcePackName().equals(VersionedClientResource.PACK_NAME) || pack.getResourcePackName().equals(ExtraSounds.MOD_NAME)) {
                    // Avoid auto-gen resource via SoundPackLoader.
                    continue;
                }
                try (InputStream stream = pack.getInputStream()) {
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(stream)));
                    final JsonObject jsonObject = new JsonParser().parse(reader.lines().collect(Collectors.joining())).getAsJsonObject();
                    soundEvents.addAll(jsonObject.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toSet()));
                } catch (Exception ignored) {
                }
            }

            SoundPackLoader.registerExternalSoundEvent(soundEvents);
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("failed to load external sounds", ex);
        }
    }
}

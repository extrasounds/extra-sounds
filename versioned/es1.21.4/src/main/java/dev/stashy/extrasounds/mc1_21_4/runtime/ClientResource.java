package dev.stashy.extrasounds.mc1_21_4.runtime;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.stashy.extrasounds.logics.runtime.VersionedClientResource;
import net.minecraft.registry.VersionedIdentifier;
import net.minecraft.resource.*;
import net.minecraft.resource.metadata.ResourceMetadataSerializer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class ClientResource extends VersionedClientResource implements ResourcePack {
    private static final Class<ResourceMetadataSerializer> RESOURCE_METADATA_SERIALIZER_CLASS = ResourceMetadataSerializer.class;

    private final ResourcePackInfo info;

    public ClientResource(String modId, String packName) {
        super(modId, packName);
        this.name = packName;
        this.info = new ResourcePackInfo(modId, Text.literal(packName), new ResourcePackSource() {
            @Override
            public Text decorate(Text packDisplayName) {
                return packDisplayName;
            }

            @Override
            public boolean canBeEnabledLater() {
                return false;
            }
        }, Optional.of(new VersionedIdentifier(Identifier.DEFAULT_NAMESPACE, modId, String.valueOf(this.packVersion))));
    }

    @Override
    public InputSupplier<InputStream> openRoot(String... segments) {
        return null;
    }

    @Override
    protected Supplier<InputStream> openRootImpl(String... segments) {
        try {
            InputStream stream = Objects.requireNonNull(this.openRoot(segments)).get();
            return () -> Objects.requireNonNull(stream);
        } catch (Exception ignored) {
        }
        return null;
    }


    @Override
    public InputSupplier<InputStream> open(ResourceType type, Identifier id) {
        if (type != ResourceType.CLIENT_RESOURCES) {
            return null;
        }

        try {
            final Supplier<byte[]> supplier = Objects.requireNonNull(this.assets.get(id));
            return () -> new ByteArrayInputStream(Objects.requireNonNull(supplier.get()));
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void findResources(ResourceType type, String namespace, String prefix, ResultConsumer consumer) {
        if (type != ResourceType.CLIENT_RESOURCES) {
            return;
        }

        for (Identifier id : this.assets.keySet()) {
            Supplier<byte[]> supplier = this.assets.get(id);
            if (supplier == null) {
                continue;
            }
            InputSupplier<InputStream> inputSupplier = () -> new ByteArrayInputStream(supplier.get());
            if (id.getNamespace().equals(namespace) && id.getPath().startsWith(prefix)) {
                consumer.accept(id, inputSupplier);
            }
        }
    }

    @Override
    public Set<String> getNamespaces(ResourceType type) {
        return super.getNamespacesImpl(type);
    }

    @Override
    public @Nullable <T> T parseMetadata(ResourceMetadataSerializer<T> metaReader) throws IOException {
        try {
            InputStream stream = Objects.requireNonNull(this.openRootImpl("pack.mcmeta")).get();
            return AbstractFileResourcePack.parseMetadata(metaReader, Objects.requireNonNull(stream));
        } catch (Exception ignored) {
            try {
                Method $name = RESOURCE_METADATA_SERIALIZER_CLASS.getMethod("name");
                Method $codec = RESOURCE_METADATA_SERIALIZER_CLASS.getMethod("codec");
                if ($name.invoke(metaReader).equals("pack")) {
                    final JsonObject object = super.createPackJson();
                    return ((Codec<T>) $codec.invoke(metaReader)).parse(JsonOps.INSTANCE, object).result().orElse(null);
                }
            } catch (Exception ignore) {
            }
            return null;
        }
    }

    @Override
    public void close() {
        super.closeImpl();
    }

    @Override
    public ResourcePackInfo getInfo() {
        return this.info;
    }
}

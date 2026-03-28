package dev.stashy.extrasounds.mc26_1.runtime;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.runtime.VersionedClientResource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.ResourceMetadata;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class ClientResource extends VersionedClientResource implements PackResources {
    private final PackLocationInfo info;

    public ClientResource(String modId, String packName) {
        super(modId, packName);
        this.name = packName;
        this.info = new PackLocationInfo(modId, Component.literal(packName), new PackSource() {
            @Override
            public Component decorate(Component packDisplayName) {
                return packDisplayName;
            }

            @Override
            public boolean shouldAddAutomatically() {
                return false;
            }
        }, Optional.of(new KnownPack(Identifier.DEFAULT_NAMESPACE, modId, String.valueOf(this.packVersion))));
    }

    @Override
    public IoSupplier<InputStream> getRootResource(String... segments) {
        return null;
    }

    @Override
    protected Supplier<InputStream> openRootImpl(String... segments) {
        try {
            var stream = Objects.requireNonNull(this.getRootResource(segments)).get();
            return () -> Objects.requireNonNull(stream);
        } catch (Exception ignored) {
        }
        return null;
    }


    @Override
    public IoSupplier<InputStream> getResource(PackType type, Identifier id) {
        if (type != PackType.CLIENT_RESOURCES) {
            return null;
        }

        try {
            final var supplier = Objects.requireNonNull(this.assets.get(id));
            return () -> new ByteArrayInputStream(Objects.requireNonNull(supplier.get()));
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void listResources(PackType type, String namespace, String prefix, ResourceOutput consumer) {
        if (type != PackType.CLIENT_RESOURCES) {
            return;
        }

        for (var id : this.assets.keySet()) {
            var supplier = this.assets.get(id);
            if (supplier == null) {
                continue;
            }
            IoSupplier<InputStream> inputSupplier = () -> new ByteArrayInputStream(supplier.get());
            if (id.getNamespace().equals(namespace) && id.getPath().startsWith(prefix)) {
                consumer.accept(Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath()), inputSupplier);
            }
        }
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return super.getNamespacesImpl(type);
    }

    @Override
    public @Nullable <T> T getMetadataSection(MetadataSectionType<T> metaReader) throws IOException {
        try {
            var stream = Objects.requireNonNull(this.openRootImpl("pack.mcmeta")).get();
            ResourceMetadata resourceMetadata = ResourceMetadata.fromJsonStream(stream);
            Optional<T> section = resourceMetadata.getSection(metaReader);
            return section.orElseThrow();
        } catch (Exception ignored) {
            if (metaReader.name().equals("pack")) {
                final JsonObject object = super.createPackJson();
                return metaReader.codec().parse(JsonOps.INSTANCE, object).ifError(tError -> ExtraSounds.LOGGER.error("Cannot register Runtime ResPack: {}", tError)).result().orElse(null);
            } else {
                return null;
            }
        }
    }

    @Override
    public void close() {
        super.closeImpl();
    }

    @Override
    public PackLocationInfo location() {
        return this.info;
    }
}

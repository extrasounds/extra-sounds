package dev.stashy.extrasounds.mc26_3.entry;

import dev.stashy.extrasounds.logics.entry.BaseVanillaGenerator;
import dev.stashy.extrasounds.mapping.SoundDefinition;
import dev.stashy.extrasounds.mapping.SoundGenerator;
import dev.stashy.extrasounds.sounds.Categories;
import dev.stashy.extrasounds.sounds.Sounds;
import net.minecraft.world.item.CushionItem;

public final class VanillaGenerator extends BaseVanillaGenerator {
    @Override
    protected SoundGenerator generate() {
        return SoundGenerator.of(item -> {
            if (item instanceof CushionItem) {
                return SoundDefinition.of(Sounds.aliased(Categories.CUSHION));
            }
            return super.generalSounds(item);
        });
    }
}

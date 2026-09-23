package dev.stashy.extrasounds.mc26_3.entry;

import dev.stashy.extrasounds.logics.entry.BaseVanillaGenerator;
import dev.stashy.extrasounds.mapping.SoundDefinition;
import dev.stashy.extrasounds.mapping.SoundGenerator;
import dev.stashy.extrasounds.mc26_3.mixin.access.SolidBucketItemAccessor;
import dev.stashy.extrasounds.sounds.Categories;
import dev.stashy.extrasounds.sounds.Sounds;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CushionItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;

import static dev.stashy.extrasounds.sounds.Sounds.event;

public final class VanillaGenerator extends BaseVanillaGenerator {
    @Override
    protected SoundGenerator generate() {
        return SoundGenerator.of(item -> {
            if (item instanceof BlockItem blockItem) {
                final Block block = blockItem.getBlock();
                if (block instanceof RotatedPillarBlock pillarBlock && pillarBlock.defaultBlockState().getSoundType().equals(SoundType.FROGLIGHT)) {
                    final Identifier blockSoundId = block.defaultBlockState().getSoundType().getPlaceSound().location();
                    return SoundDefinition.of(event(blockSoundId, 0.75f));
                } else if (blockItem instanceof SolidBucketItemAccessor bucketItem) {
                    return SoundDefinition.of(event(bucketItem.extrasounds$access_getPlaceSound().location(), 1.3f));
                }
                return this.generateFromBlock(block);
            } else if (item instanceof CushionItem) {
                return SoundDefinition.of(Sounds.aliased(Categories.CUSHION));
            }
            return super.generalSounds(item);
        });
    }
}

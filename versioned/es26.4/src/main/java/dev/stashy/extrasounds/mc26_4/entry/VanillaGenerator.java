package dev.stashy.extrasounds.mc26_4.entry;

import dev.stashy.extrasounds.logics.SoundManager;
import dev.stashy.extrasounds.logics.entry.BaseVanillaGenerator;
import dev.stashy.extrasounds.mapping.SoundDefinition;
import dev.stashy.extrasounds.mapping.SoundGenerator;
import dev.stashy.extrasounds.mc26_4.mixin.access.SolidBucketItemAccessor;
import dev.stashy.extrasounds.sounds.Categories;
import dev.stashy.extrasounds.sounds.Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CushionItem;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.sounds.BlockSoundSet;
import net.minecraft.world.level.block.state.BlockState;

import static dev.stashy.extrasounds.sounds.Sounds.event;

public final class VanillaGenerator extends BaseVanillaGenerator {
    @Override
    protected SoundGenerator generate() {
        return SoundGenerator.of(item -> {
            final LevelReader levelReader = Minecraft.getInstance().level;
            if (levelReader == null) {
                return DEFAULT_SOUND;
            }

            if (item instanceof BlockItem blockItem) {
                final Block block = blockItem.getBlock();
                if (block instanceof RotatedPillarBlock pillarBlock && isFroglightBlock(pillarBlock)) {
                    final BlockSoundSet soundSet = block.defaultBlockState().getSoundsAsHolder(levelReader).map(Holder::value).orElse(BlockSoundSet.EMPTY);
                    final Identifier blockSoundId = soundSet.placeSound().map(holder -> holder.value().location()).orElse(SoundManager.FALLBACK_SOUND_EVENT.getId());
                    return SoundDefinition.of(event(blockSoundId, 0.75f));
                } else if (blockItem instanceof SolidBucketItemAccessor bucketItem) {
                    return SoundDefinition.of(event(bucketItem.extrasounds$access_getPlaceSound().value().location(), 1.3f));
                }
                return this.generateFromBlock(block);
            } else if (item instanceof CushionItem) {
                return SoundDefinition.of(Sounds.aliased(Categories.CUSHION));
            }
            return super.generalSounds(item);
        });
    }

    private static boolean isFroglightBlock(RotatedPillarBlock pillarBlock) {
        final BlockState state = pillarBlock.defaultBlockState();
        return state.is(Blocks.OCHRE_FROGLIGHT) ||
                state.is(Blocks.PEARLESCENT_FROGLIGHT) ||
                state.is(Blocks.VERDANT_FROGLIGHT);
    }
}

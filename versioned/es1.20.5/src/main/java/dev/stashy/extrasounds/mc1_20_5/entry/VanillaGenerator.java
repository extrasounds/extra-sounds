package dev.stashy.extrasounds.mc1_20_5.entry;

import dev.stashy.extrasounds.logics.entry.BaseVanillaGenerator;
import dev.stashy.extrasounds.mapping.SoundDefinition;
import dev.stashy.extrasounds.mapping.SoundGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.PillarBlock;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import static dev.stashy.extrasounds.sounds.Categories.*;
import static dev.stashy.extrasounds.sounds.Sounds.*;

public final class VanillaGenerator extends BaseVanillaGenerator {
    @Override
    protected SoundGenerator generate() {
        return SoundGenerator.of(item -> {
            if (item instanceof BlockItem) {
                final Block block = ((BlockItem) item).getBlock();
                final Identifier blockSoundId = block.getDefaultState().getSoundGroup().getPlaceSound().getId();
                if (block instanceof PillarBlock) {
                    final PillarBlock pillarBlock = (PillarBlock) block;
                    if (pillarBlock.getDefaultState().getSoundGroup().equals(BlockSoundGroup.FROGLIGHT)) {
                        return SoundDefinition.of(event(blockSoundId, 0.6f));
                    }
                }
                return this.generateFromBlock(block);
            } else if (item instanceof ToolItem) {
                final ToolMaterial mats = ((ToolItem) item).getMaterial();
                if (mats instanceof ToolMaterials) {
                    return this.generateFromToolMaterial((ToolMaterials) mats);
                }
                return SoundDefinition.of(aliased(Gear.GENERIC));
            } else if (item instanceof ArmorItem) {
                return this.generateFromArmorMaterial(((ArmorItem) item).getMaterial().value());
            } else if (this.isPotionItem(item)) {
                return SoundDefinition.of(aliased(POTION));
            } else if (item instanceof GoatHornItem) {
                return SoundDefinition.of(single(LOOSE_METAL.getId(), 0.8f, 0.9f, Sound.RegistrationType.SOUND_EVENT));
            } else if (item instanceof SmithingTemplateItem) {
                return SoundDefinition.of(aliased(LOOSE_METAL));
            } else if (item instanceof DiscFragmentItem) {
                return SoundDefinition.of(single(METAL_BITS.getId(), 0.9f, 0.85f, Sound.RegistrationType.SOUND_EVENT));
            } else if (item instanceof BucketItem) {
                final SoundEntry soundEntry = ((BucketItem) item).fluid.getBucketFillSound().map(sound -> event(sound.getId(), 0.7f)).orElse(aliased(METAL));
                return SoundDefinition.of(soundEntry);
            } else if (item instanceof SpyglassItem) {
                return SoundDefinition.of(aliased(Gear.IRON));
            } else if (item instanceof BundleItem) {
                return SoundDefinition.of(aliased(BUNDLES));
            }

            return super.generalSounds(item);
        });
    }

    private boolean isPotionItem(Item item) {
        return item instanceof PotionItem || item instanceof ExperienceBottleItem || item instanceof OminousBottleItem;
    }

    private SoundDefinition generateFromToolMaterial(ToolMaterials mats) {
        switch (mats) {
            case WOOD:
                return SoundDefinition.of(aliased(Gear.WOOD));
            case STONE:
                return SoundDefinition.of(aliased(Gear.STONE));
            case IRON:
                return SoundDefinition.of(aliased(Gear.IRON));
            case DIAMOND:
                return SoundDefinition.of(aliased(Gear.DIAMOND));
            case GOLD:
                return SoundDefinition.of(aliased(Gear.GOLDEN));
            case NETHERITE:
                return SoundDefinition.of(aliased(Gear.NETHERITE));
            default:
                return SoundDefinition.of(aliased(Gear.GENERIC));
        }
    }

    private SoundDefinition generateFromArmorMaterial(ArmorMaterial mat) {
        if (mat == null) {
            return SoundDefinition.of(aliased(Gear.GENERIC));
        } else if (mat == ArmorMaterials.IRON.value()) {
            return SoundDefinition.of(aliased(Gear.IRON));
        } else if (mat == ArmorMaterials.GOLD.value()) {
            return SoundDefinition.of(aliased(Gear.GOLDEN));
        } else if (mat == ArmorMaterials.DIAMOND.value()) {
            return SoundDefinition.of(aliased(Gear.DIAMOND));
        } else if (mat == ArmorMaterials.NETHERITE.value()) {
            return SoundDefinition.of(aliased(Gear.NETHERITE));
        } else if (mat == ArmorMaterials.CHAIN.value()) {
            return SoundDefinition.of(aliased(Gear.CHAIN));
        } else if (mat == ArmorMaterials.TURTLE.value()) {
            return SoundDefinition.of(aliased(Gear.TURTLE));
        } else if (mat == ArmorMaterials.LEATHER.value()) {
            return SoundDefinition.of(aliased(Gear.LEATHER));
        } else if (mat == ArmorMaterials.ARMADILLO.value()) {
            return SoundDefinition.of(aliased(Gear.ARMADILLO));
        } else {
            return SoundDefinition.of(aliased(Gear.GENERIC));
        }
    }
}

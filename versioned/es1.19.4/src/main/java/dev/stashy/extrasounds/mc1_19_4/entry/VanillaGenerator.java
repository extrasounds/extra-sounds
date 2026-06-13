package dev.stashy.extrasounds.mc1_19_4.entry;

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
                final ArmorMaterial mats = ((ArmorItem) item).getMaterial();
                if (mats instanceof ArmorMaterials) {
                    return this.generateFromArmorMaterial((ArmorMaterials) mats);
                }
                return SoundDefinition.of(aliased(Gear.GENERIC));
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
        return item instanceof PotionItem || item instanceof ExperienceBottleItem;
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

    private SoundDefinition generateFromArmorMaterial(ArmorMaterials mats) {
        switch (mats) {
            case IRON:
                return SoundDefinition.of(aliased(Gear.IRON));
            case GOLD:
                return SoundDefinition.of(aliased(Gear.GOLDEN));
            case DIAMOND:
                return SoundDefinition.of(aliased(Gear.DIAMOND));
            case NETHERITE:
                return SoundDefinition.of(aliased(Gear.NETHERITE));
            case CHAIN:
                return SoundDefinition.of(aliased(Gear.CHAIN));
            case TURTLE:
                return SoundDefinition.of(aliased(Gear.TURTLE));
            case LEATHER:
                return SoundDefinition.of(aliased(Gear.LEATHER));
            default:
                return SoundDefinition.of(aliased(Gear.GENERIC));
        }
    }
}

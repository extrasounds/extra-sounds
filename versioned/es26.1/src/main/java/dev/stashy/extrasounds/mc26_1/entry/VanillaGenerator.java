package dev.stashy.extrasounds.mc26_1.entry;

import dev.stashy.extrasounds.logics.entry.BaseVanillaGenerator;
import dev.stashy.extrasounds.mapping.SoundDefinition;
import dev.stashy.extrasounds.mapping.SoundGenerator;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;

import static dev.stashy.extrasounds.sounds.Categories.*;
import static dev.stashy.extrasounds.sounds.Sounds.*;

public final class VanillaGenerator extends BaseVanillaGenerator {
    @Override
    protected SoundGenerator generate() {
        return SoundGenerator.of(item -> {
            if (item instanceof BlockItem blockItem) {
                final Block block = blockItem.getBlock();
                final Identifier blockSoundId = block.defaultBlockState().getSoundType().getPlaceSound().location();
                if (block instanceof RotatedPillarBlock pillarBlock && pillarBlock.defaultBlockState().getSoundType().equals(SoundType.FROGLIGHT)) {
                    return SoundDefinition.of(event(blockSoundId, 0.6f));
                }
                return this.generateFromBlock(block);
            } else if (this.hasMaterial(item)) {
                return this.generateWithMatString(getItemIdPath(item));
            } else if (this.isPotionItem(item)) {
                return SoundDefinition.of(aliased(POTION));
            } else if (item instanceof InstrumentItem) {
                return SoundDefinition.of(single(LOOSE_METAL.getId(), 0.6f, 0.9f, Sound.Type.SOUND_EVENT));
            } else if (item instanceof SmithingTemplateItem) {
                return SoundDefinition.of(aliased(LOOSE_METAL));
            } else if (item instanceof DiscFragmentItem) {
                return SoundDefinition.of(single(METAL_BITS.getId(), 0.7f, 0.85f, Sound.Type.SOUND_EVENT));
            } else if (item instanceof BucketItem bucketItem) {
                final SoundEventRegistration soundEntry = bucketItem.getContent().getPickupSound().map(sound -> event(sound.location(), 0.4f)).orElse(aliased(METAL));
                return SoundDefinition.of(soundEntry);
            } else if (item instanceof SpyglassItem) {
                return SoundDefinition.of(aliased(Gear.COPPER));
            }

            return super.generalSounds(item);
        });
    }

    private boolean isPotionItem(Item item) {
        return item instanceof PotionItem || item instanceof ExperienceBottleItem || item == Items.OMINOUS_BOTTLE;
    }

    private boolean hasMaterial(Item item) {
        final String path = this.getItemIdPath(item);
        return path.endsWith("_sword") || path.endsWith("_pickaxe") || path.endsWith("_spear") ||
                path.endsWith("_helmet") || path.endsWith("_chestplate") || path.endsWith("_leggings") || path.endsWith("_boots") ||
                path.endsWith("_horse_armor") || path.endsWith("_nautilus_armor") || path.equals("wolf_armor") ||
                item instanceof AxeItem || item instanceof HoeItem || item instanceof ShovelItem;
    }

    private SoundDefinition generateWithMatString(String str) {
        if (str.contains("wooden_")) {
            return SoundDefinition.of(aliased(Gear.WOOD));
        } else if (str.contains("stone_")) {
            return SoundDefinition.of(aliased(Gear.STONE));
        } else if (str.contains("leather_")) {
            return SoundDefinition.of(aliased(Gear.LEATHER));
        } else if (str.contains("copper_")) {
            return SoundDefinition.of(aliased(Gear.COPPER));
        } else if (str.contains("iron_")) {
            return SoundDefinition.of(aliased(Gear.IRON));
        } else if (str.contains("chainmail_")) {
            return SoundDefinition.of(aliased(Gear.CHAIN));
        } else if (str.contains("golden_")) {
            return SoundDefinition.of(aliased(Gear.GOLDEN));
        } else if (str.contains("diamond_")) {
            return SoundDefinition.of(aliased(Gear.DIAMOND));
        } else if (str.contains("netherite_")) {
            return SoundDefinition.of(aliased(Gear.NETHERITE));
        } else if (str.contains("turtle_")) {
            return SoundDefinition.of(aliased(Gear.TURTLE));
        } else if (str.contains("wolf_")) {
            return SoundDefinition.of(aliased(Gear.ARMADILLO));
        }
        return SoundDefinition.of(aliased(Gear.GENERIC));
    }

    private SoundDefinition generateFromRepairable(Repairable component) {
        if (component == null) {
            return SoundDefinition.of(aliased(Gear.GENERIC));
        }

        final var optionalTagKey = component.items().unwrapKey();
        if (optionalTagKey.isEmpty()) {
            return SoundDefinition.of(aliased(Gear.GENERIC));
        }

        final var matTags = optionalTagKey.get();
        if (matTags == ItemTags.WOODEN_TOOL_MATERIALS) {
            return SoundDefinition.of(aliased(Gear.WOOD));
        } else if (matTags == ItemTags.STONE_TOOL_MATERIALS) {
            return SoundDefinition.of(aliased(Gear.STONE));
        } else if (matTags == ItemTags.COPPER_TOOL_MATERIALS || matTags == ItemTags.REPAIRS_COPPER_ARMOR) {
            return SoundDefinition.of(aliased(Gear.COPPER));
        } else if (matTags == ItemTags.IRON_TOOL_MATERIALS || matTags == ItemTags.REPAIRS_IRON_ARMOR) {
            return SoundDefinition.of(aliased(Gear.IRON));
        } else if (matTags == ItemTags.GOLD_TOOL_MATERIALS || matTags == ItemTags.REPAIRS_GOLD_ARMOR) {
            return SoundDefinition.of(aliased(Gear.GOLDEN));
        } else if (matTags == ItemTags.DIAMOND_TOOL_MATERIALS || matTags == ItemTags.REPAIRS_DIAMOND_ARMOR) {
            return SoundDefinition.of(aliased(Gear.DIAMOND));
        } else if (matTags == ItemTags.NETHERITE_TOOL_MATERIALS || matTags == ItemTags.REPAIRS_NETHERITE_ARMOR) {
            return SoundDefinition.of(aliased(Gear.NETHERITE));
        } else if (matTags == ItemTags.REPAIRS_LEATHER_ARMOR) {
            return SoundDefinition.of(aliased(Gear.LEATHER));
        } else if (matTags == ItemTags.REPAIRS_CHAIN_ARMOR) {
            return SoundDefinition.of(aliased(Gear.CHAIN));
        } else if (matTags == ItemTags.REPAIRS_TURTLE_HELMET) {
            return SoundDefinition.of(aliased(Gear.TURTLE));
        } else if (matTags == ItemTags.REPAIRS_WOLF_ARMOR) {
            return SoundDefinition.of(aliased(Gear.ARMADILLO));
        } else {
            return SoundDefinition.of(aliased(Gear.GENERIC));
            //⬆ even though not required, this is in case any mods add to the repairable materials
        }
    }
}

package dev.stashy.extrasounds.logics.entry;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.SoundManager;
import dev.stashy.extrasounds.logics.mixin.access.MobBucketItemAccessor;
import dev.stashy.extrasounds.logics.mixin.access.SolidBucketItemAccessor;
import dev.stashy.extrasounds.logics.runtime.VersionedSoundEventWrapper;
import dev.stashy.extrasounds.mapping.SoundDefinition;
import dev.stashy.extrasounds.mapping.SoundGenerator;
import me.lonefelidae16.groominglib.api.McVersionInterchange;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.Optional;

import static dev.stashy.extrasounds.sounds.Categories.*;
import static dev.stashy.extrasounds.sounds.Sounds.*;
import static dev.stashy.extrasounds.sounds.Sounds.single;

public abstract class BaseVanillaGenerator {
    private static final SoundDefinition DEFAULT_SOUND = SoundDefinition.of(aliased(SoundManager.FALLBACK_SOUND_EVENT));
    public static final SoundGenerator GENERATOR;

    static {
        BaseVanillaGenerator instance = null;
        try {
            Class<BaseVanillaGenerator> clazz = McVersionInterchange.getCompatibleClass(ExtraSounds.BASE_PACKAGE, "entry.VanillaGenerator");
            instance = clazz.getConstructor().newInstance();
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Cannot initialize 'VanillaGenerator'", ex);
        }
        GENERATOR = Objects.requireNonNull(instance).generate();
    }

    protected abstract SoundGenerator generate();

    protected String getItemIdPath(Item item) {
        return ExtraSounds.MAIN.getItemId(item).getPath();
    }

    private boolean isBrickItem(Item item) {
        final String idPath = this.getItemIdPath(item);
        return item == Items.BRICK || idPath.endsWith("_pottery_sherd");
    }

    private boolean isGearGoldenItem(Item item) {
        return item instanceof CompassItem || item instanceof ShearsItem;
    }

    private boolean isGearLeatherItem(Item item) {
        return item instanceof LeadItem || this.getItemIdPath(item).equals("elytra") ||
                this.getItemIdPath(item).equals("saddle") ||
                this.getItemIdPath(item).endsWith("_harness");
    }

    private boolean isGearGenericItem(Item item) {
        return item instanceof BowItem || item instanceof CrossbowItem || item instanceof FishingRodItem ||
                item instanceof FoodOnAStickItem<?>;
    }

    private boolean isPaperItem(Item item) {
        return getItemIdPath(item).endsWith("banner_pattern") || item instanceof WritableBookItem ||
                item instanceof WrittenBookItem || item instanceof EmptyMapItem ||
                item instanceof MapItem || item instanceof NameTagItem || item instanceof KnowledgeBookItem ||
                item == Items.BOOK || item == Items.ENCHANTED_BOOK;
    }

    private boolean isStewItem(Item item) {
        return item == Items.SUSPICIOUS_STEW || item == Items.RABBIT_STEW ||
                item == Items.BEETROOT_SOUP || item == Items.MUSHROOM_STEW;
    }

    private boolean isPotionItem(Item item) {
        return item instanceof PotionItem || item instanceof ExperienceBottleItem || item == Items.OMINOUS_BOTTLE;
    }

    private boolean hasMaterial(Item item) {
        final String path = this.getItemIdPath(item);
        return path.endsWith("_sword") || path.endsWith("_pickaxe") || path.endsWith("_spear") ||
                path.endsWith("_helmet") || path.endsWith("_chestplate") || path.endsWith("_leggings") || path.endsWith("_boots") ||
                path.endsWith("_horse_armor") || path.endsWith("_nautilus_armor") || path.equals("wolf_armor") ||
                path.endsWith("_axe") || path.endsWith("_hoe") || path.endsWith("_shovel");
    }

    protected SoundDefinition generateFromBlock(Block block) {
        final BlockState blockState = block.defaultBlockState();
        final Identifier blockSoundId = Objects.requireNonNull(VersionedSoundEventWrapper.fromBlockState(blockState)).getId();

        if (block instanceof BaseRailBlock) {
            return SoundDefinition.of(aliased(RAIL));
        } else if (block instanceof BannerBlock) {
            return SoundDefinition.of(aliased(BANNER));
        } else if (block instanceof SeaPickleBlock) {
            return SoundDefinition.of(event(blockSoundId, 0.7f));
        } else if (block instanceof LeavesBlock || block instanceof VegetationBlock || block instanceof SugarCaneBlock) {
            if (blockSoundId.getPath().equals("block.grass.place")) {
                return SoundDefinition.of(aliased(LEAVES));
            } else {
                return SoundDefinition.of(event(blockSoundId, 1.3f));
            }
        }

        return SoundDefinition.of(event(blockSoundId, 1.3f));
    }

    protected SoundDefinition generateWithMatString(String str) {
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

    protected SoundDefinition generalSounds(Item item) {
        if (item instanceof BlockItem blockItem) {
            final Block block = blockItem.getBlock();
            final Identifier blockSoundId = block.defaultBlockState().getSoundType().getPlaceSound().location();
            if (block instanceof RotatedPillarBlock pillarBlock && pillarBlock.defaultBlockState().getSoundType().equals(SoundType.FROGLIGHT)) {
                return SoundDefinition.of(event(blockSoundId, 0.75f));
            } else if (blockItem instanceof SolidBucketItemAccessor bucketItem) {
                return SoundDefinition.of(event(bucketItem.extrasounds$access_getPlaceSound().location(), 1.3f));
            }
            return this.generateFromBlock(block);
        } else if (item instanceof BoatItem) {
            return SoundDefinition.of(aliased(BOAT));
        } else if (item instanceof MinecartItem) {
            return SoundDefinition.of(aliased(MINECART));
        } else if (item instanceof ItemFrameItem) {
            return SoundDefinition.of(aliased(FRAME));
        } else if (item instanceof ArrowItem) {
            return SoundDefinition.of(aliased(ARROW));
        } else if (item instanceof DyeItem) {
            return SoundDefinition.of(aliased(DUST));
        } else if (item instanceof SpawnEggItem) {
            return SoundDefinition.of(aliased(WET_SLIPPERY));
        } else if (this.getItemIdPath(item).startsWith("music_disc_")) {
            return SoundDefinition.of(aliased(MUSIC_DISC));
        } else if (this.isBrickItem(item)) {
            return SoundDefinition.of(aliased(BRICK));
        } else if (this.isGearGoldenItem(item)) {
            return SoundDefinition.of(aliased(Gear.GOLDEN));
        } else if (this.isGearLeatherItem(item)) {
            return SoundDefinition.of(aliased(Gear.LEATHER));
        } else if (this.isGearGenericItem(item)) {
            return SoundDefinition.of(aliased(Gear.GENERIC));
        } else if (this.isPaperItem(item)) {
            return SoundDefinition.of(aliased(PAPER));
        } else if (this.isStewItem(item)) {
            return SoundDefinition.of(aliased(BOWL));
        } else if (item instanceof BundleItem) {
            return SoundDefinition.of(aliased(BUNDLES));
        } else if (item instanceof EggItem) {
            return SoundDefinition.of(aliased(EGG));
        } else if (item instanceof final BucketItem bucketItem) {
            final SoundEventRegistration soundEntry = bucketItem.getContent().getPickupSound()
                    .map(sound -> event(sound.location()))
                    .or(() -> {
                        if (bucketItem instanceof MobBucketItemAccessor accessor) {
                            return Optional.of(event(accessor.extrasounds$access_getEmptySound().location(), 0.5f));
                        } else {
                            return Optional.empty();
                        }
                    })
                    .orElse(aliased(METAL));
            return SoundDefinition.of(soundEntry);
        } else if (this.isPotionItem(item)) {
            return SoundDefinition.of(aliased(POTION));
        } else if (item instanceof InstrumentItem) {
            return SoundDefinition.of(single(LOOSE_METAL.getId(), 0.6f, 0.9f, Sound.Type.SOUND_EVENT));
        } else if (item instanceof SmithingTemplateItem) {
            return SoundDefinition.of(aliased(LOOSE_METAL));
        } else if (item instanceof DiscFragmentItem) {
            return SoundDefinition.of(single(METAL_BITS.getId(), 0.7f, 0.85f, Sound.Type.SOUND_EVENT));
        } else if (item instanceof SpyglassItem) {
            return SoundDefinition.of(aliased(Gear.COPPER));
        } else if (this.hasMaterial(item)) {
            return generateWithMatString(getItemIdPath(item));
        }

        return DEFAULT_SOUND;
    }
}

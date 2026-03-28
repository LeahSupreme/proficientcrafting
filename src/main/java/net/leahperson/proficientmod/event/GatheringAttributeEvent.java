package net.leahperson.proficientmod.event;

import net.leahperson.proficientmod.Config;
import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.attribute.ModAttributes;
import net.leahperson.proficientmod.util.QualityDataUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(modid = ProficientMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GatheringAttributeEvent {

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof Player player)) return;

        List<ItemEntity> drops = event.getDrops().stream().toList();
        if (drops.isEmpty()) return;

        int quality = (int) player.getAttributeValue(ModAttributes.MOB_DROP_QUALITY.get());
        int yield = (int) player.getAttributeValue(ModAttributes.MOB_DROP_YIELD.get());

        ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType());
        int extraQuality = entityId != null ? Config.qualityThresholds.getOrDefault(entityId.toString(), 0) : 0;

        applyQuality(drops, quality - extraQuality, player.level().getRandom());
        applyYield(drops, yield, player.level().getRandom());
    }

    public static void applyQuality(List<ItemEntity> drops, int quality, RandomSource random) {
        if (quality <= 0) return;
        int rarity = computeRarity(quality, random);
        if (rarity <= 0) return;
        for (ItemEntity itemEntity : drops) {
            QualityDataUtil.setRarity(itemEntity.getItem(), rarity);
        }
    }

    public static void applyYield(List<ItemEntity> drops, int yield, RandomSource random) {
        int bonus = computeBonus(yield, random);
        if (bonus <= 0) return;
        for (ItemEntity itemEntity : drops) {
            ItemStack stack = itemEntity.getItem();
            stack.setCount(stack.getCount() + bonus);
        }
    }

    public static int computeRarity(int quality, RandomSource random) {
        int threshold = Config.qualityPerRarity;
        int guaranteed = quality / threshold;
        float chance = (quality % threshold) / (float) threshold;
        int rarity = guaranteed;
        if (chance > 0 && random.nextFloat() < chance) {
            rarity++;
        }
        return Math.min(rarity, 3);
    }

    public static int computeBonus(int yield, RandomSource random) {
        int perLevel = Config.yieldPerFortune;
        int full = yield / perLevel;
        float frac = (yield % perLevel) / (float) perLevel;
        return full + (frac > 0 && random.nextFloat() < frac ? 1 : 0);
    }
}

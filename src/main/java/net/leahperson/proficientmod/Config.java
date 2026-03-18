package net.leahperson.proficientmod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;


@Mod.EventBusSubscriber(modid = ProficientMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue QUALITY_PER_RARITY_CONFIG = BUILDER
            .comment("Amount of gathering Quality needed to guarantee one rarity tier on drops. Fractional amounts give a percentage chance.")
            .defineInRange("qualityPerRarity", 100, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue YIELD_PER_FORTUNE_CONFIG = BUILDER
            .comment("Amount of gathering Yield equivalent to one level of Fortune/Looting. Fractional amounts give a percentage chance of an extra drop.")
            .defineInRange("yieldPerFortune", 20, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue GOLDEN_SWORD_MOB_DROP_QUALITY_CONFIG = BUILDER
            .comment("Mob drop quality granted to the player while holding a golden sword.")
            .defineInRange("goldenSwordMobDropQuality", 50, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue GOLDEN_PICKAXE_MINING_QUALITY_CONFIG = BUILDER
            .comment("Mining quality granted to the player while holding a golden pickaxe.")
            .defineInRange("goldenPickaxeMiningQuality", 50, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue GOLDEN_PICKAXE_MINING_YIELD_CONFIG = BUILDER
            .comment("Mining yield granted to the player while holding a golden pickaxe.")
            .defineInRange("goldenPickaxeMiningYield", 10, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue GOLDEN_HOE_FARMING_QUALITY_CONFIG = BUILDER
            .comment("Farming quality granted to the player while holding a golden hoe.")
            .defineInRange("goldenHoeFarmingQuality", 50, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue GOLDEN_HOE_FARMING_YIELD_CONFIG = BUILDER
            .comment("Farming yield granted to the player while holding a golden hoe.")
            .defineInRange("goldenHoeFarmingYield", 10, 0, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int qualityPerRarity = 100;
    public static int yieldPerFortune = 20;
    public static int goldenSwordMobDropQuality = 50;
    public static int goldenPickaxeMiningQuality = 50;
    public static int goldenPickaxeMiningYield = 10;
    public static int goldenHoeFarmingQuality = 50;
    public static int goldenHoeFarmingYield = 10;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        qualityPerRarity = QUALITY_PER_RARITY_CONFIG.get();
        yieldPerFortune = YIELD_PER_FORTUNE_CONFIG.get();
        goldenSwordMobDropQuality = GOLDEN_SWORD_MOB_DROP_QUALITY_CONFIG.get();
        goldenPickaxeMiningQuality = GOLDEN_PICKAXE_MINING_QUALITY_CONFIG.get();
        goldenPickaxeMiningYield = GOLDEN_PICKAXE_MINING_YIELD_CONFIG.get();
        goldenHoeFarmingQuality = GOLDEN_HOE_FARMING_QUALITY_CONFIG.get();
        goldenHoeFarmingYield = GOLDEN_HOE_FARMING_YIELD_CONFIG.get();
    }
}

package net.leahperson.proficientmod.effects;

import net.leahperson.proficientmod.ProficientMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ProficientMod.MOD_ID);

    public static final RegistryObject<MobEffect> QUALITY =
            EFFECTS.register("quality", CraftingQualityEffect::new);

    public static final RegistryObject<MobEffect> PROFICIENCY =
            EFFECTS.register("proficiency", CraftingProficiencyEffect::new);

    public static final RegistryObject<MobEffect> YIELD =
            EFFECTS.register("yield", CraftingYieldEffect::new);

    public static final RegistryObject<MobEffect> MOB_DROP_QUALITY =
            EFFECTS.register("mob_drop_quality", MobDropQualityEffect::new);

    public static final RegistryObject<MobEffect> MOB_DROP_YIELD =
            EFFECTS.register("mob_drop_yield", MobDropYieldEffect::new);

    public static final RegistryObject<MobEffect> FARMING_QUALITY =
            EFFECTS.register("farming_quality", FarmingQualityEffect::new);

    public static final RegistryObject<MobEffect> FARMING_YIELD =
            EFFECTS.register("farming_yield", FarmingYieldEffect::new);

    public static final RegistryObject<MobEffect> MINING_QUALITY =
            EFFECTS.register("mining_quality", MiningQualityEffect::new);

    public static final RegistryObject<MobEffect> MINING_YIELD =
            EFFECTS.register("mining_yield", MiningYieldEffect::new);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}

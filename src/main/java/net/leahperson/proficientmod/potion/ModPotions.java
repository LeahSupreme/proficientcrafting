package net.leahperson.proficientmod.potion;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.effects.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, ProficientMod.MOD_ID);

    private static final int BASE_DURATION = 3600;
    private static final int LONG_DURATION = 9600;
    private static final int STRONG_DURATION = 1800;

    private static RegistryObject<Potion> register(String registryName, RegistryObject<?> effect, int duration, int amplifier) {
        String translationKey = ProficientMod.MOD_ID + "." + registryName;
        return POTIONS.register(registryName, () ->
                new Potion(translationKey, new MobEffectInstance(
                        (net.minecraft.world.effect.MobEffect) effect.get(), duration, amplifier)));
    }

    public static final RegistryObject<Potion> QUALITY = register("quality", ModEffects.QUALITY, BASE_DURATION, 0);
    public static final RegistryObject<Potion> LONG_QUALITY = register("long_quality", ModEffects.QUALITY, LONG_DURATION, 0);
    public static final RegistryObject<Potion> STRONG_QUALITY = register("strong_quality", ModEffects.QUALITY, STRONG_DURATION, 1);

    public static final RegistryObject<Potion> PROFICIENCY = register("proficiency", ModEffects.PROFICIENCY, BASE_DURATION, 0);
    public static final RegistryObject<Potion> LONG_PROFICIENCY = register("long_proficiency", ModEffects.PROFICIENCY, LONG_DURATION, 0);
    public static final RegistryObject<Potion> STRONG_PROFICIENCY = register("strong_proficiency", ModEffects.PROFICIENCY, STRONG_DURATION, 1);

    public static final RegistryObject<Potion> YIELD = register("yield", ModEffects.YIELD, BASE_DURATION, 0);
    public static final RegistryObject<Potion> LONG_YIELD = register("long_yield", ModEffects.YIELD, LONG_DURATION, 0);
    public static final RegistryObject<Potion> STRONG_YIELD = register("strong_yield", ModEffects.YIELD, STRONG_DURATION, 1);

    public static final RegistryObject<Potion> MOB_DROP_QUALITY = register("mob_drop_quality", ModEffects.MOB_DROP_QUALITY, BASE_DURATION, 0);
    public static final RegistryObject<Potion> LONG_MOB_DROP_QUALITY = register("long_mob_drop_quality", ModEffects.MOB_DROP_QUALITY, LONG_DURATION, 0);
    public static final RegistryObject<Potion> STRONG_MOB_DROP_QUALITY = register("strong_mob_drop_quality", ModEffects.MOB_DROP_QUALITY, STRONG_DURATION, 1);

    public static final RegistryObject<Potion> MOB_DROP_YIELD = register("mob_drop_yield", ModEffects.MOB_DROP_YIELD, BASE_DURATION, 0);
    public static final RegistryObject<Potion> LONG_MOB_DROP_YIELD = register("long_mob_drop_yield", ModEffects.MOB_DROP_YIELD, LONG_DURATION, 0);
    public static final RegistryObject<Potion> STRONG_MOB_DROP_YIELD = register("strong_mob_drop_yield", ModEffects.MOB_DROP_YIELD, STRONG_DURATION, 1);

    public static final RegistryObject<Potion> FARMING_QUALITY = register("farming_quality", ModEffects.FARMING_QUALITY, BASE_DURATION, 0);
    public static final RegistryObject<Potion> LONG_FARMING_QUALITY = register("long_farming_quality", ModEffects.FARMING_QUALITY, LONG_DURATION, 0);
    public static final RegistryObject<Potion> STRONG_FARMING_QUALITY = register("strong_farming_quality", ModEffects.FARMING_QUALITY, STRONG_DURATION, 1);

    public static final RegistryObject<Potion> FARMING_YIELD = register("farming_yield", ModEffects.FARMING_YIELD, BASE_DURATION, 0);
    public static final RegistryObject<Potion> LONG_FARMING_YIELD = register("long_farming_yield", ModEffects.FARMING_YIELD, LONG_DURATION, 0);
    public static final RegistryObject<Potion> STRONG_FARMING_YIELD = register("strong_farming_yield", ModEffects.FARMING_YIELD, STRONG_DURATION, 1);

    public static final RegistryObject<Potion> MINING_QUALITY = register("mining_quality", ModEffects.MINING_QUALITY, BASE_DURATION, 0);
    public static final RegistryObject<Potion> LONG_MINING_QUALITY = register("long_mining_quality", ModEffects.MINING_QUALITY, LONG_DURATION, 0);
    public static final RegistryObject<Potion> STRONG_MINING_QUALITY = register("strong_mining_quality", ModEffects.MINING_QUALITY, STRONG_DURATION, 1);

    public static final RegistryObject<Potion> MINING_YIELD = register("mining_yield", ModEffects.MINING_YIELD, BASE_DURATION, 0);
    public static final RegistryObject<Potion> LONG_MINING_YIELD = register("long_mining_yield", ModEffects.MINING_YIELD, LONG_DURATION, 0);
    public static final RegistryObject<Potion> STRONG_MINING_YIELD = register("strong_mining_yield", ModEffects.MINING_YIELD, STRONG_DURATION, 1);

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}

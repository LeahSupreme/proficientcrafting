package net.leahperson.proficientmod.attribute;

import net.leahperson.proficientmod.ProficientMod;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ProficientMod.MOD_ID);



    public static final RegistryObject<Attribute> PROFICIENCY = ATTRIBUTES.register("proficiency",
            () -> new RangedAttribute("qualitycrafting:proficiency", 0, 0, 1000000000));

    public static final RegistryObject<Attribute> QUALITY = ATTRIBUTES.register("quality",
            () -> new RangedAttribute("qualitycrafting:quality", 0, 0, 1000000000));

    public static final RegistryObject<Attribute> YIELD = ATTRIBUTES.register("yield",
            () -> new RangedAttribute("qualitycrafting:yield", 0, 0, 1000000000));

    public static final RegistryObject<Attribute> FARMING_QUALITY = ATTRIBUTES.register("farming_quality",
            () -> new RangedAttribute("qualitycrafting:farming_quality", 0, 0, 1000000000));

    public static final RegistryObject<Attribute> FARMING_YIELD = ATTRIBUTES.register("farming_yield",
            () -> new RangedAttribute("qualitycrafting:farming_yield", 0, 0, 1000000000));

    public static final RegistryObject<Attribute> MINING_QUALITY = ATTRIBUTES.register("mining_quality",
            () -> new RangedAttribute("qualitycrafting:mining_quality", 0, 0, 1000000000));

    public static final RegistryObject<Attribute> MINING_YIELD = ATTRIBUTES.register("mining_yield",
            () -> new RangedAttribute("qualitycrafting:mining_yield", 0, 0, 1000000000));

    public static final RegistryObject<Attribute> MOB_DROP_QUALITY = ATTRIBUTES.register("mob_drop_quality",
            () -> new RangedAttribute("qualitycrafting:mob_drop_quality", 0, 0, 1000000000));

    public static final RegistryObject<Attribute> MOB_DROP_YIELD = ATTRIBUTES.register("mob_drop_yield",
            () -> new RangedAttribute("qualitycrafting:mob_drop_yield", 0, 0, 1000000000));

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}

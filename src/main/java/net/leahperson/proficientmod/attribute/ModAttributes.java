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
            () ->  new RangedAttribute("qualitycrafting:proficiency",0,0,1000000000));

    public static final RegistryObject<Attribute> QUALITY = ATTRIBUTES.register("quality",
            () ->  new RangedAttribute("qualitycrafting:quality",0,0,1000000000));

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}

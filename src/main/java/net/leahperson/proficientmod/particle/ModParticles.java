package net.leahperson.proficientmod.particle;

import net.leahperson.proficientmod.ProficientMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ProficientMod.MOD_ID);

    public static final RegistryObject<SmallItemParticleType> SMALL_ITEM =
            PARTICLE_TYPES.register("small_item", SmallItemParticleType::new);

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}

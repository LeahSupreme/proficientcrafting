package net.leahperson.proficientmod.particle;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleType;

public class SmallItemParticleType extends ParticleType<ItemParticleOption> {

    public SmallItemParticleType() {
        super(false, ItemParticleOption.DESERIALIZER);
    }

    @Override
    public Codec<ItemParticleOption> codec() {
        return ItemParticleOption.codec(this);
    }
}

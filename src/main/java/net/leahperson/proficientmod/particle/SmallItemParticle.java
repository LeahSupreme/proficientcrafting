package net.leahperson.proficientmod.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SmallItemParticle extends TextureSheetParticle {
    private final float uo;
    private final float vo;

    SmallItemParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, ItemStack stack) {
        super(level, x, y, z, dx, dy, dz);
        this.setSprite(Minecraft.getInstance().getItemRenderer().getModel(stack, level, null, 0).getParticleIcon());
        this.uo = this.random.nextFloat() * 3.0f;
        this.vo = this.random.nextFloat() * 3.0f;
        this.quadSize *= 0.25f;
        this.gravity = 0.9f;
        this.lifetime = 6 + this.random.nextInt(8);
        this.hasPhysics = true;
    }

    @Override
    protected float getU0() {
        return this.sprite.getU((this.uo + 1.0f) / 4.0f * 16.0f);
    }

    @Override
    protected float getU1() {
        return this.sprite.getU(this.uo / 4.0f * 16.0f);
    }

    @Override
    protected float getV0() {
        return this.sprite.getV((this.vo + 1.0f) / 4.0f * 16.0f);
    }

    @Override
    protected float getV1() {
        return this.sprite.getV(this.vo / 4.0f * 16.0f);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<ItemParticleOption> {
        @Override
        public Particle createParticle(ItemParticleOption options, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new SmallItemParticle(level, x, y, z, dx, dy, dz, options.getItem());
        }
    }
}

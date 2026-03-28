package net.leahperson.proficientmod.effects;

import net.leahperson.proficientmod.attribute.ModAttributes;

public class CraftingQualityEffect extends CraftingStatEffect {
    public CraftingQualityEffect() {
        super(0x00C8FF,
                ModAttributes.QUALITY.get(),
                ModAttributes.MOB_DROP_QUALITY.get(),
                ModAttributes.FARMING_QUALITY.get(),
                ModAttributes.MINING_QUALITY.get());
    }
}

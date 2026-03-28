package net.leahperson.proficientmod.effects;

import net.leahperson.proficientmod.attribute.ModAttributes;

public class CraftingYieldEffect extends CraftingStatEffect {
    public CraftingYieldEffect() {
        super(0x00FF88,
                ModAttributes.YIELD.get(),
                ModAttributes.FARMING_YIELD.get(),
                ModAttributes.MINING_YIELD.get(),
                ModAttributes.MOB_DROP_YIELD.get());
    }
}

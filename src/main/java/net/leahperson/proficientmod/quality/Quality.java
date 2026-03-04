package net.leahperson.proficientmod.quality;

import net.leahperson.proficientmod.ProficientMod;
import net.minecraft.resources.ResourceLocation;

public record Quality(ResourceLocation type, int level) {
    public static final Quality NONE = new Quality(ProficientMod.location("none"), 0);
    public static final Quality PLAYER_PLACED = new Quality(ProficientMod.location("none"), -1);


}

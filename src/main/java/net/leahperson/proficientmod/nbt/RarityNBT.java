package net.leahperson.proficientmod.nbt;

import com.mojang.datafixers.types.templates.Hook;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class RarityNBT {

    public static int getMaxRarityIndex(Level level){

        Registry<QualityType> myregistry;

                myregistry= level.registryAccess().registryOrThrow(QualityType.RARITY_REGISTRY);
        int maxLevel = 0;
        Registry<QualityType> myRealRegistry = myregistry;
        Optional<QualityType> maxtype = myRealRegistry.stream().max((elema,elemb)->{
            return elema.index()-elemb.index();
        });
        return maxtype.get().index();
    }

    public static MutableComponent styleRarity(int index,String text){
        switch (index){
            case 0:
                return Component.literal(text);
            case 1:
                return Component.literal(text).withStyle(ChatFormatting.YELLOW);
            case 2:
                return Component.literal(text).withStyle(ChatFormatting.AQUA);
            case 3:
                return Component.literal(text).withStyle(ChatFormatting.GOLD);
        }

        return Component.literal(text);
    }

    public static Component getRarityComponent(int index){
        switch (index){
            case 0:
                return Component.literal("Common");
            case 1:
                return Component.literal("Uncommon").withStyle(ChatFormatting.YELLOW);
            case 2:
                return Component.literal("Rare").withStyle(ChatFormatting.AQUA);
            case 3:
                return Component.literal("Legendary").withStyle(ChatFormatting.GOLD);
        }
        return Component.literal("Common");
    }



    public static String getRarityName(int index){


        return "";
    }


}

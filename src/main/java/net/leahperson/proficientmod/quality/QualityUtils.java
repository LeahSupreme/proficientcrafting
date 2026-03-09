package net.leahperson.proficientmod.quality;

import net.leahperson.proficientmod.ProficientMod;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class QualityUtils {

    ///give Dev minecraft:iron_chestplate{"qualitycrafting":{"quality":3}}

    public static boolean hasQuality(ItemStack item){
        return getQualityLevel(item) > 0;

    }

    public static int getQualityLevel(ItemStack item){
        CompoundTag tag = item.getTag();

        try {
            if (tag != null) {
                CompoundTag qualityTag = tag.getCompound(ProficientMod.MOD_ID);
                int rarity = qualityTag.getInt("quality");
                return rarity;
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;

    }




    public static int getMaxRarityIndex(Level level){

        Registry<QualityDataType> myregistry;
        myregistry= level.registryAccess().registryOrThrow(QualityDataType.RARITY_REGISTRY);
        int maxLevel = 0;
        Registry<QualityDataType> myRealRegistry = myregistry;
        Optional<QualityDataType> maxtype = myRealRegistry.stream().max((elema, elemb)->{
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

    public static boolean isRarityItem(ItemStack stack, Level level){
        ResourceLocation itemId = stack.getItem().builtInRegistryHolder().key().location();
        Registry<QualityDataType> myregistry;
        myregistry= level.registryAccess().registryOrThrow(QualityDataType.RARITY_REGISTRY);
        int maxLevel = 0;
        Registry<QualityDataType> myRealRegistry = myregistry;
        boolean found = myRealRegistry.stream().anyMatch((elem)->{
            if(elem.icon().equals(itemId)){
                return true;
            }
            return false;
        });
        return found;
    }

public static ItemStack getRarityItem(int index, Level level){
    Registry<QualityDataType> myregistry;
    myregistry= level.registryAccess().registryOrThrow(QualityDataType.RARITY_REGISTRY);
    int maxLevel = 0;
    Registry<QualityDataType> myRealRegistry = myregistry;
    Optional<QualityDataType> selectedType = myRealRegistry.stream().filter((elem)->{
        if(elem.index() == index){
            return true;
        }
        return false;
    }).findAny();
    ResourceLocation icon = selectedType.get().icon();
    ItemStack something;
    Item somethingelse;
    return ForgeRegistries.ITEMS.getDelegate(icon).get().get().getDefaultInstance();
    /*Item.FILTERED_REGISTRIES.stream().filter((elem) -> {
        return elem.equals();
    }).findAny();
    return ItemStack.EMPTY;*/
}

    public static String getRarityName(int index){
        switch (index){
            case 0: return "Common";
            case 1: return "Uncommon";
            case 2: return "Rare";
            case 3: return "Legendary";
        }
        return "Common";
    }


}

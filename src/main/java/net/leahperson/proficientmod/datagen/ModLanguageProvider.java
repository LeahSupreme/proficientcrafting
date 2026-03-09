package net.leahperson.proficientmod.datagen;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.block.ModBlocks;
import net.leahperson.proficientmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(PackOutput output) {
        super(output, ProficientMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addSimpleItemName(ModItems.CRUDEHAMMER);
        addSimpleItemName(ModItems.IRONHAMMER);
        addSimpleItemName(ModItems.FUNNYRING);

        addSimpleBlockName(ModBlocks.FORGING_TABLE);

        add("creativetab.proficient_tab", "Quality Crafting");

        add("block.qualitycrafting.station.full", "The table is full");

        add("qualitycrafting.rarity.0", "Common");
        add("qualitycrafting.rarity.1", "Uncommon");
        add("qualitycrafting.rarity.2", "Rare");
        add("qualitycrafting.rarity.3", "Legendary");

        add("qualitycrafting.jei.levelcost", "Level Cost: ");
        add("qualitycrafting.jei.proficiencycost", "Proficiency required: ");
        add("qualitycrafting.jei.qualitycost", "Quality required: ");
        add("qualitycrafting.jei.yieldcost", "+%s Output per %s Yield");
        add("qualitycrafting.jei.rarityinputtooltip", "+%s Quality if %s");

        add("qualitycrafting:proficiency", "Proficiency");
        add("qualitycrafting:proficiency.desc", "Determines which items you can forge.");
        add("qualitycrafting:quality", "Quality");
        add("qualitycrafting:quality.desc", "Improves the result of forged items.");
        add("qualitycrafting:yield", "Yield");
        add("qualitycrafting:yield.desc", "Improves the amount of forged items created.");

        add("qualitycrafting.station.notproficient", "Your Proficiency is too low. Needed: %s Current: %s");
        add("qualitycrafting.station.noexperience", "You need more experience.");
    }

    public void addSimpleItemName(Supplier<? extends Item> supplier) {
        Item item = supplier.get();
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
        if (itemId != null) {
            add(item, toName(itemId.getPath()));
        } else {
            throw new IllegalStateException("Item not registered: " + item);
        }
    }

    public void addSimpleBlockName(Supplier<? extends Block> supplier) {
        Block block = supplier.get();
        ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(block);
        if (blockId != null) {
            add(block, toName(blockId.getPath()));
        } else {
            throw new IllegalStateException("Block not registered: " + block);
        }
    }

    private static String toName(String path) {
        String[] words = path.split("_");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                if (result.length() > 0) {
                    result.append(' ');
                }
                result.append(Character.toUpperCase(word.charAt(0)));
                result.append(word.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }
}

package net.leahperson.proficientmod.datagen;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ProficientMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleHandheldItem(ModItems.AMETHYST_PICKAXE);
        simpleHandheldItem(ModItems.AMETHYST_HOE);

        simpleHandheldItem(ModItems.STONE_FORGE_HAMMER);
        simpleHandheldItem(ModItems.IRON_FORGE_HAMMER);
        simpleHandheldItem(ModItems.DIAMOND_FORGE_HAMMER);
        simpleHandheldItem(ModItems.GOLD_FORGE_HAMMER);
        simpleHandheldItem(ModItems.AMETHYST_FORGE_HAMMER);
        simpleHandheldItem(ModItems.NETHERITE_FORGE_HAMMER);

        simpleHandheldItem(ModItems.WOODEN_LADLE);
        simpleHandheldItem(ModItems.IRON_LADLE);
        simpleHandheldItem(ModItems.DIAMOND_LADLE);
        simpleHandheldItem(ModItems.GOLD_LADLE);
        simpleHandheldItem(ModItems.AMETHYST_LADLE);
        simpleHandheldItem(ModItems.NETHERITE_LADLE);

        simpleItem(ModItems.IRON_CHISEL);
        simpleItem(ModItems.DIAMOND_CHISEL);
        //simpleItem(ModItems.GOLD_CHISEL);
        simpleItem(ModItems.AMETHYST_CHISEL);
        simpleItem(ModItems.NETHERITE_CHISEL);

        simpleHandheldItem(ModItems.IRON_SAW);
        simpleHandheldItem(ModItems.GOLD_SAW);
        simpleHandheldItem(ModItems.DIAMOND_SAW);
        simpleHandheldItem(ModItems.AMETHYST_SAW);
        simpleHandheldItem(ModItems.NETHERITE_SAW);

        simpleItem(ModItems.IRON_TIPPED_QUILL);
        simpleItem(ModItems.DIAMOND_TIPPED_QUILL);
        simpleItem(ModItems.NETHERITE_TIPPED_QUILL);

        simpleHandheldItem(ModItems.AMETHYST_SCEPTER);
        simpleHandheldItem(ModItems.DIAMOND_SCEPTER);
        simpleHandheldItem(ModItems.EMERALD_SCEPTER);
        simpleHandheldItem(ModItems.NETHER_STAR_SCEPTER);

        simpleItem(ModItems.LAPIS_RING);
        simpleItem(ModItems.DIAMOND_RING);
        simpleItem(ModItems.EMERALD_RING);
        simpleItem(ModItems.AMETHYST_RING);
        simpleItem(ModItems.NETHER_STAR_RING);

        simpleItem(ModItems.SMITHING_CHARM);

        simpleItem(ModItems.CRAFTERS_STEW);
        simpleItem(ModItems.ENERGETIC_SODA);
        simpleItem(ModItems.WEDDING_CAKE);

        simpleItem(ModItems.UNCOMMON_RARITY);
        simpleItem(ModItems.RARE_RARITY);
        simpleItem(ModItems.LEGENDARY_RARITY);
    }

    private void simpleItem(RegistryObject<Item> item) {
        String path = item.getId().getPath();
        ResourceLocation texture = ResourceLocation.tryBuild(ProficientMod.MOD_ID, "item/" + path);
        if (!existingFileHelper.exists(texture, PackType.CLIENT_RESOURCES, ".png", "textures")) {
            return;
        }
        withExistingParent(path, ResourceLocation.tryParse("item/generated"))
                .texture("layer0", texture);
    }

    private void simpleHandheldItem(RegistryObject<Item> item) {
        String path = item.getId().getPath();
        ResourceLocation texture = ResourceLocation.tryBuild(ProficientMod.MOD_ID, "item/" + path);
        if (!existingFileHelper.exists(texture, PackType.CLIENT_RESOURCES, ".png", "textures")) {
            return;
        }
        withExistingParent(path, ResourceLocation.tryParse("item/handheld"))
                .texture("layer0", texture);
    }
}

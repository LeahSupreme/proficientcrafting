package net.leahperson.proficientmod.block.entity;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ProficientMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<ForgingTableBlockEntity>> FORGING_STATION_BE =
            BLOCK_ENTITY_TYPES.register("forging_station_be",
                    () -> BlockEntityType.Builder.of(
                            ForgingTableBlockEntity::new,
                            ModBlocks.FORGING_STATION.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<CookingPotBlockEntity>> COOKING_POT_BE =
            BLOCK_ENTITY_TYPES.register("cooking_pot_be",
                    () -> BlockEntityType.Builder.of(
                            CookingPotBlockEntity::new,
                            ModBlocks.COOKING_POT.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<ScribingTableBlockEntity>> SCRIBING_TABLE_BE =
            BLOCK_ENTITY_TYPES.register("scribing_table_be",
                    () -> BlockEntityType.Builder.of(
                            ScribingTableBlockEntity::new,
                            ModBlocks.SCRIBING_TABLE.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<JewelcraftingStationBlockEntity>> JEWELCRAFTING_STATION_BE =
            BLOCK_ENTITY_TYPES.register("jewelcrafting_station_be",
                    () -> BlockEntityType.Builder.of(
                            JewelcraftingStationBlockEntity::new,
                            ModBlocks.JEWELCRAFTING_STATION.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<WorkbenchBlockEntity>> WORKBENCH_BE =
            BLOCK_ENTITY_TYPES.register("workbench_be",
                    () -> BlockEntityType.Builder.of(
                            WorkbenchBlockEntity::new,
                            ModBlocks.WORKBENCH.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<ReforgingAltarBlockEntity>> REFORGING_ALTAR_BE =
            BLOCK_ENTITY_TYPES.register("reforging_altar_be",
                    () -> BlockEntityType.Builder.of(
                            ReforgingAltarBlockEntity::new,
                            ModBlocks.REFORGING_ALTAR.get()
                    ).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}

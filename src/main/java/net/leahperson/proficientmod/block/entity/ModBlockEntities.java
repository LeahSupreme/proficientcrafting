package net.leahperson.proficientmod.block.entity;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraftforge.registries.ForgeRegistries.Keys.BLOCK_ENTITY_TYPES;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ProficientMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<ForgingTableBlockEntity>> FORGING_TABLE_BE =
            BLOCK_ENTITY_TYPES.register("forging_table_be",
                    () -> BlockEntityType.Builder.of(
                            ForgingTableBlockEntity::new,
                            ModBlocks.FORGING_TABLE.get()
                    ).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}

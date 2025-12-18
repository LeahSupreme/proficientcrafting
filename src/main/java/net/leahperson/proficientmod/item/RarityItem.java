package net.leahperson.proficientmod.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class RarityItem extends Item {
    public RarityItem(Properties pProperties) {
        super(pProperties);

    }

    public static int getRarityType(ItemStack rarityItem){

        return 3;
    }

    



    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return RarityBEWLR.INSTANCE;
            }
        });
    }
}

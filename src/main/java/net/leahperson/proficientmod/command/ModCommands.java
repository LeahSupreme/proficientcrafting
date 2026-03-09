package net.leahperson.proficientmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.leahperson.proficientmod.util.QualityDataUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("qualityCrafting")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("giveRarity")
                                .then(Commands.argument("rarity", IntegerArgumentType.integer(0, 3))
                                        .executes(ModCommands::executeGiveRarity)
                                )
                        )
        );
    }

    private static int executeGiveRarity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        int rarity = IntegerArgumentType.getInteger(context, "rarity");
        Player player = context.getSource().getPlayerOrException();
        ItemStack heldStack = player.getMainHandItem();
        if (heldStack.isEmpty()) {
            context.getSource().sendFailure(Component.literal("You must be holding an item."));
            return 0;
        }
        QualityDataUtil.setRarity(heldStack, rarity);
        context.getSource().sendSuccess(
                () -> Component.literal("Set rarity " + rarity + " on " + heldStack.getHoverName().getString() + "."),
                false
        );
        return 1;
    }
}

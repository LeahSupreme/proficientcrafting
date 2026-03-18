package net.leahperson.proficientmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.leahperson.proficientmod.attribute.AttributeAddition;
import net.leahperson.proficientmod.util.QualityDataUtil;
import net.leahperson.proficientmod.util.RarityAttributeNBT;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ModCommands {

    private static final SuggestionProvider<CommandSourceStack> OPERATION_SUGGESTIONS =
            (ctx, builder) -> SharedSuggestionProvider.suggest(
                    List.of("ADDITION", "MULTIPLY_BASE", "MULTIPLY_TOTAL"), builder);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("qualityCrafting")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("giveRarity")
                                .then(Commands.argument("rarity", IntegerArgumentType.integer(0, 3))
                                        .executes(ModCommands::executeGiveRarity)
                                )
                        )
                        .then(Commands.literal("addAttribute")
                                .then(Commands.argument("attribute", ResourceLocationArgument.id())
                                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggestResource(
                                                ForgeRegistries.ATTRIBUTES.getKeys(), builder))
                                        .then(Commands.argument("amount", DoubleArgumentType.doubleArg())
                                                .then(Commands.argument("operation", StringArgumentType.word())
                                                        .suggests(OPERATION_SUGGESTIONS)
                                                        .executes(ModCommands::executeAddAttribute)
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("clearAttributes")
                                .executes(ModCommands::executeClearAttributes)
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

    private static int executeAddAttribute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = context.getSource().getPlayerOrException();
        ItemStack heldStack = player.getMainHandItem();
        if (heldStack.isEmpty()) {
            context.getSource().sendFailure(Component.literal("You must be holding an item."));
            return 0;
        }

        ResourceLocation attributeId = ResourceLocationArgument.getId(context, "attribute");
        Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(attributeId);
        if (attribute == null) {
            context.getSource().sendFailure(Component.literal("Unknown attribute: " + attributeId));
            return 0;
        }

        double amount = DoubleArgumentType.getDouble(context, "amount");
        String operation = StringArgumentType.getString(context, "operation").toUpperCase();
        try {
            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.valueOf(operation);
        } catch (IllegalArgumentException e) {
            context.getSource().sendFailure(Component.literal(
                    "Invalid operation '" + operation + "'. Use ADDITION, MULTIPLY_BASE, or MULTIPLY_TOTAL."));
            return 0;
        }

        RarityAttributeNBT.addAttribute(heldStack, new AttributeAddition(attributeId.toString(), amount, operation));

        String amountStr = amount == Math.floor(amount) ? String.valueOf((long) amount) : String.format("%.4f", amount);
        context.getSource().sendSuccess(
                () -> Component.literal("Added " + operation + " " + amountStr + " "
                        + Component.translatable(attribute.getDescriptionId()).getString()
                        + " to " + heldStack.getHoverName().getString() + "."),
                false
        );
        return 1;
    }

    private static int executeClearAttributes(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = context.getSource().getPlayerOrException();
        ItemStack heldStack = player.getMainHandItem();
        if (heldStack.isEmpty()) {
            context.getSource().sendFailure(Component.literal("You must be holding an item."));
            return 0;
        }
        RarityAttributeNBT.clearAttributes(heldStack);
        context.getSource().sendSuccess(
                () -> Component.literal("Cleared rarity attributes from " + heldStack.getHoverName().getString() + "."),
                false
        );
        return 1;
    }
}

package me.madisonn.staffhelpermod.commands.utils;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.madisonn.staffhelpermod.utils.skin.SkinViewScreen;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class SkinGrabberCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> register(String commandName) {
        return literal("getskin")
                .then(argument("playername", StringArgumentType.word())
                        .suggests(SkinGrabberCommand::suggestPlayers)
                        .executes(SkinGrabberCommand::getSkin))
                .executes(ctx -> {
                    String errorMessage = commandName.equals("sh")
                            ? "Staff Helper » Usage: /sh getskin <playername>"
                            : "Staff Helper » Usage: /staffhelp getskin <playername>";
                    ctx.getSource().sendFeedback(Component.literal(errorMessage).withColor(0xFF0000));
                    return 1;
                });
    }

    public static int getSkin(CommandContext<FabricClientCommandSource> context) {
        String playerName = StringArgumentType.getString(context, "playername");
        Minecraft client = Minecraft.getInstance();

        context.getSource().sendFeedback(Component.literal("Staff Helper » Opening skin viewer for: " + playerName).withColor(0x00FF00));

        client.execute(() -> client.setScreen(new SkinViewScreen(playerName)));

        return 1;
    }

    public static CompletableFuture<Suggestions> suggestPlayers(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        Minecraft client = Minecraft.getInstance();
        if (client.getConnection() != null) {
            String input = builder.getRemaining().toLowerCase();

            client.getConnection().getOnlinePlayers().forEach(player -> {
                String playerName = player.getProfile().name();
                if (playerName.toLowerCase().startsWith(input)) {
                    builder.suggest(playerName);
                }
            });
        }
        return builder.buildFuture();
    }
}

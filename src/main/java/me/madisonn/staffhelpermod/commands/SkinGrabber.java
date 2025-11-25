package me.madisonn.staffhelpermod.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import me.madisonn.staffhelpermod.utils.skin.SkinViewScreen;

import java.util.concurrent.CompletableFuture;

public class SkinGrabber {
    public static int getskin(CommandContext<FabricClientCommandSource> context) {
        String playerName = StringArgumentType.getString(context, "playername");
        MinecraftClient client = MinecraftClient.getInstance();

        context.getSource().sendFeedback(Text.literal("StaffHelper » Opening skin viewer for: " + playerName).withColor(0x00FF00));

        client.send(() -> {
            client.setScreen(new SkinViewScreen(playerName));
        });

        return 1;
    }

    public static CompletableFuture<Suggestions> suggestPlayers(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() != null) {
            String input = builder.getRemaining().toLowerCase();

            client.getNetworkHandler().getPlayerList().forEach(player -> {
                String playerName = player.getProfile().getName();
                if (playerName.toLowerCase().startsWith(input)) {
                    builder.suggest(playerName);
                }
            });
        }
        return builder.buildFuture();
    }
}
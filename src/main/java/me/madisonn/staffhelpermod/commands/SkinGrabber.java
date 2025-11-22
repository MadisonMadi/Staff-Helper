package me.madisonn.staffhelpermod.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.concurrent.CompletableFuture;

public class SkinGrabber {
    public static int getskin(CommandContext<FabricClientCommandSource> context) {
        String playerName = StringArgumentType.getString(context, "playername");
        String url = "https://nl.namemc.com/profile/" + playerName;

        context.getSource().sendFeedback(Text.literal("")
                .append(Text.literal("StaffHelper » NameMC Profile: ").withColor(0x00FF00))
                .append(Text.literal(url)
                        .formatted(Formatting.AQUA, Formatting.UNDERLINE)
                        .styled(style -> style
                                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        Text.literal("Click to open in browser").withColor(0x00FF00))))
                ));
        return 1;
    }

    public static CompletableFuture<Suggestions> suggestPlayers(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() != null) {
            client.getNetworkHandler().getPlayerList().forEach(player -> {
                builder.suggest(player.getProfile().getName());
            });
        }
        return builder.buildFuture();
    }
}
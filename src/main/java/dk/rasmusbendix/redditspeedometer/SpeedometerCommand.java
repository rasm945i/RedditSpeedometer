package dk.rasmusbendix.redditspeedometer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record SpeedometerCommand(RedditSpeedometer plugin, boolean allowToggle) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (!allowToggle) {
            sender.sendMessage(
                    ChatColor.translateAlternateColorCodes('&',
                            plugin.getConfig().getString(
                                    "messages.toggling-not-allowed",
                                    "&cToggling the speedometer is not allowed."
                            )
                    )
            );
            return true;
        }

        // Default to disabled message
        String message = plugin.getConfig().getString(
                "messages.disabled-speedometer",
                "&eSpeedometer is now &cdisabled&e!"
        );

        // If speedometer gets enabled, change the message accordingly :)
        if (plugin.getSpeedometerUpdater().toggleSpeedometer(((Player) sender))) {
            message = plugin.getConfig().getString(
                    "messages.enabled-speedometer",
                    "&eSpeedometer is now &aenabled&e!"
            );
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        return true;

    }
}

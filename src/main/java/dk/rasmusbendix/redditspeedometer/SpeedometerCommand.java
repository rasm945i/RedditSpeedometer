package dk.rasmusbendix.redditspeedometer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record SpeedometerCommand(SpeedometerUpdater updater) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        boolean result = updater.toggleSpeedometer(((Player) sender));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eSpeedometer is now " +
                (result ? "&aenabled" : "&cdisabled") + "&e!"));

        return true;
    }
}

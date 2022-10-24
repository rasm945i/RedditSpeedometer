package dk.rasmusbendix.redditspeedometer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PapiExpansion extends PlaceholderExpansion {

    private final RedditSpeedometer plugin;

    public PapiExpansion(RedditSpeedometer plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("PAPI registered, attempting to enable expansion!");
    }


    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "speedometer";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {

        if(!player.isOnline())
            return null;

        if(params.equalsIgnoreCase("speed")) {
            return plugin.getSpeedometerUpdater().getSpeed(player.getPlayer());
        }

        return null;

    }
}

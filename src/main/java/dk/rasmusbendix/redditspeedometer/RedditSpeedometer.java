package dk.rasmusbendix.redditspeedometer;

import dk.rasmusbendix.redditspeedometer.listener.DisconnectListener;
import dk.rasmusbendix.redditspeedometer.listener.JoinListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RedditSpeedometer extends JavaPlugin {

    @Getter
    private SpeedometerUpdater speedometerUpdater;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        speedometerUpdater = new SpeedometerUpdater(this);

        // Listeners to properly keep track of joining and leaving players, so we don't store unnecessary information
        new JoinListener(this);
        new DisconnectListener(this);

        // Toggle command
        boolean allowToggle = getConfig().getBoolean("allow-toggle", true);
        getLogger().info("Allow toggling speedometer: " + allowToggle);
        if (allowToggle) {
            //noinspection ConstantConditions
            this.getServer().getPluginCommand("speedometer").setExecutor(new SpeedometerCommand(speedometerUpdater));
        }

        // Register expansion if PAPI is present
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PapiExpansion(this).register();
        }

    }

    @Override
    public void onDisable() {}
}

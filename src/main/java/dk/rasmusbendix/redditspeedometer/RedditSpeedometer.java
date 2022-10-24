package dk.rasmusbendix.redditspeedometer;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class RedditSpeedometer extends JavaPlugin {

    @Getter private SpeedometerUpdater speedometerUpdater;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        speedometerUpdater = new SpeedometerUpdater(this);
        this.getServer().getPluginCommand("speedometer").setExecutor(new SpeedometerCommand(speedometerUpdater));
        new DisconnectListener(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

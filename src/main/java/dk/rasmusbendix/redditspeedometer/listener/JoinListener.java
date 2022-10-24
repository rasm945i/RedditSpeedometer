package dk.rasmusbendix.redditspeedometer.listener;

import dk.rasmusbendix.redditspeedometer.RedditSpeedometer;
import dk.rasmusbendix.redditspeedometer.SpeedometerUpdater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final SpeedometerUpdater updater;

    public JoinListener(RedditSpeedometer plugin) {
        this.updater = plugin.getSpeedometerUpdater();
        boolean enabledByDefault = plugin.getConfig().getBoolean("enabled-by-default", true);
        plugin.getLogger().info("SpeedoMeter enabled by default for all players: " + enabledByDefault);
        if(enabledByDefault) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        updater.enableSpeedometer(e.getPlayer());
    }

}

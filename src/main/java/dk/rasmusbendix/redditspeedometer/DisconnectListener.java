package dk.rasmusbendix.redditspeedometer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Remove players from Speedometer tracking when they disconnect
 */
public class DisconnectListener implements Listener {

    private final SpeedometerUpdater updater;

    public DisconnectListener(RedditSpeedometer plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.updater = plugin.getSpeedometerUpdater();
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        updater.disableSpeedometer(e.getPlayer());
    }

}

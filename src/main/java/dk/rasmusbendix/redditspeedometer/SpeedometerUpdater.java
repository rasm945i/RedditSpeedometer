package dk.rasmusbendix.redditspeedometer;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

public class SpeedometerUpdater {

    private final DecimalFormat df = new DecimalFormat("#.##");
    private final JavaPlugin plugin;
    /**
     * UUID - The player
     * Double - The speed
     */
    private final HashMap<UUID, Double> speedometerEnabled;


    /**
     * UUID - The player
     * LocationTimestamp - Keeps track of last location and when the player was there
     */
    private final HashMap<UUID, LocationTimestamp> locationMap;

    /**
     * @param plugin JavaPlugin instance used to run the repeating task.
     */
    public SpeedometerUpdater(JavaPlugin plugin) {
        this.plugin = plugin;
        this.speedometerEnabled = new HashMap<>();
        this.locationMap = new HashMap<>();
        run();
    }


    /**
     * @param player The player to toggle speedometer for.
     * @return true - The player now has the speedometer enabled.<br>
     * false - The speedometer is now disabled for the player.
     */
    public boolean toggleSpeedometer(Player player) {
        if (speedometerEnabled.containsKey(player.getUniqueId())) {
            speedometerEnabled.remove(player.getUniqueId());
            return false;
        }
        speedometerEnabled.put(player.getUniqueId(), 0d);
        locationMap.put(player.getUniqueId(), new LocationTimestamp(player));
        return true;
    }

    /**
     * @param player The player to disable the speedometer for.
     */
    public void disableSpeedometer(Player player) {
        speedometerEnabled.remove(player.getUniqueId());
        locationMap.remove(player.getUniqueId());
    }

    public void enableSpeedometer(Player player) {
        if(hasSpeedometerEnabled(player))
            return;
        speedometerEnabled.put(player.getUniqueId(), 0d);
        locationMap.put(player.getUniqueId(), new LocationTimestamp(player));
    }

    /**
     * @param player Target to check if speedometer is enabled for.
     * @return true - The player has speedometer enabled.<br>
     *  false - The player has speedometer disabled.
     */
    public boolean hasSpeedometerEnabled(Player player) {
        return speedometerEnabled.containsKey(player.getUniqueId());
    }

    /**
     * @param player The player to set the new speed value for.
     */
    public void updateSpeedometer(Player player) {

        LocationTimestamp previous = locationMap.get(player.getUniqueId());
        LocationTimestamp current = new LocationTimestamp(player);

        double blocksPerSecond = previous.getBlocksPerSecond(current);

        // Display blocks per second in the action-bar if its enabled
        if(plugin.getConfig().getBoolean("show-in-actionbar")) {
            player.sendActionBar(Component.text(df.format(blocksPerSecond)));
        }

        speedometerEnabled.put(player.getUniqueId(), blocksPerSecond);
        locationMap.put(player.getUniqueId(), current);

    }

    /**
     * Repeating task that updates all online players speedometer if they have it enabled.
     */
    private void run() {

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (hasSpeedometerEnabled(player)) {
                updateSpeedometer(player);
            }
        }

        plugin.getServer().getScheduler().runTaskLaterAsynchronously(
                plugin,
                this::run,
                plugin.getConfig().getInt("update-rate-in-ticks", 20)
        );

    }

    public String getSpeed(Player player) {
        if(!hasSpeedometerEnabled(player))
            return "N/A";
        return df.format(speedometerEnabled.get(player.getUniqueId()));
    }


    /**
     * A wrapper class to contain a timestamp along with a location
     */
    private static class LocationTimestamp {

        /**
         * System.currentTimeMillis
         */
        @Getter private final long timestamp;

        /**
         * The players location at the given time
         */
        @Getter private final Location location;

        public LocationTimestamp(Player player) {
            this.timestamp = System.currentTimeMillis();
            this.location = player.getLocation();
        }

        /**
         * @param other The other LocationTimestamp to compare to
         * @return How many blocks per second the player moves per second
         */
        public double getBlocksPerSecond(LocationTimestamp other) {

            double dist = location.distance(other.location);
            double timeDiff = (Math.max(timestamp, other.timestamp) - Math.min(timestamp, other.timestamp)) / 1000f;

            if(dist == 0 || timeDiff == 0)
                return 0;

            return dist / timeDiff;

        }

    }

}

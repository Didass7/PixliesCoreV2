package net.pixlies.core.utils.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@Data
@AllArgsConstructor
public class LazyLocation {

    private final String world;
    private final double x, y, z;
    private final float pitch, yaw;

    public LazyLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
    }

    public World getAsBukkitWorld() {
        return Main.getInstance().getServer().getWorld(world);
    }

    public Location getAsBukkitLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
    }

    @Override
    public String toString() {
        return world + ";" + x + ";" + y + ";" + z + ";" + pitch + ";" + yaw;
    }

    public static LazyLocation fromString(String string) {
        String[] args = string.split(";");
        return new LazyLocation(args[0],
                Double.parseDouble(args[1]),
                Double.parseDouble(args[2]),
                Double.parseDouble(args[3]),
                Float.parseFloat(args[4]),
                Float.parseFloat(args[5])
        );
    }

}
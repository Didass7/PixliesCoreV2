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

    private String world;
    private double x, y, z;
    private float pitch, yaw;

    public LazyLocation(Location location) {
        setLocation(location);
    }

    public void setLocation(Location location) {
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

}
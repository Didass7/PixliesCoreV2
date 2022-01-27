package net.pixlies.nations.nations.chunk;

import lombok.Data;
import net.pixlies.core.Main;
import org.bukkit.Location;
import org.bukkit.World;

@Data
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

    @Override
    public String toString() {
        return world + ";" + x + ";" + y + ";" + z + ";" + pitch + ";" + yaw;
    }

}

package net.pixlies.nations.nations.chunk;

import co.aikar.commands.lib.util.Table;
import com.google.gson.JsonObject;
import dev.morphia.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.pixlies.nations.Nations;
import net.pixlies.nations.nations.Nation;

import java.util.HashMap;
import java.util.Map;

/**
 * Efficient nation chunk system
 *
 * @author MickMMars
 * @author Dynmie
 */
@Entity
@Data
@AllArgsConstructor
public class NationChunk {
    private static final Nations instance = Nations.getInstance();

    @Getter private static Map<String, Table<Integer, Integer, NationChunk>> table = new HashMap<>(); // World, X, Y, NationChunk

    private String nationId, world;
    private int x, z;
    private NationChunkType type;
    private JsonObject data;

    public void claim(boolean claim) {
        Table<Integer, Integer, NationChunk> rst = table.get(world);
        rst.put(x, z, this);
        table.put(world, rst);
        Nation nation = Nation.getFromId(nationId);
        if (!nation.getClaims().contains(this)) {
            nation.getClaims().add(this);
            nation.save();
        }

        if (claim)
            instance.getLogger().info("§b" + type.name() + "-Chunk claimed at §e" + x + "§8, §e " + z + "§bfor §e" + nation.getName());
    }

    /**
     * Unclaim this chunk.
     */
    public void unclaim() {
        if (table.get(world) == null) return;

        Nation nation = Nation.getFromId(nationId);
        if (nation == null)
            return;

        Table<Integer, Integer, NationChunk> rst = table.get(world);

        if (nation.getClaims().contains(this)) {
            nation.getClaims().remove(this);
            nation.save();
        }

        rst.remove(x, z);
        table.put(world, rst);
    }

}

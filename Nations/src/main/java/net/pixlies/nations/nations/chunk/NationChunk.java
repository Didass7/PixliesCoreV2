package net.pixlies.nations.nations.chunk;

import co.aikar.commands.lib.util.Table;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Efficient nation chunk system
 *
 * @author MickMMars
 * @author Dynmie
 */
public class NationChunk {
    public static Map<String, Table<Integer, Integer, NationChunk>> table;

    private String              nationId, world;
    private int                 x,z;
    private NationChunkType     type;
    private JsonObject          data;

    public void claim(boolean claim) {
        Table<Integer, Integer, NationChunk> rst = table.get(world);
        rst.put(x,z, this);
        table.put(world, rst);
//        Nation nation = Nation.getById(nationId);
//        if(!nation.getChunks().contains(serialize())) {
//            nations.getChunks().add(serialize());
//            nations.save();
//        }
//
//        if (claim) System.out.println("§b" + type.name() + "-Chunk claimed at §e" + x + "§8, §e " + z + "§bfor §e" + nation.getName());
    }

}

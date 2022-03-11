package net.pixlies.nations.nations.chunk;

import co.aikar.commands.lib.util.Table;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morphia.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.pixlies.core.entity.user.User;
import net.pixlies.nations.Nations;
import net.pixlies.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.Nation;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

    public void grantAccess(@NotNull NationProfile profile) {
        Nation nation = Nation.getFromId(nationId);
        nation.getClaims().remove(this);
        nation.save();

        JsonArray accessors = data.has("accessors") ? data.getAsJsonArray("accessors") : new JsonArray();
        accessors.add(profile.getUuid());
        data.add("accessors", accessors);

        claim(false);
    }

    public void revokeAccess(@NotNull NationProfile profile) {
        if (!data.has("accessors")) return;

        Nation nation = Nation.getFromId(nationId);
        nation.getClaims().remove(this);
        nation.save();

        JsonArray accessors = data.getAsJsonArray("accessors");
        for (JsonElement accessor : accessors) {
            if (!accessor.getAsString().equals(profile.getUniqueId().toString())) {
                return;
            }
            accessors.remove(accessor);
        }
        data.add("accessors", accessors);

        claim(false);
    }

    public boolean hasAccess(@NotNull NationProfile profile) {
        for (NationProfile toCheck : this.getAccessors()) {
            if (profile.getUniqueId().equals(toCheck.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public @NotNull List<NationProfile> getAccessors() {
        List<NationProfile> returner = new ArrayList<>();
        if (!data.has("accessors")) return returner;

        JsonArray accessors = data.getAsJsonArray("accessors");
        for (JsonElement accessor : accessors) {
            UUID uuid = UUID.fromString(accessor.getAsString());
            User user = User.get(uuid);
            NationProfile profile = NationProfile.get(user);
            returner.add(profile);
        }

        return returner;
    }

}

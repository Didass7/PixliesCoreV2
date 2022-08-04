package net.pixlies.nations.nations.chunk;

import co.aikar.commands.lib.util.Table;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    private String data;

    public void claim(boolean claim) {
        Table<Integer, Integer, NationChunk> rst = table.get(world);
        rst.put(x, z, this);
        table.put(world, rst);

        Nation nation = Nation.getFromId(nationId);
        if (nation == null)
            return;

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

    /**
     * Grant a NationProfile access to a chunk
     * @see NationProfile
     * @param profile the profile to allow access to
     */
    public void grantAccess(@NotNull NationProfile profile) {
        Nation nation = Nation.getFromId(nationId);
        if (nation == null)
            return;
        nation.getClaims().remove(this);
        nation.save();

        JsonObject data = this.getJsonData();
        JsonArray accessors = data.has("accessors") ? data.getAsJsonArray("accessors") : new JsonArray();
        accessors.add(profile.getUuid());
        data.add("accessors", accessors);
        this.setJsonData(data);

        claim(false);
    }

    /**
     * Revoke a NationProfile access to a chunk
     * @see NationProfile
     * @param profile the profile to revoke access to
     */
    public void revokeAccess(@NotNull NationProfile profile) {
        JsonObject data = this.getJsonData();
        if (!data.has("accessors")) return;

        Nation nation = Nation.getFromId(nationId);
        if (nation == null)
            return;
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
        this.setJsonData(data);

        claim(false);
    }

    /**
     * Check if a NationProfile has access to a chunk
     * @see NationProfile
     * @param profile the profile to check access
     */
    public boolean hasAccess(@NotNull NationProfile profile) {
        for (NationProfile toCheck : this.getAccessors()) {
            if (profile.getUniqueId().equals(toCheck.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets all the accessors in a chunk
     * @see NationProfile
     * @return a list of all chunk accessors
     */
    public @NotNull List<NationProfile> getAccessors() {
        List<NationProfile> returner = new ArrayList<>();

        JsonObject data = this.getJsonData();
        if (!data.has("accessors")) return returner;

        JsonArray accessors = data.getAsJsonArray("accessors");
        for (JsonElement accessor : accessors) {
            UUID uuid = UUID.fromString(accessor.getAsString());
            NationProfile profile = NationProfile.get(uuid);
            returner.add(profile);
        }
        this.setJsonData(data);

        return returner;
    }

    public JsonObject getJsonData() {
        return JsonParser.parseString(data).getAsJsonObject();
    }

    public void setJsonData(JsonObject data) {
        this.data = data.getAsString();
    }

}

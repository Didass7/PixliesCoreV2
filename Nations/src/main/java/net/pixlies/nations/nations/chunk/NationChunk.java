package net.pixlies.nations.nations.chunk;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.pixlies.nations.Nations;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bson.Document;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Efficient nation chunk system
 *
 * @author MickMMars
 * @author Dynmie
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class NationChunk {
    private static final Nations instance = Nations.getInstance();

    private String nationId;
    private String world;
    private int x;
    private int z;
    private NationChunkType type;
    private List<String> accessors; // UUID of accessors

    public NationChunk(String id, String world, int x, int z) {
        this(
                id,
                world,
                x,
                z,
                NationChunkType.NORMAL,
                new ArrayList<>()
        );
    }

    public NationChunk(Document document) {
        this.nationId = document.getString("nationId");
        this.world = document.getString("world");
        this.x = document.getInteger("x");
        this.z = document.getInteger("z");
        try {
            this.type = NationChunkType.valueOf(document.getString("type"));
        } catch (IllegalArgumentException e) {
            this.type = NationChunkType.NORMAL;
        }
        this.accessors = document.getList("accessors", String.class);
    }

    public Document toDocument() {
        Document document = new Document();

        document.put("nationId", nationId);
        document.put("world", world);
        document.put("x", x);
        document.put("z", z);
        document.put("type", type.name());
        document.put("accessors", accessors);

        return document;
    }

    /**
     * Claims a chunk
     * Backup will not be called.
     * @see Nation#save()
     * @param log Logs the claim.
     */
    public void claim(boolean log) {
        Nation nation = Nation.getFromId(nationId);
        if (nation == null)
            return;

        nation.getClaims().add(this);
        loadClaim();

        if (log) {
            instance.getLogger().info( type.name() + " Chunk claimed at " + x + ", " + z + "for Nation " + nation.getName());
        }

    }

    public void loadClaim() {
        Table<Integer, Integer, NationChunk> claims = instance.getNationManager().getNationClaims().computeIfAbsent(getWorld(),
                k -> HashBasedTable.create());
        claims.put(getX(), getZ(), this);
    }

    public void unloadClaim() {
        Table<Integer, Integer, NationChunk> claims = instance.getNationManager().getNationClaims().get(getWorld());
        if (claims == null) return;
        claims.remove(getX(), getZ());
    }

    /**
     * Unclaim this chunk.
     * Backup will not be called.
     * @see Nation#save()
     **/
    public void unclaim() {
        Nation nation = Nation.getFromId(nationId);
        if (nation == null)
            return;

        nation.getClaims().remove(this);
        unloadClaim();
    }

    public @Nullable Nation getNation() {
        return Nation.getFromId(nationId);
    }

    /**
     * Grant a NationProfile access to a chunk
     * @see NationProfile
     * @param profile the profile to allow access to
     */
    public void grantAccess(@NotNull NationProfile profile) {
        accessors.add(profile.getUuid());
    }

    /**
     * Revoke a NationProfile access to a chunk
     * @see NationProfile
     * @param profile the profile to revoke access to
     */
    public void revokeAccess(@NotNull NationProfile profile) {
        accessors.remove(profile.getUuid());
    }

    /**
     * Check if a NationProfile has access to a chunk
     * @see NationProfile
     * @param profile the profile to check access
     */
    public boolean hasAccess(@NotNull NationProfile profile) {
        return accessors.contains(profile.getUuid());
    }

    public static @Nullable NationChunk getClaimAt(String world, int x, int z) {
        if (world == null) return null;
        Table<Integer, Integer, NationChunk> claims = instance.getNationManager().getNationClaims().get(world);
        if (claims == null) return null;
        return claims.get(x, z);
    }

    public static @Nullable NationChunk getClaimFromChunk(Chunk chunk) {
        return getClaimAt(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
    }

}

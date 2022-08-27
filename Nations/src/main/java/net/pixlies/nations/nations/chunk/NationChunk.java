package net.pixlies.nations.nations.chunk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.pixlies.nations.Nations;
import net.pixlies.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.Nation;
import org.bson.Document;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

        if (log) {
            instance.getLogger().info("§b" + type.name() + "-Chunk claimed at §e" + x + "§8, §e " + z + "§bfor §e" + nation.getName());
        }

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
        Nation nation = Nation.getFromId(nationId);
        if (nation == null)
            return;
        nation.getClaims().remove(this);
        nation.save();

        accessors.add(profile.getUuid());

        claim(false);
    }

    /**
     * Revoke a NationProfile access to a chunk
     * @see NationProfile
     * @param profile the profile to revoke access to
     */
    public void revokeAccess(@NotNull NationProfile profile) {
        Nation nation = Nation.getFromId(nationId);
        if (nation == null)
            return;

        accessors.remove(profile.getUuid());

        claim(false);
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
        for (Nation nation : instance.getNationManager().getNations().values()) {
            for (NationChunk nationChunk : nation.getClaims()) {
                if (nationChunk.getX() == x && nationChunk.getZ() == z && nationChunk.getWorld().equalsIgnoreCase(world)) {
                    return nationChunk;
                }
            }
        }
        return null;
    }

    public static @Nullable NationChunk getClaimFromChunk(Chunk chunk) {
        return getClaimAt(chunk.getWorld().toString(), chunk.getX(), chunk.getZ());
    }

    public static ClaimType getClaimType(String world, int x, int z) {
        NationChunk chunk = getClaimAt(world, x, z);
        if (chunk == null) {
            return ClaimType.WILDERNESS;
        }
        Nation nation = chunk.getNation();
        if (nation == null) {
            return ClaimType.WILDERNESS;
        }
        return switch (nation.getNationId()) {
            case "warp" -> ClaimType.WARP;
            case "warzone" -> ClaimType.WARZONE;
            case "spawn" -> ClaimType.SPAWN;
            default -> ClaimType.NORMAL;
        };
    }

}

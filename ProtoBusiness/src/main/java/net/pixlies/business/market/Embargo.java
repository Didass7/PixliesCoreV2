package net.pixlies.business.market;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.nations.nations.Nation;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Represents a trade embargo.
 *
 * @author vyketype
 */
public record Embargo(String embargoId, String initId, String targetId, int duration) {
    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private static final String EMBARGOES_PATH = instance.getDataFolder().getAbsolutePath() + "/embargoes/";
    
    public void save() {
        String filename = embargoId + ".yml";
        
        File file = new File(EMBARGOES_PATH + filename);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        
        yaml.set("initId", initId);
        yaml.set("targetId", targetId);
        yaml.set("duration", duration);
        
        try {
            yaml.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
            instance.getLogger().log(Level.SEVERE, "Unable to save embargo of ID " + embargoId + ".");
        }
        
        instance.logInfo("Saved embargo " + embargoId + " to files.");
    }
    
    public boolean delete() {
        File file = new File(EMBARGOES_PATH + embargoId + ".yml");
        return file.delete();
    }
    
    public void sendDeletionMessages() {
        Nation initNation = Nation.getFromId(initId);
        Nation targetNation = Nation.getFromId(targetId);
        
        // Send message to the players of the initial nation
        for (UUID uuid : Objects.requireNonNull(initNation.getMembers())) {
            if (Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline())
                continue;
            MarketLang.INCOMING_EMBARGO_REMOVED.send(Bukkit.getPlayer(uuid), "%NATION%;" + targetNation.getName());
        }
    
        // Send message to the players of the target nation
        for (UUID uuid : Objects.requireNonNull(targetNation.getMembers())) {
            if (Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline())
                continue;
            MarketLang.OUTGOING_EMBARGO_REMOVED.send(Bukkit.getPlayer(uuid), "%NATION%;" + initNation.getName());
        }
    }
    
    // --------------------------------------------------------------------------------------------
    
    public static List<Embargo> getAll() {
        File directory = new File(EMBARGOES_PATH);
        if (directory.list() == null) {
            directory.mkdirs();
        }
        
        List<Embargo> embargoes = new ArrayList<>();
        List<String> pathnames = List.of(Objects.requireNonNull(new File(EMBARGOES_PATH).list()));
        for (String pathname : pathnames) {
            embargoes.add(Embargo.get(pathname.substring(0, pathname.length() - 4)));
        }
        return embargoes;
    }
    
    public static Embargo get(String embargoId) {
        String filename = embargoId + ".yml";
        File file = new File(EMBARGOES_PATH + filename);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        return new Embargo(embargoId, yaml.getString("initId"), yaml.getString("targetId"), yaml.getInt("duration"));
    }
    
    public static @Nullable String getEmbargoId(String initNation, String targetNation) {
        for (Embargo embargo : Embargo.getAll()) {
            boolean fromCond = Objects.equals(embargo.initId(),
                    Objects.requireNonNull(Nation.getFromName(initNation)).getNationId());
            boolean toCond = Objects.equals(embargo.targetId(),
                    Objects.requireNonNull(Nation.getFromName(targetNation)).getNationId());
            if (fromCond && toCond) return embargo.embargoId();
        }
        return null;
    }
}

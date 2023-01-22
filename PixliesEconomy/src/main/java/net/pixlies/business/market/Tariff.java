package net.pixlies.business.market;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.business.PixliesEconomy;
import net.pixlies.core.utils.TextUtils;
import net.pixlies.nations.nations.Nation;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Tariff class.
 * Փիւսկիլլիւ պելա․․․
 *
 * @author vyketype
 */
@Getter
@AllArgsConstructor
public class Tariff {
    private static final PixliesEconomy instance = PixliesEconomy.getInstance();
    private static final String TARIFFS_PATH = instance.getDataFolder().getAbsolutePath() + "/tariffs/";
    
    private final String tariffId;
    private final String initId;
    private final String targetId;
    private final double rate;
    
    public Tariff(String initId, String targetId, double rate) {
        tariffId = TextUtils.generateId(9);
        this.initId = initId;
        this.targetId = targetId;
        this.rate = rate;
    }

    public String getFormattedRate() {
        return (rate * 100) + "%";
    }
    
    public void save() {
        String filename = tariffId + ".yml";
        
        File file = new File(TARIFFS_PATH + filename);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
    
        yaml.set("initId", initId);
        yaml.set("targetId", targetId);
        yaml.set("rate", rate);
        
        try {
            yaml.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
            instance.getLogger().log(Level.SEVERE, "Unable to save tariff of ID " + tariffId + ".");
        }
        
        instance.logInfo("Saved tariff " + tariffId + " to files.");
    }
    
    public boolean delete() {
        File file = new File(TARIFFS_PATH + tariffId + ".yml");
        return file.delete();
    }
    
    // --------------------------------------------------------------------------------------------
    
    public static List<Tariff> getAll() {
        File directory = new File(TARIFFS_PATH);
        if (directory.list() == null) {
            directory.mkdirs();
        }
        
        List<Tariff> tariffs = new ArrayList<>();
        List<String> pathnames = List.of(Objects.requireNonNull(new File(TARIFFS_PATH).list()));
        for (String pathname : pathnames) {
            tariffs.add(Tariff.get(pathname.substring(0, pathname.length() - 4)));
        }
        return tariffs;
    }
    
    public static Tariff get(String tariffId) {
        String filename = tariffId + ".yml";
        File file = new File(TARIFFS_PATH + filename);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        return new Tariff(
                tariffId,
                yaml.getString("initId"),
                yaml.getString("targetId"),
                yaml.getDouble("rate")
        );
    }
    
    public static @Nullable String getTariffId(String initNation, String targetNation) {
        for (Tariff t : Tariff.getAll()) {
            boolean fromCond = Objects.equals(t.getInitId(),
                    Objects.requireNonNull(Nation.getFromName(initNation)).getNationId());
            boolean toCond = Objects.equals(t.getTargetId(),
                    Objects.requireNonNull(Nation.getFromName(targetNation)).getNationId());
            if (fromCond && toCond) return t.getTariffId();
        }
        return null;
    }
}

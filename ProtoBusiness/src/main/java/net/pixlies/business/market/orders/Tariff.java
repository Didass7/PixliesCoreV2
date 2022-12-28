package net.pixlies.business.market.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.utils.TextUtils;
import net.pixlies.nations.nations.Nation;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Tariff class.
 * Փիւսկիլլիւ պելա․․․
 *
 * @author vyketype
 */
@Getter
@AllArgsConstructor
public class Tariff {
    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private static final String TARIFFS_PATH = instance.getDataFolder().getAbsolutePath() + "/tariffs/";
    
    private final String tariffId;
    private final Type type;
    private final String initId;
    private final String targetId;
    
    @Setter
    private double rate;
    
    public Tariff(Type type, String initId, String targetId, double rate) {
        tariffId = TextUtils.generateId(7);
        this.type = type;
        this.initId = initId;
        this.targetId = targetId;
        this.rate = rate;
    }

    public String getFormattedRate() {
        return (rate * 100) + "%";
    }
    
    public void save() {
        String filename = tariffId + ".yml";
        Config file = new Config(new File(TARIFFS_PATH + filename), filename);
        file.set("type", type.toString());
        file.set("initId", initId);
        file.set("targetId", targetId);
        file.set("rate", rate);
        file.save();
    }
    
    public boolean delete() {
        File file = new File(TARIFFS_PATH + tariffId + ".yml");
        return file.delete();
    }
    
    public static List<Tariff> getAll() {
        List<Tariff> tariffs = new ArrayList<>();
        List<String> pathnames = List.of(Objects.requireNonNull(new File(TARIFFS_PATH).list()));
        for (String pathname : pathnames) {
            tariffs.add(Tariff.get(pathname.substring(0, pathname.length() - 4)));
        }
        return tariffs;
    }
    
    public static Tariff get(String tariffId) {
        String filename = tariffId + ".yml";
        Config file = new Config(new File(TARIFFS_PATH + filename), filename);
        return new Tariff(
                filename,
                Type.valueOf(file.getString("type")),
                file.getString("initId"),
                file.getString("targetId"),
                file.getDouble("rate")
        );
    }
    
    public static @Nullable String getTariffId(String initNation, String targetNation, Type type) {
        for (Tariff t : Tariff.getAll()) {
            boolean fromCond = Objects.equals(t.getInitId(),
                    Objects.requireNonNull(Nation.getFromName(initNation)).getNationId());
            boolean toCond = Objects.equals(t.getTargetId(),
                    Objects.requireNonNull(Nation.getFromName(targetNation)).getNationId());
            if (fromCond && toCond && type == t.getType()) return t.getTariffId();
        }
        return null;
    }
    
    @Getter
    @AllArgsConstructor
    public enum Type {
        IMPORTS("6"),
        EXPORTS("c");
        
        private final String color;
    }
}

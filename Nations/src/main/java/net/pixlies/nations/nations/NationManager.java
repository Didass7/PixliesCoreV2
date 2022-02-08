package net.pixlies.nations.nations;

import com.google.gson.Gson;
import net.pixlies.core.Main;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class NationManager {

    private static final Main instance = Main.getInstance();

    // Saves the nation object with its id
    public static Map<String, Nation> nations;

    public static void init() {
        nations = new HashMap<>();

        Gson gson = new Gson();

        for (Document d : instance.getDatabase().getNationCollection().find()) {
            Nation nation = gson.fromJson(d.toJson(), Nation.class);
            if (nation.getId() != null) {
                nations.put(nation.getId(), nation);
            }
        }
    }

}
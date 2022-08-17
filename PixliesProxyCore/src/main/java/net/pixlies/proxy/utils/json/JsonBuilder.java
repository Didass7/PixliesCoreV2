package net.pixlies.proxy.utils.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonBuilder {

    private final JsonObject jsonObject;

    public JsonBuilder() {
        this.jsonObject = new JsonObject();
    }

    public JsonBuilder(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JsonBuilder addProperty(String property, String value) {
        jsonObject.addProperty(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, Number value) {
        jsonObject.addProperty(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, Character value) {
        jsonObject.addProperty(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, Boolean value) {
        jsonObject.addProperty(property, value);
        return this;
    }

    public JsonBuilder add(String property, JsonElement value) {
        jsonObject.add(property, value);
        return this;
    }

    public JsonObject toJsonObject() {
        return jsonObject;
    }

}

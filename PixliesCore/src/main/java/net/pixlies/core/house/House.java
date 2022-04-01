package net.pixlies.core.house;

import lombok.Getter;

public enum House {

    BUILDER("§b§lB"),
    FARMER("§e§lF"),
    FIGHTER("§c§lF"),
    WIZARD("§d§lW"),
    POLITICIAN("§7§lP"),
    MINER("§3§lM"),

    NOT_DECIDED("§4§lN");

    private final @Getter String icon;

    House(String icon) {
        this.icon = icon;
    }

}

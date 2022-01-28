package net.pixlies.core.handlers.impl;

import lombok.Getter;
import lombok.Setter;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.Handler;

public class CosmeticsHandler implements Handler {

    private static final Main instance = Main.getInstance();

    @Getter @Setter private boolean suicidalDeath = false;

}

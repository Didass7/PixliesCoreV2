package net.pixlies.core.handlers.impl;

import lombok.Getter;
import lombok.Setter;
import net.pixlies.core.handlers.Handler;

public class GlobalPvpHandler implements Handler {

    @Getter @Setter private boolean globalPvpEnabled = true;

}

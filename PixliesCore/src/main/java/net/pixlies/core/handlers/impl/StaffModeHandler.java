package net.pixlies.core.handlers.impl;

import lombok.Getter;
import lombok.Setter;
import net.pixlies.core.handlers.Handler;

public class StaffModeHandler implements Handler {

    @Getter @Setter private boolean staffModeEnabled = false;

}

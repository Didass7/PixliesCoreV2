package net.pixlies.core.handlers.impl;

import lombok.Getter;
import net.pixlies.core.handlers.Handler;

import java.util.*;

public class PollHandler implements Handler {

    @Getter final List<String> pollIds = new ArrayList<>();

    @Getter final Map<String, UUID> pollsInCreation = new HashMap<>();

}

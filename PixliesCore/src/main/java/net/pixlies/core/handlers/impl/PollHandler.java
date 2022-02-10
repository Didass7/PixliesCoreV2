/**
 * @author vPrototype_
 */

package net.pixlies.core.handlers.impl;

import lombok.Getter;
import net.pixlies.core.entity.Poll;
import net.pixlies.core.handlers.Handler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PollHandler implements Handler {

    @Getter final Map<String, Poll> polls = new HashMap<>();

    @Getter final Map<UUID, Poll> pollsInCreation = new HashMap<>();

}

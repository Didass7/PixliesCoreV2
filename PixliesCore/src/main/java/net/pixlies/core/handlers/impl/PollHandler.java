/**
 * @author vyketype
 */

package net.pixlies.core.handlers.impl;

import lombok.Getter;
import lombok.Setter;
import net.pixlies.core.entity.polls.Poll;
import net.pixlies.core.handlers.Handler;
import org.jetbrains.annotations.Nullable;

public class PollHandler implements Handler {

    private @Getter @Setter @Nullable Poll activePoll;

}

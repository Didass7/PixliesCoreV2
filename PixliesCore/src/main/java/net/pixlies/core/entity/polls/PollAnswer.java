package net.pixlies.core.entity.polls;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PollAnswer {
    private String answer;
    private int votes;
}

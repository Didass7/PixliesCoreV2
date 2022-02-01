package events.impl.player;

import events.PixliesEventCancellable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class MessagePlayerEvent extends PixliesEventCancellable {

    private final CommandSender sender;
    private final Player target;

}

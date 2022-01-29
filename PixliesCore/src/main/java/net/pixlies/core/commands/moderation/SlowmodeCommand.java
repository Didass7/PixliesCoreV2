package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.SlowmodeHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;

@CommandAlias("slowmode")
@CommandPermission("pixlies.moderation.slowmode")
public class SlowmodeCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final SlowmodeHandler slowmodeHandler = instance.getHandlerManager().getHandler(SlowmodeHandler.class);

    @Default
    @Description("Sets a cooldown for the chat")
    public void onSlowmode(CommandSender sender, String seconds) {
        if (seconds == null) {
            Lang.INVALID_COMMAND_SYNTAX.send(sender, "%REASON%;/slowmode <seconds>");
            return;
        } else if (!isNumeric(seconds) || Long.parseLong(seconds) > 60 || Long.parseLong(seconds) < 0) {
            Lang.SLOWMODE_MUST_BE_NUMERIC_VALUE.send(sender);
            return;
        } else if (Long.parseLong(seconds) == 0L && slowmodeHandler.getSlowmode() == 0L) {
            Lang.SLOWMODE_IS_ALREADY_OFF.send(sender);
            return;
        }
        slowmodeHandler.setSlowmode(Long.valueOf(seconds));
        if (slowmodeHandler.getSlowmode() == 0L) {
            Lang.SLOWMODE_OFF.broadcast("%PLAYER%;" + sender.getName());
        } else {
            Lang.SLOWMODE_ON.broadcast("%PLAYER%;" + sender.getName(), "%SECONDS%;" +
                    slowmodeHandler.getSlowmode());
        }
    }

    private static boolean isNumeric(String str) {
        int size = str.length();
        for (int i = 0; i < size; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

}

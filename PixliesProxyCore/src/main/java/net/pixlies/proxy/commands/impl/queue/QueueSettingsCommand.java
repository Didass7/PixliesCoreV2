package net.pixlies.proxy.commands.impl.queue;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.md_5.bungee.api.CommandSender;
import net.pixlies.proxy.PixliesProxy;
import net.pixlies.proxy.localization.Lang;
import net.pixlies.proxy.queue.Queue;
import net.pixlies.proxy.queue.QueueManager;

@CommandAlias("queuesettings|queueset")
@CommandPermission("pixlies.queue.queuesettings")
public class QueueSettingsCommand extends BaseCommand {

    private static final PixliesProxy instance = PixliesProxy.getInstance();
    private static final QueueManager manager = instance.getQueueManager();

    @Subcommand("pause")
    @Syntax("<queue>")
    @CommandCompletion("Earth")
    @Description("Pauses a queue.")
    public void onPause(CommandSender sender, @Single String queueName) {
        Queue queue = manager.getQueue(queueName);
        if (queue == null) {
            Lang.PLAYER_QUEUE_NOT_EXIST.send(sender, "%SERVER%;" + queueName);
            return;
        }

        queue.setPaused(true);
        Lang.PLAYER_QUEUE_SETTINGS_COMMAND_PAUSE_ENABLED.send(sender, "%SERVER%;" + queue.getName());
    }

    @Subcommand("resume")
    @Syntax("<queue>")
    @CommandCompletion("Earth")
    @Description("Resumes a queue.")
    public void onResume(CommandSender sender, @Single String queueName) {
        Queue queue = manager.getQueue(queueName);
        if (queue == null) {
            Lang.PLAYER_QUEUE_NOT_EXIST.send(sender, "%SERVER%;" + queueName);
            return;
        }

        queue.setPaused(false);
        Lang.PLAYER_QUEUE_SETTINGS_COMMAND_PAUSE_DISABLED.send(sender, "%SERVER%;" + queue.getName());
    }

    @Subcommand("limit")
    @Syntax("<queue> <limit>")
    @CommandCompletion("Earth")
    @Description("Limits the maximum amount of players for a server.")
    public void onLimit(CommandSender sender, String queueName, @Conditions("limits:min=-1,max=400") Integer maximum) {
        Queue queue = manager.getQueue(queueName);
        if (queue == null) {
            Lang.PLAYER_QUEUE_NOT_EXIST.send(sender, "%SERVER%;" + queueName);
            return;
        }

        queue.setLimit(maximum);
        Lang.PLAYER_QUEUE_SETTINGS_COMMAND_LIMIT_SET.send(sender, "%SERVER%;" + queue.getName(), "%LIMIT%;" + maximum);
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}

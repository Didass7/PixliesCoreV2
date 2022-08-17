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

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}

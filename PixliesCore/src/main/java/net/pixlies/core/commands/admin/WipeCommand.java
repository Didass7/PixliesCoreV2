package net.pixlies.core.commands.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.morphia.query.experimental.filters.Filters;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;

@CommandAlias("wipe")
@CommandPermission("pixlies.admin.wipe")
public class WipeCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();

    @Default
    @Description("Wipes the users database")
    public void onWipeUsers(CommandSender sender) {
        for (User user : instance.getDatabase().getDatastore().find(User.class).iterator().toList()) {
            String uuid = user.getUuid();
            instance.getDatabase().getDatastore().find(User.class).filter(Filters.gte("uuid", uuid)).delete();
        }
        Lang.WIPED_USERS.send(sender);
    }

}

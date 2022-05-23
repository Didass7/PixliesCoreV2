package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.business.market.Challenge;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

@CommandAlias("challenge|challenges")
@CommandPermission("pixlies.business.challenge")
public class ChallengeCommand extends BaseCommand {

    @Default
    @Description("Gets a list of all market challenges")
    public void onChallenge(Player player) {
        User user = User.get(player.getUniqueId());
        Lang.CHALLENGE_LIST.send(player);
        for (Challenge c : Challenge.values()) {
            if (user.getCompletedChallenges().contains(c)) {
                Lang.CHALLENGE_COMPLETE_FORMAT.send(player, "%CHALLENGE%;" + c.getMessage(player));
            } else {
                Lang.CHALLENGE_INCOMPLETE_FORMAT.send(player, "%CHALLENGE%;" + c.getMessage(player));
            }
        }
    }

}

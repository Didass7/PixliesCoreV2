package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.market.orders.Tariff;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.ranks.NationPermission;
import org.bukkit.entity.Player;

import java.util.Objects;

@CommandAlias("tariff|tariffs|trf")
@CommandPermission("pixlies.business.tariff")
public class TariffCommand extends BaseCommand {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    @Subcommand("local")
    @Description("Retrieve the list of incoming and outgoing tariffs for your nation")
    public void onTariffLocal(Player player) {
        if (!NationProfile.isInNation(User.get(player.getUniqueId()))) return;

    }

    @Subcommand("global")
    @Description("Retrieve the list of all incoming and outgoing tariffs")
    public void onTariffGlobal(Player player) {

    }

    @Subcommand("set")
    @Description("Set a tariff for a nation")
    public void onTariffSet(Player player, String to, double rate) {
        User user = User.get(player.getUniqueId());
        if (!NationProfile.isInNation(user)) return;

        if (NationPermission.MANAGE_TARIFFS.hasNationPermission(user) || user.getSettings().isBypassing()) {
            String fromId = Objects.requireNonNull(NationProfile.get(user)).getNationId();
            String toId = Objects.requireNonNull(Nation.getFromName(to)).getNationsId();
            Tariff tariff = new Tariff(fromId, toId, rate);

            instance.getMarketManager().getTariffs().put(tariff.getTariffId(), tariff);
            Lang.TARIFF_SET.send(player, "%NATION%;" + to, "%RATE%;" + rate);
            // TODO: nation wide message
            // TODO: impacted nation wide message
        } else {
            Lang.NATION_NO_PERMISSION.send(player);
            player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
        }
    }

    @Subcommand("remove")
    @Description("Remove a tariff from a nation")
    public void onTariffRemove(Player player, String to) {
        User user = User.get(player.getUniqueId());
        if (!NationProfile.isInNation(user)) return;

        if (NationPermission.MANAGE_TARIFFS.hasNationPermission(user) || user.getSettings().isBypassing()) {
            String from = Objects.requireNonNull(NationProfile.get(user)).getNation().getName();

            if (instance.getMarketManager().getTariffId(from, to) == null) {
                Lang.TARIFF_DOES_NOT_EXIST.send(player);
                player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
                return;
            }

            instance.getMarketManager().getTariffs().remove(instance.getMarketManager().getTariffId(from, to));
            Lang.TARIFF_REMOVED.send(player, "%NATION%;" + to);
            // TODO: nation wide message
            // TODO: impacted nation wide message
        } else {
            Lang.NATION_NO_PERMISSION.send(player);
            player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
        }
    }

    @Default
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}

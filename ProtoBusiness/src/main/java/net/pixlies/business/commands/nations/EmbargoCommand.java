package net.pixlies.business.commands.nations;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.business.ProtoBusinesss;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.Embargo;
import net.pixlies.business.threads.EmbargoExpirationThread;
import net.pixlies.business.util.preconditions.CommandPreconditions;
import net.pixlies.core.utils.TextUtils;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

/**
 * Embargo command.
 *
 * @author vyketype
 */
@CommandAlias("embargo")
@CommandPermission("pixlies.business.embargo")
public class EmbargoCommand extends BaseCommand {
    private static final ProtoBusinesss instance = ProtoBusinesss.getInstance();
    
    @Subcommand("listlocal")
    @Description("Retrieve the list of embargoes which concern your nation")
    public void onEmbargoLocal(Player player) {
    
    }
    
    @Subcommand("listglobal")
    @Description("Retrieve the list of all embargoes")
    public void onEmbargoGlobal(Player player, @Optional Integer page) {
    
    }
    
    @Subcommand("add")
    @Syntax("<nation> [durationHours]")
    @Description("Embargo a nation")
    public void onEmbargoAdd(Player player, String strArgs) {
        String[] args = StringUtils.split(strArgs, " ", -1);
        NationProfile nationProfile = NationProfile.get(player.getUniqueId());
    
        if (!CommandPreconditions.embargoSet(player, args[0]))
            return;
    
        String targetId = Objects.requireNonNull(Nation.getFromName(args[0])).getNationId();
        int duration = args.length > 1 ? CommandPreconditions.getValidDuration(player, args[1]) : -1;
        Embargo embargo = new Embargo(TextUtils.generateId(9), nationProfile.getNationId(), targetId, duration);
        embargo.save();
        
        String strDuration = duration == -1 ? "(no expiration)" : duration + "h";
    
        // Send message to the players of the initial nation
        for (UUID uuid : Objects.requireNonNull(nationProfile.getNation()).getMembers()) {
            MarketLang.INCOMING_EMBARGO_ADDED.send(
                    Objects.requireNonNull(Bukkit.getPlayer(uuid)),
                    "%NATION%;" + args[0],
                    "%DURATION%;" + strDuration
            );
        }
    
        // Send message to the players of the target nation
        for (UUID uuid : Objects.requireNonNull(Nation.getFromName(args[0])).getMembers()) {
            MarketLang.OUTGOING_EMBARGO_ADDED.send(
                    Objects.requireNonNull(Bukkit.getPlayer(uuid)),
                    "%NATION%;" + nationProfile.getNation().getName(),
                    "%DURATION%;" + strDuration
            );
        }
    
        if (duration != -1) {
            EmbargoExpirationThread.EMBARGO_EXPIRATIONS.put(embargo.embargoId(), embargo.duration() * 60);
        }
    }
    
    @Subcommand("remove")
    @Syntax("<nation>")
    @Description("Remove an embargo from a nation")
    public void onEmbargoRemove(Player player, String targetNation) {
        NationProfile nationProfile = NationProfile.get(player.getUniqueId());
    
        if (!CommandPreconditions.embargoRemove(player, targetNation))
            return;
    
        String embargoId = Embargo.getEmbargoId(nationProfile.getNation().getName(), targetNation);
        Embargo embargo = Embargo.get(embargoId);
    
        if (!CommandPreconditions.isEmbargoDeleteSuccessful(player, embargo))
            return;
        
        embargo.sendDeletionMessages();
        EmbargoExpirationThread.EMBARGO_EXPIRATIONS.remove(embargoId);
    }
    
    @Default
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }
}

package net.pixlies.nations.commands.impl.nations;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import net.pixlies.core.entity.user.User;
import net.pixlies.nations.interfaces.NationProfile;
import net.pixlies.nations.locale.Lang;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.chunk.NationChunk;
import net.pixlies.nations.nations.chunk.NationChunkType;
import net.pixlies.nations.nations.ranks.NationPermission;
import net.pixlies.nations.utils.NationTextUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@CommandAlias("%nationscommand")
@Subcommand("claim")
public class NationClaimCommand extends BaseCommand {

    // -------------------------------------------------------------------------------------------------
    //                                         /n claim one
    // -------------------------------------------------------------------------------------------------
    @Subcommand("one")
    @Description("Claim one chunk")
    public void onClaim(Player player, @Optional String nationName) {
        User user = User.get(player.getUniqueId());
        NationProfile profile = NationProfile.get(player.getUniqueId());

        boolean staffCondition = user.isBypassing() && player.hasPermission("nations.staff.forceclaim");
        boolean playerCondition = NationPermission.CLAIM.hasPermission(player);

        // :: /nation claim one
        if (nationName == null || nationName.isEmpty()) {

            // NOT IN NATION
            if (!profile.isInNation()) {
                Lang.NOT_IN_NATION.send(player);
                return;
            }

            if (!(staffCondition || playerCondition)) {
                // NO PERMISSION TO DISBAND
                Lang.NATION_NO_PERMISSION.send(player);
                return;
            }

            // STAFF OR PLAYER DISBAND
            Nation nation = profile.getNation();
            if (nation == null) {
                Lang.NOT_IN_NATION.send(player);
                return;
            }

            if (NationChunk.getClaimAt(player.getWorld().getName(), player.getChunk().getX(), player.getChunk().getZ()) != null) {
                Lang.NATION_CLAIM_ALREADY_CLAIMED.send(player);
                return;
            }

            NationChunk chunk = new NationChunk(
                    nation.getNationId(),
                    player.getWorld().getName(),
                    player.getChunk().getX(),
                    player.getChunk().getZ(),
                    NationChunkType.NORMAL,
                    new ArrayList<>()
            );
            chunk.claim(true);
            nation.backup();

            Lang.NATION_CLAIM_MESSAGE.send(player,
                    "%LOCATION%;" + NationTextUtils.getChunkLocationFormatted(chunk.getX(), chunk.getZ()),
                    "%NATION%;" + nation.getName()
            );

            nation.getOnlineMembersAsPlayer().forEach((p) -> {
                if (player.equals(p)) return;
                Lang.NATION_CLAIM_MESSAGE.send(p,
                        "%PLAYER%;" + player.getName(),
                        "%LOCATION%;" + NationTextUtils.getChunkLocationFormatted(chunk.getX(), chunk.getZ())
                );
            });

            return;

        }

        // :: /nation claim one <NATION>
        if (!staffCondition) {
            Lang.NATION_NO_PERMISSION.send(player);
            return;
        }

        Nation nation = Nation.getFromName(nationName);
        if (nation == null) {
            Lang.NATION_DOES_NOT_EXIST.send(player);
            return;
        }

        if (NationChunk.getClaimAt(player.getWorld().getName(), player.getChunk().getX(), player.getChunk().getZ()) != null) {
            Lang.NATION_CLAIM_ALREADY_CLAIMED.send(player);
            return;
        }

        NationChunk chunk = new NationChunk(
                nation.getNationId(),
                player.getWorld().getName(),
                player.getChunk().getX(),
                player.getChunk().getZ(),
                NationChunkType.NORMAL,
                new ArrayList<>()
        );
        chunk.claim(true);
        nation.backup();

        Lang.NATION_CLAIM_MESSAGE.send(player,
                "%LOCATION%;" + NationTextUtils.getChunkLocationFormatted(chunk.getX(), chunk.getZ()),
                "%NATION%;" + nation.getName()
        );

        nation.getOnlineMembersAsPlayer().forEach((p) -> {
            if (player.equals(p)) return;
            Lang.NATION_CLAIM_MESSAGE.send(p,
                    "%PLAYER%;" + player.getName(),
                    "%LOCATION%;" + NationTextUtils.getChunkLocationFormatted(chunk.getX(), chunk.getZ())
            );
        });

    }

}

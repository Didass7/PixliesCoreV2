package net.pixlies.core.handlers.impl;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.handlers.Handler;
import net.pixlies.core.ranks.Rank;
import net.pixlies.core.scoreboard.PixliesTabAdapter;
import net.pixlies.core.utils.CC;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class TabListHandler implements Handler {

    private static final Main instance = Main.getInstance();
    private static final Config config = instance.getConfig();
    private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    private @Getter @Nullable PixliesTabAdapter adapter;

    public void load(PixliesTabAdapter adapter) {
        this.adapter = adapter;
    }

    public void unload() {
        adapter = null;
    }

    public boolean isLoaded() {
        return adapter != null;
    }

    @SuppressWarnings("deprecation") // Legacy message support
    public void formatTabListFor(Player player) {

        if (adapter == null) {
            return;
        }

        // HEADER
        StringJoiner headerJoiner = new StringJoiner("\n");
        for (String line : adapter.getHeader(player)) {
            if (line == null) continue;
            headerJoiner.add(CC.format("&r" + line));
        }
        player.setPlayerListHeader(headerJoiner.toString());

        // FOOTER
        StringJoiner footerJoiner = new StringJoiner("\n");
        for (String line : adapter.getFooter(player)) {
            if (line == null) continue;
            footerJoiner.add(CC.format("&r" + line));
        }
        player.setPlayerListFooter(footerJoiner.toString());
    }

    @SuppressWarnings("deprecation") // Legacy message support
    public void updateDisplayNames() {
        for (Player player : instance.getServer().getOnlinePlayers()) {
            User user = User.get(player.getUniqueId());
            boolean isStaffStuffEnabled = user.isPassive();
            player.setPlayerListName(isStaffStuffEnabled ? player.getName() : user.getNickName());
        }
    }

    public void sortTabList() {

        ConfigurationSection section = config.getConfigurationSection("tablist.ranks");
        if (section == null) return;

        Map<String, String> ranks = new HashMap<>();
        for (String rankName : section.getKeys(false)) {
            String rankOrder = section.getString(rankName + ".order", "");
            ranks.put(rankName, rankOrder);
        }

        for (Player player : instance.getServer().getOnlinePlayers()) {
            final Scoreboard scoreboard = player.getScoreboard();

            scoreboard.getTeams().forEach(team -> {
                if (team.getName().startsWith("tab_")) {
                    for (Player target : instance.getServer().getOnlinePlayers()) {
                        team.removePlayer(target);
                    }
                }
            });

            for (Player target : instance.getServer().getOnlinePlayers()) {
                String group = PlaceholderAPI.setPlaceholders(target, "%luckperms_primary_group_name%");
                String teamName = "tab_" + ranks.getOrDefault(group, "");

                Team team = scoreboard.getTeam(teamName);
                if (team == null) {
                    team = scoreboard.registerNewTeam(teamName);
                }

                if (!team.hasPlayer(player)) {
                    team.addPlayer(player);
                }

            }
            player.setScoreboard(scoreboard);
        }
    }

    public String getTabListPrefix(Player player, boolean isStaff) {
        User user = User.get(player.getUniqueId());
        String prefix = Rank.getRank(player.getUniqueId()).getChatPrefix();
        if (isStaff) {
            if (user.isInStaffMode() && player.hasPermission("pixlies.moderation.staffmode")) {
                prefix = "&7&lSTAFF ";
            } else if (user.isVanished()) {
                prefix = "&7&lVANISH ";
            }
        }
        return prefix;
    }

    public String getTabListSuffix(Player player, boolean isStaff) {
        User user = User.get(player.getUniqueId());
        return PlaceholderAPI.setPlaceholders(player, "%luckperms_suffix%");
    }

}

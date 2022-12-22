package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.orders.OrderProfile;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.moderation.PunishmentType;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

/**
 * Market command.
 *
 * @author vyketype
 */
@CommandAlias("market|m|nasdaq|nyse|snp500|dowjones|ftse")
@CommandPermission("pixlies.business.market")
public class MarketCommand extends BaseCommand {
    /*
     * SOUNDS
     * - placed new order: block.amethyst_block.break
     * - invalid/error/restricted: block.anvil.land
     * - claimed goods/unrestricted: entity.experience_orb.pickup
     * - cancelled order: block.netherite_block.place
     */
    private static final Main pixlies = Main.getInstance();
    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);
    
    @Default
    @Description("Opens the market menu")
    public void onMarket(Player player) {
        if (!instance.getConfig().getBoolean("marketOpen")) {
            MarketLang.MARKET_IS_CLOSED.send(player);
            player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
            return;
        }
        
        User user = User.get(player.getUniqueId());
        if (user.getActivePunishmentByType(PunishmentType.MARKET_RESTRICT) != null) {
            MarketLang.MARKET_PLAYER_IS_RESTRICTED.send(player);
            player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
            return;
        }
        
        UUID uuid = player.getUniqueId();
        marketHandler.getProfiles().put(uuid.toString(), new OrderProfile(uuid));
        
        Objects.requireNonNull(OrderProfile.get(player.getUniqueId())).openMarketPage();
    }
    
    @Subcommand("open")
    @CommandPermission("pixlies.business.market.gates")
    @Description("Opens the market to the public")
    public void onMarketOpen(Player player) {
        if (instance.getConfig().getBoolean("marketOpen")) {
            MarketLang.MARKET_WAS_ALREADY_OPEN.send(player);
            return;
        }
        instance.getConfig().set("marketOpen", true);
        MarketLang.MARKET_OPEN.broadcast();
    }
    
    @Subcommand("close")
    @CommandPermission("pixlies.business.market.gates")
    @Description("Closes the market")
    public void onMarketClose(Player player) {
        if (!instance.getConfig().getBoolean("marketOpen")) {
            MarketLang.MARKET_WAS_ALREADY_CLOSED.send(player);
            return;
        }
        instance.getConfig().set("marketOpen", false);
        MarketLang.MARKET_CLOSED.broadcast();
    }
    
    @Subcommand("resetstats")
    @CommandPermission("pixlies.business.market.resetstats")
    @Description("Resets the market statistics")
    public void onMarketReset(Player player, @Optional Player target) {
        if (target == null) {
            instance.getMarketManager().resetBooks();
            MarketLang.MARKET_STATISTICS_RESET.broadcast("%PLAYER%;" + player.getName());
            player.playSound(player.getLocation(), "entity.experience_orb.pickup", 100, 1);
        } else {
            if (target.isOnline()) {
                instance.getMarketManager().resetPlayer(target);
                MarketLang.MARKET_PLAYER_STATISTICS_RESET.send(target, "%PLAYER%;" + target.getName(),
                        "%SENDER%;" + player.getName());
                target.playSound(target.getLocation(), "entity.experience_orb.pickup", 100, 1);
            } else {
                Lang.PLAYER_DOESNT_EXIST.send(player);
            }
        }
    }
    
    @Subcommand("restrict")
    @CommandPermission("pixlies.business.market.restrict")
    @Description("Restricts/unrestricts a player from accessing the market")
    public void onMarketRestrict(Player player, Player target, @Optional String reason) {
        User user = User.get(target.getUniqueId());
        
        reason = reason.substring(target.getName().length());
        if (reason.equals("")) {
            reason = pixlies.getConfig().getString("moderation.defaultReason", "No reason given");
        }
        
        if (user.getActivePunishmentByType(PunishmentType.MARKET_RESTRICT) != null) {
            if (target.isOnline()) {
                MarketLang.MARKET_PLAYER_ALLOWED_TARGET.send(target);
                target.playSound(target.getLocation(), "entity.experience_orb.pickup", 100, 1);
            }
            MarketLang.MARKET_PLAYER_ALLOWED_SENDER.send(player, "%PLAYER%;" + target.getName());
//            user.unRestrict(); TODO
        } else {
            if (target.isOnline()) {
                MarketLang.MARKET_PLAYER_RESTRICTED_TARGET.send(target, "%PLAYER%;" + player.getName(),
                        "%REASON%;" + reason);
                target.playSound(target.getLocation(), "block.anvil.land", 100, 1);
            }
            MarketLang.MARKET_PLAYER_RESTRICTED_SENDER.send(player, "%PLAYER%;" + target.getName(),
                    "%REASON%;" + reason);
//            user.marketRestrict(player, reason);
        }
    }
    
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }
    
    @Getter
    @AllArgsConstructor
    public enum Selection {
        MINERALS(Material.DIAMOND_PICKAXE, "§b", false, true, false),
        FOODSTUFFS_AND_PLANTS(Material.GOLDEN_HOE, "§e", true, true, true),
        BLOCKS(Material.IRON_SHOVEL, "§d", true, true, true),
        MOB_DROPS(Material.NETHERITE_SWORD, "§c", false, true, false),
        MISCELLANEOUS(Material.ARROW, "§6", false, false, false);
        
        private final Material material;
        private final String color;
        private final boolean seventhColumn;
        private final boolean fourthRow;
        private final boolean fifthRow;
        
        public boolean hasSeventhColumn() {
            return seventhColumn;
        }
        
        public boolean hasFourthRow() {
            return fourthRow;
        }
        
        public boolean hasFifthRow() {
            return fifthRow;
        }
        
        public String getName() {
            return WordUtils.capitalize(name().toLowerCase().replace("_", " "));
        }
    }
}

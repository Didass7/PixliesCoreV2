package net.pixlies.business.market;

import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.ProtoBusiness;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Market profile.
 *
 * @author vyketype
 */
@Getter
public class MarketProfile {
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      
      private final UUID uuid;
      private final List<UUID> blockedPlayers;
      
      @Setter
      private boolean restricted;
      
      public MarketProfile(UUID uuid) {
            this.uuid = uuid;
            blockedPlayers = new ArrayList<>();
            restricted = false;
      }
      
      public void tradeBlockPlayer(UUID uuid) {
            blockedPlayers.add(uuid);
      }
      
      public void unTradeBlockPlayer(UUID uuid) {
            blockedPlayers.remove(uuid);
      }
      
      public void save() {
            // TODO: save to file system
      }
      
      public static MarketProfile get(UUID uuid) {
            // TODO: load from file system
            
            MarketProfile profile = new MarketProfile(uuid);
            profile.save();
            return profile;
      }
}

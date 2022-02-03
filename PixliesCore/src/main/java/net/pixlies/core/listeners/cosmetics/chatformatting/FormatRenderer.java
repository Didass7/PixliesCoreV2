package net.pixlies.core.listeners.cosmetics.chatformatting;

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.pixlies.core.entity.User;
import net.pixlies.core.utils.CC;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

// edit this to add proper chat formatting
public class FormatRenderer implements ChatRenderer {

    @Override
    public @NotNull Component render(@NotNull Player player, @NotNull Component playerDisplayName, @NotNull Component message, @NotNull Audience viewer) {

        User user = User.get(player.getUniqueId());
        Component nickName = Component.text(user.getNickName());

        // &fplayer&7: &fmessage
        return Component.text(CC.format("&f"))
                .append(nickName)
                .append(Component.text(CC.format("&7: &f")))
                .append(message);

    }

}

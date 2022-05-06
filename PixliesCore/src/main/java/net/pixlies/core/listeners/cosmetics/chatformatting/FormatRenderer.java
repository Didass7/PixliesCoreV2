package net.pixlies.core.listeners.cosmetics.chatformatting;

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.utils.CC;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

// edit this to add proper chat formatting
public class FormatRenderer implements ChatRenderer {

    @Override
    public @NotNull Component render(@NotNull Player player, @NotNull Component playerDisplayName, @NotNull Component message, @NotNull Audience viewer) {

        User user = User.get(player.getUniqueId());
        Component nickName = Component.text(user.getNickName());

        String prefix = "";
        String suffix = "";

        // Add tags support
        for (PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
            final String permission = perm.getPermission();

            // check prefix and set prefix
            if (permission.startsWith("pixlies.chat.meta.prefix.")) {
                prefix = permission.replaceFirst("pixlies\\.chat\\.meta\\.prefix.", "");
                continue;
            }

            // check suffix and set suffix
            if (permission.startsWith("pixlies.chat.meta.suffix.")) {
                suffix = permission.replaceFirst("pixlies\\.chat\\.meta\\.suffix.", "");
            }

        }

        // &x??&fplayer&7: &fmessage
        return Component.text(CC.format("&f"))
                .append(Component.text(CC.format(prefix)))
                .append(nickName)
                .append(Component.text(CC.format(suffix)))
                .append(Component.text(CC.format("&7: &f")))
                .append(message);

    }

}

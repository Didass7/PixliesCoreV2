package net.pixlies.core.moderation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.core.Main;
import net.pixlies.core.utils.PlayerUtils;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@AllArgsConstructor
public class Punishment {

    private final @Getter UUID punishmentId;
    private final @Getter PunishmentType type;
    private final @Getter UUID punishedBy;
    private @Getter @Setter long punishedAt;
    private @Getter @Setter long duration;
    private @Getter @Setter String reason;

    private @Getter @Nullable UUID removedBy;
    private @Getter long removedAt;
    private @Getter boolean removed;

    public Punishment(Document document) {
        this(
                UUID.fromString(document.getString("punishmentId")),
                PunishmentType.valueOf(document.getString("type")),
                UUID.fromString(document.getString("punishedBy")),
                document.getLong("punishedAt"),
                document.getLong("duration"),
                document.getString("reason"),

                document.get("removedBy", UUID.class),
                document.get("removedAt", 0L),
                document.get("removed", false)
        );
    }

    public void remove(UUID by, long at) {
        removedBy = by;
        removedAt = at;
        removed = true;
    }

    public boolean isPermanent() {
        return duration == 0;
    }

    public boolean isExpired() {
        if (removed) return true;
        if (isPermanent()) return false;
        return punishedAt + duration <= System.currentTimeMillis();
    }

    public boolean isActive() {
        if (removed) return false;
        if (isPermanent()) return true;
        return punishedAt + duration > System.currentTimeMillis();
    }

    public boolean isPunishedByConsole() {
        return PlayerUtils.getConsoleUUID().equals(punishedBy);
    }

    public long getUntil() {
        return punishedAt + duration;
    }

    public Document toDocument() {
        Document document = new Document();

        document.put("punishmentId", punishmentId.toString());
        document.put("type", type.name());
        document.put("punishedBy", punishedBy.toString());
        document.put("punishedAt", punishedAt);
        document.put("duration", duration);
        document.put("reason", reason);

        document.put("removedBy", removedBy);
        document.put("removedAt", removedAt);
        document.put("removed", removed);

        return document;
    }

}

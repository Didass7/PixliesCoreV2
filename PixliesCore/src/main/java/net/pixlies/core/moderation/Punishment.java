package net.pixlies.core.moderation;

import dev.morphia.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Entity
public class Punishment {

    private @Getter String punishmentId;
    private String type;
    private @Getter String punisher;
    private @Getter String punisherName;
    private @Getter long datePunished;
    private @Getter String reason;
    private @Getter long until;

    public UUID getPunisherId() {
        return UUID.fromString(punisher);
    }

    public PunishmentType getType() {
        return PunishmentType.valueOf(type);
    }

    public boolean isPermanent() {
        return until == 0;
    }

    public boolean isExpired() {
        if (isPermanent()) return false;
        if (getUntil() == 0) return false;
        return System.currentTimeMillis() - getUntil() <= 0;
    }

}

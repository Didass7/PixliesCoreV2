package net.pixlies.core.moderation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.Document;

import java.util.UUID;

@AllArgsConstructor
public class Punishment {

    private @Getter String punishmentId;
    private String type;
    private @Getter String punisher;
    private @Getter String punisherName;
    private @Getter long datePunished;
    private @Getter String reason;
    private @Getter long until;

    public Punishment(Document document) {
        this(
                document.getString("punishmentId"),
                document.getString("type"),
                document.getString("punisher"),
                document.getString("punisherName"),
                document.getLong("datePunished"),
                document.getString("reason"),
                document.getLong("until")
        );
    }

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

    public Document toDocument() {
        Document document = new Document();

        document.put("punishmentId", punishmentId);
        document.put("type", type);
        document.put("punisher", punisher);
        document.put("punisherName", punisherName);
        document.put("datePunished", datePunished);
        document.put("reason", reason);
        document.put("until", until);

        return document;
    }

}

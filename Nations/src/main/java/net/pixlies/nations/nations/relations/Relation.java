package net.pixlies.nations.nations.relations;

import lombok.Getter;
import net.pixlies.nations.locale.NationsLang;

public enum Relation {

    SAME(NationsLang.NATION_SAME_COLOUR.get()),
    ALLY(NationsLang.NATION_ALLY_COLOUR.get()),
    OTHER(NationsLang.NATION_OTHER_COLOUR.get());

    private final @Getter String color;

    Relation(String color) {
        this.color = color;
    }

}

package net.pixlies.core.entity.user.data;

import dev.morphia.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.core.Main;
import net.pixlies.core.house.House;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@Entity
public class UserStats {

    private String dateJoined; // In Pixlies DateAndTime
    private Map<House, Integer> houses; // House & House XPs
    private int civilPoints; // Range: -100 to 100

    private int buyOrdersMade;
    private int sellOrdersMade;
    private int tradesMade;
    private double moneySpent;
    private double moneyGained;
    private int itemsSold;
    private int itemsBought;

    public static UserStats createNew() {
        return new UserStats(
                Main.getInstance().getCalendar().formatDateAndTime(),
                new HashMap<>(){{
                    for (House house : House.values())
                        put(house, 0);
                }},
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
        );
    }

    public House getHouse() {
        int max = Collections.max(houses.values());

        if (max < 200) return House.NOT_DECIDED;

        for (Map.Entry<House, Integer> entry : houses.entrySet()) {
            if (entry.getValue()==max) {
                return entry.getKey();
            }
        }

        return House.NOT_DECIDED;
    }

    public void addBuy() {
        buyOrdersMade += 1;
    }

    public void addSell() {
        sellOrdersMade += 1;
    }

    public void addTrade() {
        tradesMade += 1;
    }

    public void addMoneySpent(double money) {
        moneySpent += money;
    }

    public void addMoneyGained(double money) {
        moneyGained += money;
    }

    public void addItemsSold(int items) {
        itemsSold += items;
    }

    public void addItemsBought(int items) {
        itemsBought += items;
    }

}

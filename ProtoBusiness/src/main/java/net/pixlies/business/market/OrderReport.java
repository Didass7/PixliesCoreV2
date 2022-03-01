package net.pixlies.business.market;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Easily keep track of all past orders
 *
 * @author vPrototype_
 * @author NeedlessMemeing
 */
@AllArgsConstructor
public class OrderReport {

    private final @Getter ArrayList<Trade> trades;
    private @Getter @Setter Order order;

    @Override
    public String toString() {
        StringBuilder retString = new StringBuilder("--- Order Report ---\n  Trades:\n");
        for (Trade t : trades) {
            retString.append("\n").append(t.toString());
        }
        retString.append("\nOrders:\n");
        retString.append(order.toString());
        return  retString + "\n--------------------------";
    }

}

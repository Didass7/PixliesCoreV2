package net.pixlies.business.market;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * Trade class
 *
 * @author vPrototype_
 * @author NeedlessMemeing
 */
@Getter
@AllArgsConstructor
public class Trade {

    private int timestamp;
    private double price;
    private int amount;

    private UUID provider; // for sell orders
    private UUID taker; // for sell orders
    private UUID buyer; // for buy orders
    private UUID seller; // for buy orders

    private String orderId;

}

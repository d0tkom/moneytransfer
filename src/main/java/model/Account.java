package model;

import java.math.BigDecimal;

public class Account {
    public final String id;
    public final BigDecimal balance;

    public Account(String id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }
}

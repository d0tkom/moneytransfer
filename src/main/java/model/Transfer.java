package model;

import java.math.BigDecimal;

public class Transfer {
    public final String id;
    public final String source;
    public final String target;
    public final BigDecimal amount;

    public Transfer(String id, String source, String target, BigDecimal amount) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.amount = amount;
    }
}

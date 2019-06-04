package model;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer that = (Transfer)o;
        return Objects.equals(id, that.id) &&
                Objects.equals(source, that.source) &&
                Objects.equals(target, that.target) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target, amount);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", source=" + source +
                ", target=" + target +
                ", amount=" + amount +
                "}";
    }
}

package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

// Model that we use to save transfers in our data store, and also to send back info about transfers
public class Transfer {
    public final String id;
    public final String source;
    public final String target;
    public final BigDecimal amount;
    public final LocalDateTime created;

    public Transfer(String id, String source, String target, BigDecimal amount, LocalDateTime created) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.amount = amount;
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer that = (Transfer)o;
        return Objects.equals(id, that.id) &&
                Objects.equals(source, that.source) &&
                Objects.equals(target, that.target) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target, amount, created);
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", source=" + source +
                ", target=" + target +
                ", amount=" + amount +
                ", created=" + created +
                "}";
    }
}

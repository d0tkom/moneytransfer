package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Account {
    public final String id;
    public final BigDecimal balance;
    public final LocalDateTime created;

    public Account(String id, BigDecimal balance, LocalDateTime created) {
        this.id = id;
        this.balance = balance;
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account that = (Account)o;
        return Objects.equals(id, that.id) && Objects.equals(balance, that.balance) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, created);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", created=" + created +
                "}";
    }
}

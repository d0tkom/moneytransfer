package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class AccountResponse extends  Account {
    public final BigDecimal balance;

    public AccountResponse(String id, LocalDateTime created, BigDecimal balance) {
        super(id, created);

        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountResponse that = (AccountResponse)o;
        return Objects.equals(id, that.id) && Objects.equals(balance, that.balance) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, created);
    }

    @Override
    public String toString() {
        return "AccountResponse{" +
                "id=" + id +
                ", balance=" + balance +
                ", created=" + created +
                "}";
    }
}

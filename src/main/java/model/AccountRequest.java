package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

// Model users use to create an account
public class AccountRequest {
    public final BigDecimal balance;

    public AccountRequest(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountRequest that = (AccountRequest)o;
        return Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance);
    }

    @Override
    public String toString() {
        return "AccountRequest{" +
                "balance=" + balance +
                "}";
    }
}

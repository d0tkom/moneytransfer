package model;

import java.time.LocalDateTime;
import java.util.Objects;

// Account model we actually store in our data store
public class Account {
    public final String id;
    public final LocalDateTime created;

    public Account(String id, LocalDateTime created) {
        this.id = id;
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account that = (Account)o;
        return Objects.equals(id, that.id) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", created=" + created +
                "}";
    }
}

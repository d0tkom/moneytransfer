package model;

import java.util.Objects;

public class Error {
    public final String message;

    public Error(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Error that = (Error)o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    @Override
    public String toString() {
        return "Error{" +
                "message=" + message +
                "}";
    }
}

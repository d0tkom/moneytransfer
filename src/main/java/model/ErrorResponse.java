package model;

import java.util.Objects;

// Wrapper for our error message
public class ErrorResponse {
    public final Error error;

    public ErrorResponse(String message) {
        Error error  = new Error(message);

        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponse that = (ErrorResponse)o;
        return Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error=" + error +
                "}";
    }
}

package handler;

import exception.AccountNotFoundException;
import exception.InsufficientFundsException;

public class ExceptionHandler {
    public spark.ExceptionHandler handleException() {
        return (e, req, res) -> {
            if (e instanceof InsufficientFundsException) {
                res.status(400);
            } else if (e instanceof AccountNotFoundException) {
                res.status(404);
            }

            res.body(e.getMessage());
        };
    }
}
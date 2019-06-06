package handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.AccountNotFoundException;
import exception.InsufficientFundsException;
import exception.TransferNotFoundException;
import model.ErrorResponse;
import org.eclipse.jetty.http.HttpStatus;

public class ErrorHandler {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public spark.ExceptionHandler handleException() {
        return (e, req, res) -> {
            if (e instanceof InsufficientFundsException || e instanceof IllegalArgumentException) {
                res.status(HttpStatus.BAD_REQUEST_400);
            } else if (e instanceof AccountNotFoundException || e instanceof TransferNotFoundException) {
                res.status(HttpStatus.NOT_FOUND_404);
            } else {
                res.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            }

            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            res.body(gson.toJson(errorResponse));
        };
    }
}
package handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import exception.AccountNotFoundException;
import exception.InsufficientFundsException;
import exception.TransferNotFoundException;
import model.ErrorResponse;
import org.eclipse.jetty.http.HttpStatus;

public class ExceptionHandler {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public spark.ExceptionHandler handleException() {
        return (e, req, res) -> {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            if (e instanceof InsufficientFundsException || e instanceof IllegalArgumentException) {
                res.status(HttpStatus.BAD_REQUEST_400);
            } else if (e instanceof AccountNotFoundException || e instanceof TransferNotFoundException) {
                res.status(HttpStatus.NOT_FOUND_404);
            } else if (e instanceof  JsonParseException) {
                errorResponse = new ErrorResponse("Could not parse json");
                res.status(HttpStatus.BAD_REQUEST_400);
            } else {
                res.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            }

            res.body(gson.toJson(errorResponse));
        };
    }
}
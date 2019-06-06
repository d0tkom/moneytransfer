package route;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import handler.AccountHandler;
import model.AccountRequest;
import spark.Request;
import spark.Route;

import java.math.BigDecimal;
import java.util.Optional;

public class AccountsRoute {
    private final AccountHandler accountHandler;
    private final Gson gson;

    public AccountsRoute(AccountHandler accountHandler) {
        this.accountHandler = accountHandler;

        gson = new Gson();
    }

    public Route getAccounts() {
        return (req, res) -> {
            res.type("application/json");

            return accountHandler.getAccounts();
        };
    }

    public Route postAccounts() {
        return (req, res) -> {
            AccountRequest accountRequest =  accountFromRequest(req);

            res.type("application/json");
            res.status(201);

            return accountHandler.createAccount(accountRequest.balance);
        };
    }

    public Route getAccountById() {
        return (req, res) -> {
            String id = req.params(":id");

            res.type("application/json");

            return accountHandler.getAccount(id);
        };
    }

    public Route getTransfersByAccountId() {
        return (req, res) -> {
            String id = req.params(":id");

            res.type("application/json");

            return accountHandler.getTransfersByAccountId(id);
        };
    }

    private AccountRequest accountFromRequest(Request req) {
        Optional<String> json = Optional.of(req.body());

        return json.map(this::fromJson).orElse(new AccountRequest(BigDecimal.ZERO));
    }

    private AccountRequest fromJson(String json) {
        try {
            return gson.fromJson(json, AccountRequest.class);
        } catch (JsonParseException e) {
            throw new IllegalArgumentException("Could not parse json");
        }
    }
}

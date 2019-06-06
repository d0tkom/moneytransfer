package route;

import com.google.gson.Gson;
import handler.AccountHandler;
import spark.Route;

import java.math.BigDecimal;

public class AccountsRoute {
    private final AccountHandler accountHandler;
    private final Gson gson;

    public AccountsRoute(AccountHandler accountHandler) {
        this.accountHandler = accountHandler;

        gson = new Gson();
    }

    public Route getAccounts() {
        return (req, res) -> accountHandler.getAccounts();
    }

    public Route postAccounts() {
        return (req, res) -> {
            BigDecimal balance =  gson.fromJson(req.body(), BigDecimal.class);

            return accountHandler.createAccount(balance);
        };
    }

    public Route getAccountById() {
        return (req, res) -> {
            String id = req.params(":id");

            return accountHandler.getAccount(id);
        };
    }
}

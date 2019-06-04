package route;

import handler.AccountHandler;
import spark.Route;

public class AccountsRoute {
    private final AccountHandler accountHandler;

    public AccountsRoute(AccountHandler accountHandler) {
        this.accountHandler = accountHandler;
    }

    public Route getAccounts() {
        return (req, res) -> accountHandler.getAccounts();
    }

    public Route getAccountById() {
        return (req, res) -> {
            String id = req.params(":id");

            return accountHandler.getAccount(id);
        };
    }
}

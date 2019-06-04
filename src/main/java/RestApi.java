import db.DataStore;
import handler.AccountHandler;
import handler.TransferHandler;
import route.AccountsRoute;
import route.TransfersRoute;
import service.AccountService;
import service.AccountServiceImpl;
import service.TransferService;
import service.TransferServiceImpl;

import static spark.Spark.get;
import static spark.Spark.post;
import static util.JsonUtil.json;

public class RestApi {
    private final TransfersRoute transfersRoute;
    private final AccountsRoute accountsRoute;

    public RestApi(DataStore db) {
        final TransferService transferService = new TransferServiceImpl(db);
        final AccountService accountService = new AccountServiceImpl(db);

        final TransferHandler transferHandler = new TransferHandler(accountService, transferService);
        final AccountHandler accountHandler = new AccountHandler(accountService);

        transfersRoute = new TransfersRoute(transferHandler);
        accountsRoute = new AccountsRoute(accountHandler);
    }

    public void listen() {
        setupAccountsEndpoint();
        setupTransfersEndpoint();
    }

    public void setupAccountsEndpoint() {
        get("/accounts", (req, res) -> accountsRoute.getAccounts(), json());
        get("/accounts/:id", (req, res) -> accountsRoute.getAccountById(), json());
    }

    public void setupTransfersEndpoint() {
        get("/transfers", (req, res) -> transfersRoute.getTransfers(), json());
        get("/transfers/:id", (req, res) -> transfersRoute.getTransferById(), json());

        post("/transfers", (req, res) -> transfersRoute.postTransfer(), json());
    }
}

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

public class RestApi {
    private final TransfersRoute transfersRoute;
    private final AccountsRoute accountsRoute;
    private final Gson gson;

    public RestApi(DataStore db) {
        final TransferService transferService = new TransferServiceImpl(db);
        final AccountService accountService = new AccountServiceImpl(db);

        final TransferHandler transferHandler = new TransferHandler(accountService, transferService);
        final AccountHandler accountHandler = new AccountHandler(accountService);

        transfersRoute = new TransfersRoute(transferHandler);
        accountsRoute = new AccountsRoute(accountHandler);

        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void listen() {
        setupAccountsEndpoint();
        setupTransfersEndpoint();
    }

    public void setupAccountsEndpoint() {
        get("/accounts", accountsRoute.getAccounts(), gson::toJson);
        get("/accounts/:id", accountsRoute.getAccountById(), gson::toJson);
    }

    public void setupTransfersEndpoint() {
        get("/transfers", transfersRoute.getTransfers(), gson::toJson);
        get("/transfers/:id", transfersRoute.getTransferById(), gson::toJson);

        post("/transfers", transfersRoute.postTransfer(), gson::toJson);
    }
}

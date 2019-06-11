package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import db.DataStore;
import handler.AccountHandler;
import handler.ErrorHandler;
import handler.TransferHandler;
import route.AccountsRoute;
import route.TransfersRoute;
import service.AccountService;
import service.AccountServiceImpl;
import service.TransferService;
import service.TransferServiceImpl;

import static spark.Spark.*;

public class RestApi {
    private final TransfersRoute transfersRoute;
    private final AccountsRoute accountsRoute;
    private final ErrorHandler errorHandler;
    private final Gson gson;

    public RestApi(DataStore db) {
        final TransferService transferService = new TransferServiceImpl(db);
        final AccountService accountService = new AccountServiceImpl(db);

        final TransferHandler transferHandler = new TransferHandler(accountService, transferService);
        final AccountHandler accountHandler = new AccountHandler(accountService, transferService);

        transfersRoute = new TransfersRoute(transferHandler);
        accountsRoute = new AccountsRoute(accountHandler);

        errorHandler = new ErrorHandler();

        gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    }

    public static void main(String[] args) {
        DataStore db = new DataStore();
        RestApi api = new RestApi(db);

        api.listen();
    }

    public void listen() {
        setupAccountsEndpoint();
        setupTransfersEndpoint();

        setupExceptionHandler();
    }

    public void setupAccountsEndpoint() {
        get("/accounts", accountsRoute.getAccounts(), gson::toJson);
        get("/accounts/:id", accountsRoute.getAccountById(), gson::toJson);
        get("/accounts/:id/transfers", accountsRoute.getTransfersByAccountId(), gson::toJson);

        post("/accounts", accountsRoute.postAccounts(), gson::toJson);
    }

    public void setupTransfersEndpoint() {
        get("/transfers", transfersRoute.getTransfers(), gson::toJson);
        get("/transfers/:id", transfersRoute.getTransferById(), gson::toJson);

        post("/transfers", transfersRoute.postTransfer(), gson::toJson);
    }

    public void setupExceptionHandler() {
        exception(Exception.class, errorHandler.handleException());
    }
}

import com.google.gson.Gson;
import db.DataStore;
import handler.TransferHandler;
import model.Transfer;
import service.AccountService;
import service.AccountServiceImpl;
import service.TransferService;
import service.TransferServiceImpl;

import static spark.Spark.get;
import static spark.Spark.post;
import static util.JsonUtil.json;

public class RestApi {
    private final TransferService transferService;
    private final AccountService accountService;
    private final TransferHandler transferHandler;
    private final Gson gson;

    public RestApi(DataStore db) {
        transferService = new TransferServiceImpl(db);
        accountService = new AccountServiceImpl(db);
        transferHandler = new TransferHandler(accountService, transferService);
        gson = new Gson();
    }

    public void listen() {
        setupAccountsEndpoints();
        setupTransfersEndpoints();
    }

    public void setupAccountsEndpoints() {
        get("/accounts", (req, res) -> accountService.getAccounts(), json());

        get("/accounts/:id", (req, res) -> {
            String id = req.params(":id");

            return accountService.getAccount(id);
        }, json());
    }

    public void setupTransfersEndpoints() {

        get("/transfers", (req, res) -> transferHandler.getTransfers(), json());
        get("/transfers/:id", (req, res) -> {
            String id = req.params(":id");

            Transfer transfer = transferHandler.getTransfer(id);

            return transfer;
        }, json());

        post("/transfers", (req, res) -> {
            Transfer transferReq = gson.fromJson(req.body(), Transfer.class);

            Transfer transfer = transferHandler.transfer(transferReq.source, transferReq.target, transferReq.amount);

            return transfer;
        }, json());
    }
}

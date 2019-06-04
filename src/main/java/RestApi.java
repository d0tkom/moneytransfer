import com.google.gson.Gson;
import db.DataStore;
import model.Transfer;
import service.AccountService;
import service.AccountServiceImpl;
import service.TransferService;
import service.TransferServiceImpl;

import static spark.Spark.get;
import static spark.Spark.post;

public class RestApi {
    private final TransferService transferService;
    private final AccountService accountService;
    private final Gson gson;

    public RestApi(DataStore db) {
        transferService = new TransferServiceImpl(db);
        accountService = new AccountServiceImpl(db);
        gson = new Gson();
    }

    public void listen() {
        setupAccountsEndpoints();
        setupTransfersEndpoints();
    }

    public void setupAccountsEndpoints() {
        get("/accounts", (req, res) -> {
            return accountService.getAccounts();
        });

        get("/accounts/:id", (req, res) -> {
            String id = req.params(":id");

            return gson.toJson(accountService.getAccount(id));
        });
    }

    public void setupTransfersEndpoints() {

        get("/transfers", (req, res) -> {
            return gson.toJson(transferService.getTransfers());
        });

        get("/transfers/:id", (req, res) -> {
            String id = req.params(":id");

            Transfer transfer = transferService.getTransfer(id);

            return gson.toJson(transfer);
        });

        post("/transfers", (req, res) -> {
            Transfer transferReq = gson.fromJson(req.body(), Transfer.class);

            Transfer transfer = transferService.transfer(transferReq.source, transferReq.target, transferReq.amount);

            return gson.toJson(transfer);
        });
    }
}

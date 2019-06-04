import com.google.gson.Gson;
import db.DataStore;
import model.Account;
import model.Transfer;
import service.AccountService;
import service.AccountServiceImpl;
import service.TransferService;
import service.TransferServiceImpl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {

        DataStore db = new DataStore();
        Gson gson = new Gson();

        TransferService transferService = new TransferServiceImpl(db);
        AccountService accountService = new AccountServiceImpl(db);

        get("/accounts", (req, res) -> {
            return gson.toJson(accountService.getAccounts());
        });

        get("/accounts/:id", (req, res) -> {
            String id = req.params(":id");

            return gson.toJson(accountService.getAccount(id));
        });

        get("/transfers", (req, res) -> {
            return gson.toJson(db.transfers.values());
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

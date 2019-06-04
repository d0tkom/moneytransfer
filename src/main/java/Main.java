import com.google.gson.Gson;
import db.DataStore;
import model.Account;
import model.Transfer;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {

        DataStore db = new DataStore();
        Gson gson = new Gson();

        get("/accounts", (req, res) -> {
            List<Account> accs = db.accounts.stream().map(a -> getAccount(a, db)).collect(Collectors.toList());

            return gson.toJson(accs);
        });

        get("/accounts/:id", (req, res) -> {
            String id = req.params(":id");

            if (db.accounts.contains(id)) {
                Account acc = getAccount(id, db);

                return gson.toJson(acc);
            }

            return null;
        });

        get("/transfers", (req, res) -> {
            return gson.toJson(db.transfers.values());
        });

        get("/transfers/:id", (req, res) -> {
            String id = req.params(":id");

            return gson.toJson(db.transfers.get(id));
        });

        post("/transfers", (req, res) -> {
            Transfer t = gson.fromJson(req.body(), Transfer.class);

            String uuid = UUID.randomUUID().toString();
            Transfer transfer = new Transfer(uuid, t.source, t.target, t.amount);

            db.transfers.put(uuid, transfer);

            return gson.toJson(transfer);
        });
    }

    private static Account getAccount(String id, DataStore db) {
        BigDecimal balance = db.transfers.values().stream()
                .map(t -> t.source.equals(id) ? t.amount.negate() : t.target.equals(id) ? t.amount : new BigDecimal(0))
                .reduce(new BigDecimal(0), BigDecimal::add);

        return new Account(id, balance);
    }
}

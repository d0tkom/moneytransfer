package route;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import handler.TransferHandler;
import model.Transfer;
import spark.Request;
import spark.Route;

import java.util.Optional;

public class TransfersRoute {
    private final TransferHandler transferHandler;
    private final Gson gson;

    public TransfersRoute(TransferHandler transferHandler) {
        this.transferHandler = transferHandler;

        gson = new Gson();
    }

    public Route getTransfers() {
        return (req, res) -> {
            res.type("application/json");

            return transferHandler.getTransfers();
        };
    }

    public Route getTransferById() {
        return (req, res) -> {
            String id = req.params(":id");

            Transfer transfer = transferHandler.getTransfer(id);

            res.type("application/json");

            return transfer;
        };
    }

    public Route postTransfer() {
        return (req, res) -> {
            Transfer transferReq = transferFromRequest(req);

            Transfer transfer = transferHandler.transfer(transferReq.source, transferReq.target, transferReq.amount);

            res.type("application/json");
            res.status(201);

            return transfer;
        };
    }

    private Transfer transferFromRequest(Request req) {
        Optional<String> json = Optional.of(req.body());

        return json.map(this::fromJson).orElseThrow(() -> new IllegalArgumentException("Could not parse json"));
    }

    private Transfer fromJson(String json) {
        try {
            return gson.fromJson(json, Transfer.class);
        } catch (JsonParseException e) {
            throw new IllegalArgumentException("Could not parse json");
        }
    }
}

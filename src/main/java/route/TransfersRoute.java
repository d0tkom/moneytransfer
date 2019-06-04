package route;

import com.google.gson.Gson;
import handler.TransferHandler;
import model.Transfer;
import spark.Route;

public class TransfersRoute {
    private final TransferHandler transferHandler;
    private final Gson gson;

    public TransfersRoute(TransferHandler transferHandler) {
        this.transferHandler = transferHandler;

        gson = new Gson();
    }

    public Route getTransfers() {
        return (req, res) -> transferHandler.getTransfers();
    }

    public Route getTransferById() {
        return (req, res) -> {
            String id = req.params(":id");

            Transfer transfer = transferHandler.getTransfer(id);

            return transfer;
        };
    }

    public Route postTransfer() {
        return (req, res) -> {
            Transfer transferReq = gson.fromJson(req.body(), Transfer.class);

            Transfer transfer = transferHandler.transfer(transferReq.source, transferReq.target, transferReq.amount);

            return transfer;
        };
    }
}

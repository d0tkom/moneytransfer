import db.DataStore;

public class Main {
    public static void main(String[] args) {
        DataStore db = new DataStore();
        RestApi api = new RestApi(db);

        api.listen();
    }
}

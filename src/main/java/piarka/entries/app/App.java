package piarka.entries.app;

// TODO upgrade mongodb library to latest
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static spark.Spark.post;
import static spark.Spark.options;
import static spark.Spark.before;
import com.google.gson.Gson;

public class App 
{
    public class SimpleLocation {
        String coordinateX;
        String coordinateY;
        String name;
    }
private static void enableCORS() {
    options("/*", (request, response) -> {
    // TODO ther are so many if cases cant we just remove them all ?
        String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
        if (accessControlRequestHeaders != null) {
            response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
        }
        String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
        if (accessControlRequestMethod != null) {
            response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
        }
        return "OK";
    });

    before((request, response) -> {
        response.header("Access-Control-Allow-Origin", "*");
        response.header("Access-Control-Request-Method", "GET,PUT,POST,DELETE,OPTIONS");
        response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
        response.type("application/json");
    });
}
    // TODO seperate this into seperate files
    public static void main(String[] args) {
        // TODO put this to config
        MongoClientURI connectionString = new MongoClientURI("mongodb://<username>:<password>@piarkacluster-shard-00-00.snpsj.mongodb.net:27017,piarkacluster-shard-00-01.snpsj.mongodb.net:27017,piarkacluster-shard-00-02.snpsj.mongodb.net:27017/myFirstDatabase?ssl=true&replicaSet=atlas-thq208-shard-0&authSource=admin&retryWrites=true&w=majority");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("piarka");

        MongoCollection<Document> collection = database.getCollection("coordinates");
        Gson gson = new Gson();
        enableCORS();
        post("/coordinates", (request, response) -> {
            System.out.println(request.body());
            SimpleLocation coordinateObject = gson.fromJson(request.body(), SimpleLocation.class);
            System.out.println(coordinateObject.name);
            Document doc = new Document("name", coordinateObject.name)
                    .append("coordinateX", coordinateObject.coordinateX)
                    .append("coordinateY", coordinateObject.coordinateY);
            collection.insertOne(doc);
            return "Hello: " + request.params(":name");
        });
    }

}

package piarka.entries.app;

// TODO upgrade mongodb library to latest
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static spark.Spark.post;
import com.google.gson.Gson;

public class App 
{
    public class SimpleLocation {
        String coordinateX;
        String coordinateY;
        String name;
    }

    public static void main(String[] args) {
        // TODO put this to config
        MongoClientURI connectionString = new MongoClientURI("mongodb://dogabudak:199100@piarkacluster-shard-00-00.snpsj.mongodb.net:27017,piarkacluster-shard-00-01.snpsj.mongodb.net:27017,piarkacluster-shard-00-02.snpsj.mongodb.net:27017/myFirstDatabase?ssl=true&replicaSet=atlas-thq208-shard-0&authSource=admin&retryWrites=true&w=majority");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("users");

        MongoCollection<Document> collection = database.getCollection("coordinates");
        Gson gson = new Gson();

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

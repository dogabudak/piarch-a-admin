package piarka.entries.app;

import com.mongodb.MongoClient;
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

        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
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

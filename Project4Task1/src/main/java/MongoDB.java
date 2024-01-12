import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Scanner;

public class MongoDB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a string to be added to MongoDB: "); // get input from user
        String input = scanner.nextLine();
        scanner.close();

        String connectionString = "mongodb://mshoaib:H87NyDqXcAlTIOde@ac-41xfnxe-shard-00-02.fbowljg.mongodb.net:27017,ac-41xfnxe-shard-00-01.fbowljg.mongodb.net:27017,ac-41xfnxe-shard-00-00.fbowljg.mongodb.net:27017/test?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1";

        // from the QuickStart guide: https://www.mongodb.com/docs/drivers/java/sync/v4.3/quick-start/#set-up-a-free-tier-cluster-in-atlas
        MongoClient client = MongoClients.create(connectionString); // create connection
        MongoDatabase database = client.getDatabase("Cluster"); // fetch the database

        MongoCollection<Document> collection = database.getCollection("testStrings");

        Document doc = new Document(); // create a document
        doc.append("name", input);
        collection.insertOne(doc); // insert the doc in the collection

        for (Document d : collection.find()) { // print all the documents in the collection
            String s = d.getString("name"); // only get strings from the documents
            System.out.println(s);
        }

        client.close();
    }

}

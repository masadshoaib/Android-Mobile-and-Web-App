/**
 * mshoaib. Muhammad Asad Shoaib. Project 4 Task 2.
 * This is the class which handles the requests from android application, also writes & reads to/from mongoDB, and shows dashboard
 */
package com.example.p4t2webservice;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class WebGetCatModel {
    static String JSONResp = "";
    /**
     * Method to fetch URL of the image from CatAPI cataas.com having a text written on it.
     * @param input is the text written on cat image
     * @return the URL of the image with the text
     * @throws IOException
     */
    public static String fetchURL(String input) throws IOException {

        String URL = "";

        String apiUrl = "https://cataas.com/cat/says/" + input + "?json=true";

        // Code taken from REST/RMI lab
        java.net.URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // handling for 404 error and other error
            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            // Displaying the JSON object recieved
            System.out.println("JSON Object recieved: "+ jsonObject.entrySet());
            JSONResp = String.valueOf(jsonObject.entrySet());
            String message = jsonObject.get("url").getAsString();
            URL = "https://cataas.com" + message;
        } else {
            System.out.println("Error: " + responseCode);
        }
        return URL;
    }

    /**
     * Return JSON reponse after fetch has been called.
     * @return the JSON as string to be displayed in log
     */
    public static String getJSONResp() {
        return JSONResp;
    }

    /**
     * Method to write to MongoDB.
     * https://www.mongodb.com/docs/drivers/java/sync/v4.3/fundamentals/data-formats/document-data-format-pojo/
     */
    public void addMongoDB(MongoP4T2 m) {
        // Code taken from link above
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        MongoCollection<MongoP4T2> collection = null;

        // Establish the connection
        ConnectionString connectionString = new ConnectionString("mongodb://mshoaib:H87NyDqXcAlTIOde@ac-41xfnxe-shard-00-02.fbowljg.mongodb.net:27017,ac-41xfnxe-shard-00-01.fbowljg.mongodb.net:27017,ac-41xfnxe-shard-00-00.fbowljg.mongodb.net:27017/test?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1");
        MongoClient client = MongoClients.create(connectionString); // create connection
        MongoDatabase database = client.getDatabase("DashboardDB").withCodecRegistry(pojoCodecRegistry); // get DB

        // Settings
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .codecRegistry(pojoCodecRegistry).build();

        // fetch the collection
        collection = database.getCollection("results", MongoP4T2.class);

        // Add pojo to MongoDB
        collection.insertOne(m);
    }
    /**
     * Method to fetch the data from mongoDB.
     * @return Array of data from Mongo DB.
     */
    public ArrayList<MongoP4T2> getMongoDB() {
        ArrayList<MongoP4T2> data = new ArrayList<MongoP4T2>();

        // Code taken from method to add above and https://www.mongodb.com/docs/drivers/java/sync/v4.3/quick-start/#set-up-a-free-tier-cluster-in-atlas
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        MongoCollection<MongoP4T2> collection = null;

        ConnectionString connectionString = new ConnectionString("mongodb://mshoaib:H87NyDqXcAlTIOde@ac-41xfnxe-shard-00-02.fbowljg.mongodb.net:27017,ac-41xfnxe-shard-00-01.fbowljg.mongodb.net:27017,ac-41xfnxe-shard-00-00.fbowljg.mongodb.net:27017/test?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1");
        MongoClient client = MongoClients.create(connectionString); // create connection
        MongoDatabase database = client.getDatabase("DashboardDB").withCodecRegistry(pojoCodecRegistry); // get DB

        MongoClientSettings settings = MongoClientSettings.builder() // collect the settings
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .codecRegistry(pojoCodecRegistry).build();

        collection = database.getCollection("results", MongoP4T2.class);

        // now that we have the collection, we just need to return all elements from it
        for (MongoP4T2 elements : collection.find()) {
            data.add(elements);
        }
        return data;
    }
    /**
     * Method to make calculations for dashboard
     * @return Array of formatted results to be shown on dashboard
     */
    public ArrayList<String> dashboardResults() {
        ArrayList<MongoP4T2> data = getMongoDB();
        ArrayList<String> results = new ArrayList<>();
        StringBuilder log = new StringBuilder();

        HashMap<String, Integer> words = new HashMap<>(); // hashMap to store search terms and their frequencies

        int count = 0; // for storing the number of elements in the DB
        String topWord = ""; // stores most frequently occuring word
        int totalTime = 0; // stores total time taken for searches

        long datetime = 0; // for time of search
        String search = "";
        String responseFromApi = "";
        String device = "";
        long timeTaken = 0;
        String sentToPhone = "";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String timeString = "";

        for (MongoP4T2 d : data) {
            // adding frequencies of words so that most frequently occuring one can be found
            String word = d.getWord();
            if (!words.containsKey(word)) {
                words.put(word, 1);
            } else {
                words.put(word, words.get(word) + 1);
            }

            // getting total time taken for searches
            totalTime += d.getTimeTaken();

            // incrementing frequency of elements
            count += 1;

            // now need to get the log data
            datetime = d.getDateTime();
            date = new Date(datetime);
            timeString = sdf.format(date);
            search = word;
            responseFromApi = d.getResponse();
            device = d.getUa();
            timeTaken = d.getTimeTaken();
            sentToPhone = d.getSentToPhone();

            log.append( timeString + "----" + search + "----" + responseFromApi + "----" + device + "----" + timeTaken + "----" + sentToPhone + "</td>" + "|" );

        }

        // Calculating top frequencies
        String mostFrequentTerm1 = "";
        int highestFrequency1 = 0;
        String mostFrequentTerm2 = "";
        int highestFrequency2 = 0;

        for (HashMap.Entry<String, Integer> entry : words.entrySet()) {
            String word = entry.getKey();
            int frequency = entry.getValue();
            if (frequency > highestFrequency1) {
                highestFrequency2 = highestFrequency1;
                mostFrequentTerm2 = mostFrequentTerm1;
                highestFrequency1 = frequency;
                mostFrequentTerm1 = word;
            } else if (frequency > highestFrequency2) {
                highestFrequency2 = frequency;
                mostFrequentTerm2 = word;
            }
        }
        topWord = "Term 1: " + mostFrequentTerm1 + ", Term 2: " + mostFrequentTerm2;

        // Adding required things on dashboard for operational analytics
        results.add(0, String.valueOf(count));
        results.add(1, topWord);
        results.add(2, String.valueOf(totalTime / count));

        //Adding things for logs
        results.add(3, String.valueOf(log));


        return results;
    }
}

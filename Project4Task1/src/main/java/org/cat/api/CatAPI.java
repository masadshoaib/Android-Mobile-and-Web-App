package org.cat.api;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CatAPI {
    /**
     * Method to fetch URL of the image from CatAPI cataas.com having a text written on it.
     * @param input is the text written on cat image
     * @return the URL of the image with the text
     * @throws IOException
     */
    private static String fetchURL(String input) throws IOException {
        String URL = "";

        String apiUrl = "https://cataas.com/cat/says/" + input + "?json=true";

//        System.out.println(apiUrl);
        // Code taken from REST/RMI lab
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // handling for 404 error and other error
            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            // Displaying the JSON object recieved
            System.out.println("JSON Object recieved: "+jsonObject.entrySet());
            String message = jsonObject.get("url").getAsString();
            URL = "https://cataas.com" + message;
        } else {
            System.out.println("Error: " + responseCode);
        }
        return URL;
    }

    /**
     * Main class for getting input from user and getting the imageURL.
     * @param args not being used
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // Getting input from user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter search term to be displayed with cat: ");
        String searchTerm = scanner.nextLine();
        scanner.close();

        System.out.println("URL to be used from JSON Object: " + fetchURL(searchTerm));

    }

}

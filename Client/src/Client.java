import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.*;

public class Client {
    public static void main(String[] args) {
        String uri = "http://localhost:8000/phonebook";
        HttpClient client = HttpClient.newHttpClient();

        //Simple get to check to see if we have a connection
        try {
            //Run a simple get request
            get(client, uri);
        }catch (Exception e) {
            e.printStackTrace();
        }

        //Send a small phonebook --- Uncomment to send larger phonebooks
        sendFile(client, uri, "SmallPhonebook.xml");
        //sendFile(client, uri, "MediumPhonebook.xml");
        //sendFile(client, uri, "LargePhonebook.xml");
    }

    public static void sendFile(HttpClient client, String uri, String fileName) {
        try {
            //Create one json object of the entire xml file
            JSONObject xmlPhonebook = XML.toJSONObject(readXML(fileName));
            JSONArray contacts = xmlPhonebook.getJSONObject("PHONEBOOK").getJSONArray("CONTACT");
            try {
                for (int i = 0; i < contacts.length(); i++) {
                    //Break down and send each contact as an individual package
                    JSONObject contactDsc = contacts.getJSONObject(i);
                    post(client, uri, contactDsc);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Posts request to the uri, containing a contact json
    public static void post(HttpClient client, String uri, JSONObject contactDesc) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                //Send the data as bytes
                .POST(HttpRequest.BodyPublishers.ofByteArray(contactDesc.toString().getBytes()))
                .uri(URI.create(uri))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    //Get request
    public static void get(HttpClient client, String uri) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();
        try{
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch(IOException e) {
            throw new Exception();
        }
    }

    //Read and return a string of the entire xml-file
    public static String readXML(String fileName) throws IOException {
        File xmlFile = new File("src/" + fileName);
        Reader fileReader = new FileReader(xmlFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuild = new StringBuilder();
        String line = bufferedReader.readLine();

        while (line != null) {
            stringBuild.append(line).append("\n");
            line = bufferedReader.readLine();
        }

        return stringBuild.toString();
    }
}
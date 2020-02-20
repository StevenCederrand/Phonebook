import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.*;

public class Client {
    public static void main(String[] args) throws IOException {
        String uri = "http://localhost:8000/phonebook";
        HttpClient client = HttpClient.newHttpClient();

        //Simple get to check to see if we have a connection
        if(!get(client, uri)) {
            throw new IOException("No Connection");
        }

        JSONObject xmlPhonebook = XML.toJSONObject(readXML());
        JSONArray contacts = xmlPhonebook.getJSONObject("PHONEBOOK").getJSONArray("CONTACT");
        try {
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject contactDsc = contacts.getJSONObject(i);
                System.out.println(contactDsc.toString());
                post(client, uri, contactDsc);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void post(HttpClient client, String uri, JSONObject contactDesc) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofByteArray(contactDesc.toString().getBytes()))
                .uri(URI.create(uri))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static boolean get(HttpClient client, String uri) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();
        HttpResponse<String> response;
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.toString());
        } catch(IOException | InterruptedException ioExcep) {
            System.out.print("client send");
            return false;
        }

        return true;
    }

    //Read XML file
    public static String readXML() throws IOException {
        File xmlFile = new File("src/Phonebook.xml");
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
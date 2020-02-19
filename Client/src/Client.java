import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        //System.out.println(xmlPhonebook.toString(4));

        JSONObject phonebook = xmlPhonebook.getJSONObject("PHONEBOOK");
        JSONArray contacts = phonebook.getJSONArray("CONTACT");

        try {
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject contactDsc = contacts.getJSONObject(i);
                System.out.println(contactDsc.toString());
                post(client, uri, contactDsc);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void post(HttpClient client, String uri, JSONObject contactDesc) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofByteArray(contactDesc.toString().getBytes()))
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static boolean get(HttpClient client, String uri) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();
        HttpResponse<String> response = null;
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

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

        String xmlString = stringBuild.toString();
        return xmlString;
    }
}
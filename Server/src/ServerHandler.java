import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.TreeSet;

public class ServerHandler implements HttpHandler {
    //Contacts are placed in a tree set structure.
    //This solves the issue of data duplicates, but also will sort the list in alphabetical order
    private TreeSet<JSONObject> contacts;

    public ServerHandler() {
        contacts = new TreeSet<>(new JSONCompare("NAME"));
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        //Possibility for expansion
        if("POST".equals(httpExchange.getRequestMethod())) {
            JSONObject contact;

            StringBuilder data = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(httpExchange.getRequestBody());

            char[] buffer = new char[256];
            int read;
            //Read data from the input stream and append it to our data string
            while ((read = reader.read(buffer)) > 0) {
                data.append(buffer, 0, read);
            }

            try {
                //Create a json object from the incoming data
                contact = new JSONObject(data.toString());
                contacts.add(contact);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        htmlBuilder(httpExchange);
    }

    private void htmlBuilder(HttpExchange httpExchange) throws IOException {
        //Build our HTML
        String param = "<h1>PHONE BOOK</h1>";
        //Format into an HTML table
        param += "<table> <tr> <th>Name</th><th>Work Number</th><th>Home Number</th></tr>";
        for (JSONObject var : contacts) {
            param += "<tr><td>" + var.get("NAME") + "</td><td>" + var.get("WORK_NUM") + "</td><td>" + var.get("HOME_NUM") + "</td></tr>";
        }
        param += "</table>";

        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder htmlBuild = new StringBuilder();

        htmlBuild.append(param);

        String htmlResponse = htmlBuild.toString();
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream = httpExchange.getResponseBody();
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
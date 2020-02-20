import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashSet;

public class ServerHandler implements HttpHandler {
    private HashSet<JSONObject> contacts;
    public ServerHandler() {
        contacts = new HashSet<>();
    }

    @Override
    public void handle(HttpExchange httpExch) throws IOException {
        //Possibility for expansion
        if("POST".equals(httpExch.getRequestMethod())) {
            JSONObject contact;

            StringBuilder data = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(httpExch.getRequestBody());

            char[] buffer = new char[256];
            int read;

            while ((read = reader.read(buffer)) > 0) {
                data.append(buffer, 0, read);
            }
            try {
                contact = new JSONObject(data.toString());
                contacts.add(contact);
            } catch(JSONException err) {
                System.out.println("Error converting to json object");
            }
        }

        String param = "<h1>PHONEBOOK</h1>";
        param += "<table> <tr> <th>Name</th><th>Work Number</th><th>Home Number</th></tr>";
        for (JSONObject var : contacts) {
            param += "<tr><td>" + var.get("NAME") + "</td><td>" + var.get("WORK_NUM") + "</td><td>" + var.get("HOME_NUM") + "</td></tr>";
        }
        param += "</table>";
        htmlBuilder(httpExch, param);
    }

    private void htmlBuilder(HttpExchange httpExchange, String params) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder htmlBuild = new StringBuilder();

        htmlBuild.append(params);

        String htmlResponse = htmlBuild.toString();
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream = httpExchange.getResponseBody();
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}

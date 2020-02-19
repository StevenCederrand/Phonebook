import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Vector;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Server {
    public static Vector<JSONObject> contacts;

    public static void main(String[] args) {
        contacts = new Vector<JSONObject>();

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/phonebook", new PhonebookServerHandler());
            server.setExecutor(null);
            server.start();
        } catch(IOException ioeException) {
            System.out.print("IoException");
        }

        System.out.print("Hello, Server\n");
    }

    public static class PhonebookServerHandler implements HttpHandler {
        public static int getRequestCount = 0;
        public static int postRequestCount = 0;

        @Override
        public void handle(HttpExchange httpExch) throws IOException {

            //Possibility for expansion
            if("GET".equals(httpExch.getRequestMethod())) {

            }
            else if("POST".equals(httpExch.getRequestMethod())) {
                JSONObject contact = null;

                String postData = null;
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

            String param = new String("<h1>PHONEBOOK</h1>");
            param += "<table> <tr> <th>Name</th><th>Work Number</th><th>Home Number</th></tr>";
            for (JSONObject var : contacts) {
                param += "<tr><td>" + var.get("NAME") + "</td><td>" + var.get("WORK_NUM") + "</td><td>" + var.get("HOME_NUM") + "</td></tr>";
            }
            param += "</table>";
            htmlBuilder(httpExch, param);
        }
    }

    public static void htmlBuilder(HttpExchange httpExchange, String params) throws IOException {
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
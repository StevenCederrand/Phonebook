import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Vector;

import com.sun.net.httpserver.HttpServer;

import org.json.JSONObject;

public class Server {
    public static Vector<JSONObject> contacts;

    public static void main(String[] args) {
        contacts = new Vector<JSONObject>();

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/phonebook", new ServerHandler());
            server.setExecutor(null);
            server.start();
        } catch(IOException ioeException) {
            System.out.print("IoException");
        }

        System.out.print("Hello, Server\n");
    }
}
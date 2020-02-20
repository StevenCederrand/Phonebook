import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

//http://localhost:8000/phonebook -> server site

public class Server {

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/phonebook", new ServerHandler());
            server.setExecutor(null);
            server.start();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
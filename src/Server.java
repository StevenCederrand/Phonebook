import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/test", new PhonebookServerHandler());
            server.setExecutor(null);
            server.start();
        } catch(IOException ioeException) {
            System.out.print("IoException");
        }

        System.out.print("Hello, world\n");
    }
}

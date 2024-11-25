package model;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.io.FileWriter;

public class HttpServerLogin {
    private CountDownLatch latch;
    static String filepath;

    public HttpServerLogin() {
    }

    public HttpServerLogin(CountDownLatch latch) {
        this.latch = latch;
    }

    public HttpServer CreateSimpleHttpServer() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/auth/retorno-login", new AuthHandler(latch));
        return server;
    }

    static class AuthHandler implements HttpHandler {
        private CountDownLatch latch;

        public AuthHandler(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                System.out.println("Login cookie recebido: " + query);

                WriteLoginFromFile(query, filepath);

                String response = "Token recebido com sucesso!";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();


                latch.countDown();
            } else {
                String response = "Método não suportado!";
                exchange.sendResponseHeaders(405, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    public String readLoginFromFile(String filePath) {
        HttpServerLogin.filepath = filePath;
        String text = null;

        try (BufferedReader leitorFiler = new BufferedReader(new FileReader(filePath))) {
            text = leitorFiler.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

    public static void WriteLoginFromFile(String text, String filePath){
        try(FileWriter escrever = new FileWriter(filePath)){
            escrever.write(text);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}

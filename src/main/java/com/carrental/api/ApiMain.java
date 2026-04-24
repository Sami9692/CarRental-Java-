package com.carrental.api;

import com.carrental.util.DBConnection;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ApiMain {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                    new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                    LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
            .registerTypeAdapter(java.time.LocalDateTime.class, (JsonSerializer<java.time.LocalDateTime>) (src, typeOfSrc, context) ->
                    new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
            .registerTypeAdapter(java.time.LocalDateTime.class, (JsonDeserializer<java.time.LocalDateTime>) (json, typeOfT, context) ->
                    java.time.LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .create();

    public static void main(String[] args) {
        System.out.println("Starting AutoElite API Server (Zero-Dependency Edition)...");

        // Ensure DB is connected
        DBConnection.getInstance();

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            // Register Contexts
            AuthApi.registerRoutes(server);
            CarApi.registerRoutes(server);
            ReservationApi.registerRoutes(server);
            PaymentApi.registerRoutes(server);

            server.setExecutor(null); // creates a default executor
            server.start();
            System.out.println("AutoElite API Server running on http://localhost:8080");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.stop(0);
                DBConnection.getInstance().closeConnection();
            }));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendJson(HttpExchange exchange, int statusCode, Object responseObj) throws IOException {
        String response = GSON.toJson(responseObj);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        
        byte[] bytes = response.getBytes("UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
    
    public static void handleCorsOptions(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(204, -1);
    }
}

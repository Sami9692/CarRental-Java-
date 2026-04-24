package com.carrental.api;

import com.carrental.model.User;
import com.carrental.service.impl.AuthServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class AuthApi {

    private static final AuthServiceImpl authService = new AuthServiceImpl();

    public static void registerRoutes(HttpServer server) {

        server.createContext("/api/auth/login", exchange -> {
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                ApiMain.handleCorsOptions(exchange);
                return;
            }
            if ("POST".equals(exchange.getRequestMethod())) {
                try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                    LoginRequest req = ApiMain.GSON.fromJson(reader, LoginRequest.class);
                    Optional<User> userOpt = authService.login(req.username, req.password);

                    if (userOpt.isPresent()) {
                        ApiMain.sendJson(exchange, 200, userOpt.get());
                    } else {
                        ApiMain.sendJson(exchange, 401, new ErrorResponse("Invalid username or password"));
                    }
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        });

        server.createContext("/api/auth/register", exchange -> {
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                ApiMain.handleCorsOptions(exchange);
                return;
            }
            if ("POST".equals(exchange.getRequestMethod())) {
                try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                    RegisterRequest req = ApiMain.GSON.fromJson(reader, RegisterRequest.class);
                    try {
                        User user = authService.register(
                                req.firstName, req.lastName, req.email, req.dob, req.license, req.username, req.password
                        );
                        ApiMain.sendJson(exchange, 200, user);
                    } catch (Exception e) {
                        ApiMain.sendJson(exchange, 400, new ErrorResponse(e.getMessage()));
                    }
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        });
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }

    public static class RegisterRequest {
        public String firstName;
        public String lastName;
        public String email;
        public String dob;
        public String license;
        public String username;
        public String password;
    }

    public static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) { this.error = error; }
    }
}

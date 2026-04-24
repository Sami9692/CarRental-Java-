package com.carrental.api;

import com.carrental.model.Reservation;
import com.carrental.service.impl.ReservationServiceImpl;
import com.sun.net.httpserver.HttpServer;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class ReservationApi {

    private static final ReservationServiceImpl reservationService = new ReservationServiceImpl();

    public static void registerRoutes(HttpServer server) {

        server.createContext("/api/reservations", exchange -> {
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                ApiMain.handleCorsOptions(exchange);
                return;
            }
            
            String path = exchange.getRequestURI().getPath();
            
            if ("GET".equals(exchange.getRequestMethod())) {
                if (path.startsWith("/api/reservations/user/")) {
                    try {
                        int userId = Integer.parseInt(path.substring("/api/reservations/user/".length()));
                        ApiMain.sendJson(exchange, 200, reservationService.getUserReservations(userId));
                    } catch (Exception e) {
                        ApiMain.sendJson(exchange, 400, new AuthApi.ErrorResponse("Invalid user ID"));
                    }
                } else if (path.equals("/api/reservations")) {
                    ApiMain.sendJson(exchange, 200, reservationService.getAllReservations());
                } else {
                    exchange.sendResponseHeaders(404, -1);
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                    BookRequest req = ApiMain.GSON.fromJson(reader, BookRequest.class);
                    try {
                        LocalDate start = LocalDate.parse(req.startDate);
                        LocalDate end = LocalDate.parse(req.endDate);
                        Reservation res = reservationService.bookCar(
                                req.userId, req.carId, req.locationId, start, end, req.insuranceTypeId
                        );
                        ApiMain.sendJson(exchange, 200, res);
                    } catch (Exception e) {
                        ApiMain.sendJson(exchange, 400, new AuthApi.ErrorResponse(e.getMessage()));
                    }
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        });
    }

    public static class BookRequest {
        public int userId;
        public int carId;
        public int locationId;
        public String startDate;
        public String endDate;
        public int insuranceTypeId;
    }
}

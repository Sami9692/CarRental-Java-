package com.carrental.api;

import com.carrental.model.CardDetails;
import com.carrental.model.Payment;
import com.carrental.model.Reservation;
import com.carrental.service.impl.PaymentServiceImpl;
import com.carrental.service.impl.ReservationServiceImpl;
import com.sun.net.httpserver.HttpServer;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class PaymentApi {

    private static final PaymentServiceImpl paymentService = new PaymentServiceImpl();
    private static final ReservationServiceImpl reservationService = new ReservationServiceImpl();

    public static void registerRoutes(HttpServer server) {

        server.createContext("/api/payments/cards", exchange -> {
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                ApiMain.handleCorsOptions(exchange);
                return;
            }
            
            String path = exchange.getRequestURI().getPath();

            if ("GET".equals(exchange.getRequestMethod())) {
                // Example: /api/payments/cards/user/1
                if (path.startsWith("/api/payments/cards/user/")) {
                    try {
                        int userId = Integer.parseInt(path.substring("/api/payments/cards/user/".length()));
                        List<CardDetails> cards = paymentService.getUserCards(userId);
                        ApiMain.sendJson(exchange, 200, cards);
                    } catch (Exception e) {
                        ApiMain.sendJson(exchange, 400, new AuthApi.ErrorResponse("Invalid user ID"));
                    }
                } else {
                    exchange.sendResponseHeaders(404, -1);
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                // Add a new card
                try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                    AddCardRequest req = ApiMain.GSON.fromJson(reader, AddCardRequest.class);
                    try {
                        CardDetails card = paymentService.addCard(
                                req.userId, req.cardNumber, req.cardHolder, req.expiryDate, req.cardType
                        );
                        ApiMain.sendJson(exchange, 200, card);
                    } catch (Exception e) {
                        ApiMain.sendJson(exchange, 400, new AuthApi.ErrorResponse(e.getMessage()));
                    }
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        });

        server.createContext("/api/payments", exchange -> {
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                ApiMain.handleCorsOptions(exchange);
                return;
            }

            if ("POST".equals(exchange.getRequestMethod())) {
                try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                    ProcessPaymentRequest req = ApiMain.GSON.fromJson(reader, ProcessPaymentRequest.class);
                    try {
                        Optional<Reservation> resOpt = reservationService.getReservationById(req.reservationId);
                        if (resOpt.isEmpty()) {
                            ApiMain.sendJson(exchange, 404, new AuthApi.ErrorResponse("Reservation not found"));
                            return;
                        }
                        Reservation res = resOpt.get();
                        
                        // Security check
                        if (res.getUserId() != req.userId) {
                            ApiMain.sendJson(exchange, 403, new AuthApi.ErrorResponse("Not authorized to pay for this reservation"));
                            return;
                        }

                        BigDecimal amount = reservationService.calculateTotalCost(
                                res.getCarId(), res.getInsuranceTypeId(), res.getStartDate(), res.getEndDate()
                        );

                        Payment payment = paymentService.makePayment(
                                req.reservationId, req.userId, req.cardId, amount, req.paymentMethod
                        );
                        
                        ApiMain.sendJson(exchange, 200, payment);
                    } catch (Exception e) {
                        ApiMain.sendJson(exchange, 400, new AuthApi.ErrorResponse(e.getMessage()));
                    }
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        });
    }

    public static class AddCardRequest {
        public int userId;
        public String cardNumber;
        public String cardHolder;
        public String expiryDate;
        public String cardType;
    }

    public static class ProcessPaymentRequest {
        public int reservationId;
        public int userId;
        public int cardId;
        public String paymentMethod; // e.g. "Credit"
    }
}

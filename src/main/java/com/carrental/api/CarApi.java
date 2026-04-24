package com.carrental.api;

import com.carrental.service.impl.CarServiceImpl;
import com.sun.net.httpserver.HttpServer;

public class CarApi {

    private static final CarServiceImpl carService = new CarServiceImpl();

    public static void registerRoutes(HttpServer server) {

        server.createContext("/api/cars", exchange -> {
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                ApiMain.handleCorsOptions(exchange);
                return;
            }
            
            String path = exchange.getRequestURI().getPath();
            
            if ("GET".equals(exchange.getRequestMethod())) {
                if (path.equals("/api/cars/available")) {
                    ApiMain.sendJson(exchange, 200, carService.getAvailableCars());
                } else if (path.equals("/api/cars")) {
                    ApiMain.sendJson(exchange, 200, carService.getAllCars());
                } else {
                    exchange.sendResponseHeaders(404, -1);
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                try (java.io.InputStreamReader reader = new java.io.InputStreamReader(exchange.getRequestBody(), java.nio.charset.StandardCharsets.UTF_8)) {
                    com.carrental.model.Car req = ApiMain.GSON.fromJson(reader, com.carrental.model.Car.class);
                    try {
                        com.carrental.model.Car car = carService.addCar(
                            req.getMake(), req.getModel(), req.getYear(), req.getLicensePlate(),
                            req.getDailyRate(), req.getTypeId(), req.getLocationId(), req.getImageUrl()
                        );
                        ApiMain.sendJson(exchange, 200, car);
                    } catch (Exception e) {
                        ApiMain.sendJson(exchange, 400, new AuthApi.ErrorResponse(e.getMessage()));
                    }
                }
            } else if ("PUT".equals(exchange.getRequestMethod()) || "DELETE".equals(exchange.getRequestMethod())) {
                if (path.startsWith("/api/cars/")) {
                    try {
                        int carId = Integer.parseInt(path.substring("/api/cars/".length()));
                        if ("DELETE".equals(exchange.getRequestMethod())) {
                            carService.deleteCar(carId);
                            ApiMain.sendJson(exchange, 200, new AuthApi.ErrorResponse("Car deleted")); // Reuse ErrorResponse for simple message
                        } else {
                            try (java.io.InputStreamReader reader = new java.io.InputStreamReader(exchange.getRequestBody(), java.nio.charset.StandardCharsets.UTF_8)) {
                                com.carrental.model.Car req = ApiMain.GSON.fromJson(reader, com.carrental.model.Car.class);
                                req.setCarId(carId);
                                carService.updateCar(req);
                                ApiMain.sendJson(exchange, 200, req);
                            }
                        }
                    } catch (Exception e) {
                        ApiMain.sendJson(exchange, 400, new AuthApi.ErrorResponse(e.getMessage()));
                    }
                } else {
                    exchange.sendResponseHeaders(404, -1);
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        });
    }
}

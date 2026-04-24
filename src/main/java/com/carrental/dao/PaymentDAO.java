package com.carrental.dao;

import com.carrental.model.Payment;
import java.util.List;
import java.util.Optional;

public interface PaymentDAO {
    int processPayment(Payment payment);
    Optional<Payment> findById(int paymentId);
    Optional<Payment> findByReservationId(int reservationId);
    List<Payment> findByUserId(int userId);
    void updatePaymentStatus(int paymentId, String status);
}

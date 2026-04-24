package com.carrental.service.impl;

import com.carrental.dao.CardDetailsDAO;
import com.carrental.dao.PaymentDAO;
import com.carrental.dao.ReservationDAO;
import com.carrental.dao.impl.CardDetailsDAOImpl;
import com.carrental.dao.impl.PaymentDAOImpl;
import com.carrental.dao.impl.ReservationDAOImpl;
import com.carrental.model.CardDetails;
import com.carrental.model.Payment;
import com.carrental.model.Reservation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Manages payment processing and card management.
 */
public class PaymentServiceImpl {

    private final PaymentDAO       paymentDAO;
    private final CardDetailsDAO   cardDAO;
    private final ReservationDAO   reservationDAO;

    public PaymentServiceImpl() {
        this.paymentDAO     = new PaymentDAOImpl();
        this.cardDAO        = new CardDetailsDAOImpl();
        this.reservationDAO = new ReservationDAOImpl();
    }

    /**
     * Processes a payment for a reservation.
     * Validates the reservation exists and belongs to the user.
     *
     * @return the created Payment object
     */
    public Payment makePayment(int reservationId, int userId, int cardId,
                               BigDecimal amount, String paymentMethod) {

        Reservation res = reservationDAO.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + reservationId));

        if (res.getUserId() != userId) {
            throw new SecurityException("This reservation does not belong to you.");
        }

        // Check card belongs to user
        CardDetails card = cardDAO.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found: " + cardId));
        if (card.getUserId() != userId) {
            throw new SecurityException("This card does not belong to you.");
        }

        // Check no duplicate payment
        if (paymentDAO.findByReservationId(reservationId).isPresent()) {
            throw new IllegalStateException("A payment already exists for reservation " + reservationId);
        }

        Payment payment = new Payment();
        payment.setReservationId(reservationId);
        payment.setCardId(cardId);
        payment.setAmount(amount);
        payment.setPaymentStatus("completed");
        payment.setPaymentMethod(paymentMethod);

        paymentDAO.processPayment(payment);

        // Update reservation status to 'confirmed'
        reservationDAO.updateStatus(reservationId, "confirmed");

        System.out.printf("[PaymentService] Payment #%d processed: $%.2f%n",
                payment.getPaymentId(), amount);
        return payment;
    }

    /** Saves a new credit/debit card for the user (stores only last 4 digits). */
    public CardDetails addCard(int userId, String fullCardNumber, String cardHolder,
                               String expiryDate, String cardType) {
        // Store only last 4 digits
        String lastFour = fullCardNumber.length() >= 4
                ? fullCardNumber.substring(fullCardNumber.length() - 4) : fullCardNumber;
        CardDetails card = new CardDetails(0, userId, lastFour, cardHolder, expiryDate, cardType);
        cardDAO.addCard(card);
        return card;
    }

    public List<CardDetails> getUserCards(int userId) {
        return cardDAO.findByUserId(userId);
    }

    public void deleteCard(int cardId, int userId) {
        CardDetails card = cardDAO.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found: " + cardId));
        if (card.getUserId() != userId) throw new SecurityException("Card does not belong to you.");
        cardDAO.deleteCard(cardId);
    }

    public Optional<Payment> getPaymentByReservation(int reservationId) {
        return paymentDAO.findByReservationId(reservationId);
    }

    public List<Payment> getUserPayments(int userId) {
        return paymentDAO.findByUserId(userId);
    }
}

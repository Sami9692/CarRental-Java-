package com.carrental.controller;

import com.carrental.model.CardDetails;
import com.carrental.model.Payment;
import com.carrental.service.impl.PaymentServiceImpl;
import com.carrental.service.impl.ReservationServiceImpl;
import com.carrental.model.Reservation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Controller for payment and card management UI.
 */
public class PaymentController {

    private final PaymentServiceImpl     paymentService;
    private final ReservationServiceImpl reservationService;
    private final Scanner scanner;

    public PaymentController(Scanner scanner) {
        this.paymentService     = new PaymentServiceImpl();
        this.reservationService = new ReservationServiceImpl();
        this.scanner            = scanner;
    }

    /** Pay for a reservation. */
    public void makePayment(int userId) {
        System.out.println("\n=== MAKE PAYMENT ===");

        // Show user's cards
        List<CardDetails> cards = paymentService.getUserCards(userId);
        if (cards.isEmpty()) {
            System.out.println("No saved cards found. Please add a card first.");
            return;
        }
        System.out.println("Your Cards:");
        cards.forEach(c -> System.out.printf("  [%d] %s **** %s (expires %s)%n",
                c.getCardId(), c.getCardType(), c.getCardNumber(), c.getExpiryDate()));

        try {
            System.out.print("Reservation ID : "); int resId  = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Card ID        : "); int cardId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Payment Method (Credit/Debit): "); String method = scanner.nextLine().trim();

            // Auto-calculate amount
            Optional<Reservation> resOpt = reservationService.getReservationById(resId);
            if (resOpt.isEmpty()) { System.out.println("✘ Reservation not found."); return; }
            Reservation res = resOpt.get();
            BigDecimal amount = reservationService.calculateTotalCost(
                    res.getCarId(), res.getInsuranceTypeId(), res.getStartDate(), res.getEndDate());

            System.out.printf("Amount to pay: $%.2f%nConfirm? (yes/no): ", amount);
            if (!"yes".equalsIgnoreCase(scanner.nextLine().trim())) {
                System.out.println("Payment cancelled."); return;
            }

            Payment payment = paymentService.makePayment(resId, userId, cardId, amount, method);
            System.out.printf("✔ Payment #%d successful! Amount: $%.2f%n",
                    payment.getPaymentId(), payment.getAmount());
        } catch (NumberFormatException ex) {
            System.out.println("✘ Invalid number input.");
        } catch (Exception ex) {
            System.out.println("✘ " + ex.getMessage());
        }
    }

    /** Add a credit/debit card to the user's profile. */
    public void addCard(int userId) {
        System.out.println("\n=== ADD PAYMENT CARD ===");
        try {
            System.out.print("Card Number  : "); String number  = scanner.nextLine().trim();
            System.out.print("Card Holder  : "); String holder  = scanner.nextLine().trim();
            System.out.print("Expiry (MM/YY): "); String expiry = scanner.nextLine().trim();
            System.out.print("Card Type (Visa/Mastercard/Amex): "); String type = scanner.nextLine().trim();

            CardDetails card = paymentService.addCard(userId, number, holder, expiry, type);
            System.out.printf("✔ Card added (ID: %d). Stored last 4 digits: %s%n",
                    card.getCardId(), card.getCardNumber());
        } catch (Exception ex) {
            System.out.println("✘ Error: " + ex.getMessage());
        }
    }

    /** Remove a saved card. */
    public void deleteCard(int userId) {
        System.out.println("\n=== REMOVE CARD ===");
        List<CardDetails> cards = paymentService.getUserCards(userId);
        if (cards.isEmpty()) { System.out.println("No cards saved."); return; }
        cards.forEach(c -> System.out.printf("  [%d] %s **** %s%n",
                c.getCardId(), c.getCardType(), c.getCardNumber()));
        System.out.print("Card ID to remove: ");
        try {
            int cardId = Integer.parseInt(scanner.nextLine().trim());
            paymentService.deleteCard(cardId, userId);
            System.out.println("✔ Card removed.");
        } catch (Exception ex) {
            System.out.println("✘ " + ex.getMessage());
        }
    }

    /** View payment history for the current user. */
    public void viewPaymentHistory(int userId) {
        System.out.println("\n=== PAYMENT HISTORY ===");
        List<Payment> payments = paymentService.getUserPayments(userId);
        if (payments.isEmpty()) { System.out.println("No payments found."); return; }
        System.out.printf("%-6s %-14s %-10s %-12s %-20s%n",
                "ID", "ReservationID", "Amount", "Status", "Date");
        System.out.println("-".repeat(65));
        for (Payment p : payments) {
            System.out.printf("%-6d %-14d $%-9.2f %-12s %-20s%n",
                    p.getPaymentId(), p.getReservationId(), p.getAmount(),
                    p.getPaymentStatus(), p.getPaymentDate());
        }
    }
}

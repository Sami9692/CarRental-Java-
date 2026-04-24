package com.carrental.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private int paymentId;
    private int reservationId;
    private int cardId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentStatus;   // "pending" | "completed" | "refunded"
    private String paymentMethod;

    public Payment() {}

    public Payment(int paymentId, int reservationId, int cardId, BigDecimal amount,
                   LocalDateTime paymentDate, String paymentStatus, String paymentMethod) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.cardId = cardId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
    }

    public int getPaymentId()                   { return paymentId; }
    public void setPaymentId(int v)             { this.paymentId = v; }
    public int getReservationId()               { return reservationId; }
    public void setReservationId(int v)         { this.reservationId = v; }
    public int getCardId()                      { return cardId; }
    public void setCardId(int v)                { this.cardId = v; }
    public BigDecimal getAmount()               { return amount; }
    public void setAmount(BigDecimal v)         { this.amount = v; }
    public LocalDateTime getPaymentDate()       { return paymentDate; }
    public void setPaymentDate(LocalDateTime v) { this.paymentDate = v; }
    public String getPaymentStatus()            { return paymentStatus; }
    public void setPaymentStatus(String v)      { this.paymentStatus = v; }
    public String getPaymentMethod()            { return paymentMethod; }
    public void setPaymentMethod(String v)      { this.paymentMethod = v; }

    @Override
    public String toString() {
        return String.format("Payment[id=%d, reservationId=%d, amount=$%.2f, status=%s, date=%s]",
                paymentId, reservationId, amount, paymentStatus, paymentDate);
    }
}

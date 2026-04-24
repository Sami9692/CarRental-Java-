package com.carrental.model;

public class CardDetails {
    private int cardId;
    private int userId;
    private String cardNumber;   // last 4 digits stored
    private String cardHolder;
    private String expiryDate;
    private String cardType;     // Visa, Mastercard, etc.

    public CardDetails() {}

    public CardDetails(int cardId, int userId, String cardNumber,
                       String cardHolder, String expiryDate, String cardType) {
        this.cardId = cardId;
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
        this.cardType = cardType;
    }

    public int getCardId()              { return cardId; }
    public void setCardId(int v)        { this.cardId = v; }
    public int getUserId()              { return userId; }
    public void setUserId(int v)        { this.userId = v; }
    public String getCardNumber()       { return cardNumber; }
    public void setCardNumber(String v) { this.cardNumber = v; }
    public String getCardHolder()       { return cardHolder; }
    public void setCardHolder(String v) { this.cardHolder = v; }
    public String getExpiryDate()       { return expiryDate; }
    public void setExpiryDate(String v) { this.expiryDate = v; }
    public String getCardType()         { return cardType; }
    public void setCardType(String v)   { this.cardType = v; }

    @Override
    public String toString() {
        return String.format("Card[id=%d, %s **** %s, holder=%s, expires=%s]",
                cardId, cardType, cardNumber, cardHolder, expiryDate);
    }
}

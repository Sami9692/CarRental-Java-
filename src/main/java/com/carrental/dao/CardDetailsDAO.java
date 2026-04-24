package com.carrental.dao;

import com.carrental.model.CardDetails;
import java.util.List;
import java.util.Optional;

public interface CardDetailsDAO {
    int addCard(CardDetails card);
    Optional<CardDetails> findById(int cardId);
    List<CardDetails> findByUserId(int userId);
    void deleteCard(int cardId);
}

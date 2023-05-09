package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Card;
import ru.netology.web.request.RequestTransfer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/*
transfers - данные о запросах на перевод
cards - номера карт тестовые
codes - коды подтверждения операций
 */
@Repository
public class RepositoryMoney {
    private final Map<String, RequestTransfer> transfers = new ConcurrentHashMap<>();
    private final Map<String, Card> cards = new ConcurrentHashMap<>();
    private final Map<String, String> codes = new ConcurrentHashMap<>();
    private final AtomicInteger operationId = new AtomicInteger();

    public void saveTransfers(String operationId, RequestTransfer request) {
        transfers.put(operationId, request);
    }

    public RequestTransfer getRequestTransfer(String operationId) {
        return transfers.get(operationId);
    }

    public int getId() {
        return operationId.incrementAndGet();
    }

    public void saveCode(String operationId, String code) {
        codes.put(operationId, code);
    }

    public String getCode(String operationId) {
        return codes.get(operationId);
    }

    public void saveCard(String cardNumber, Card card) {
        cards.put(cardNumber, card);
    }

    public Card getCard(String cartNumber) {
        return cards.get(cartNumber);
    }

    public Card searchCard(String cartNumber) {
        for (String s : cards.keySet()) {
            if (s.equals(cartNumber)) {
                return cards.get(s);
            }
        }
        return null;
    }
}

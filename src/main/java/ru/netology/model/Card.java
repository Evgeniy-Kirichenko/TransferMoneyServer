package ru.netology.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Card {
    private String cardNumber;
    private String cardValidTill;
    private String cardCVV;
    private Amount amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (!Objects.equals(cardNumber, card.cardNumber)) return false;
        if (!Objects.equals(cardValidTill, card.cardValidTill))
            return false;
        return Objects.equals(cardCVV, card.cardCVV);
    }

    @Override
    public int hashCode() {
        int result = cardNumber != null ? cardNumber.hashCode() : 0;
        result = 31 * result + (cardValidTill != null ? cardValidTill.hashCode() : 0);
        result = 31 * result + (cardCVV != null ? cardCVV.hashCode() : 0);
        return result;
    }
}

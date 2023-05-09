package ru.netology.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.netology.model.Amount;

@Data
@AllArgsConstructor
public class RequestTransfer {
    private String cardFromNumber;
    private String cardToNumber;
    private String cardFromCVV;
    private String cardFromValidTill;

    private Amount amount;

}

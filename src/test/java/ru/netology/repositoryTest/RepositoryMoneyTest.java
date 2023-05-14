package ru.netology.repositoryTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.netology.model.Amount;
import ru.netology.model.Card;
import ru.netology.repository.RepositoryMoney;
import ru.netology.web.request.RequestTransfer;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testing repositoryMoney functionality")
public class RepositoryMoneyTest {
    @Autowired
    RepositoryMoney repositoryMoney;

    public static final String OPERATION_ID = "1";
    public static final int ID = 1;
    public static final String CONFIRMATION_CODE = "8998";
    public static final RequestTransfer REQUEST_TRANSFER = new RequestTransfer("1111222233334444",
            "5555666677778888","01/27","555",new Amount(10000,"RUB"));
    public static final Card TEST_CARD_FROM = new Card("5555333344440000","01/26","666",new Amount(
            800000,"RUB"));
    public static final Card TEST_CARD_TO = new Card("9999888877776666","01/26","444",new Amount(
            9500000,"RUB"));
    public static final String CARD_NUMBER_FROM = "5555333344440000";
    public static final String CARD_NUMBER_TO = "9999888877776666";
@Test
@DisplayName("Проверка сохранения запроса по адресу /transfer")
    void saveTransferTest (){
       String operationId = OPERATION_ID;
       RequestTransfer requestExp = REQUEST_TRANSFER;
       repositoryMoney.saveTransfers(operationId,requestExp);

    RequestTransfer requestActual = repositoryMoney.getRequestTransfer(operationId);
    assertEquals(requestExp,requestActual);
}

@Test
@DisplayName("Проверка получения сохраненного Request по Id")
void getRequestTransfer_Test(){
    String operationId = OPERATION_ID;
    RequestTransfer requestExp = REQUEST_TRANSFER;
    RequestTransfer requestActual = repositoryMoney.getRequestTransfer(operationId);
    assertEquals(requestExp,requestActual);
}
}

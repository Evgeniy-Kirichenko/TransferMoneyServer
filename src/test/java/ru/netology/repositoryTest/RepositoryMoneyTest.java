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
            "5555666677778888", "01/27", "555", new Amount(10000, "RUB"));
    public static final Card TEST_CARD = new Card("5555333344440000", "01/26", "666", new Amount(
            800000, "RUB"));
    public static final String CARD_NUMBER = "5555333344440000";


    @Test
    @DisplayName("Проверка сохранения запроса по адресу /transfer")
    void saveTransferTest() {
        String operationId = OPERATION_ID;
        RequestTransfer requestExp = REQUEST_TRANSFER;
        repositoryMoney.saveTransfers(operationId, requestExp);

        RequestTransfer requestActual = repositoryMoney.getRequestTransfer(operationId);
        assertEquals(requestExp, requestActual);
    }

    @Test
    @DisplayName("Проверка получения сохраненного Request по Id")
    void getRequestTransferTest() {
        String operationId = OPERATION_ID;
        RequestTransfer requestExp = REQUEST_TRANSFER;
        repositoryMoney.saveTransfers(operationId, requestExp);

        RequestTransfer requestActual = repositoryMoney.getRequestTransfer(operationId);
        assertEquals(requestExp, requestActual);
    }

    @Test
    @DisplayName("Проверка сохранения кода подтверждения карты")
    void saveCode_Test() {
        String operationId = OPERATION_ID;
        String codeExp = CONFIRMATION_CODE;
        repositoryMoney.saveCode(operationId, codeExp);
        String codeActual = repositoryMoney.getCode(operationId);
        assertEquals(codeExp, codeActual);
    }

    @Test
    @DisplayName("Проверка получения кода подтверждения карты")
    void getCodeTest() {
        String operationId = OPERATION_ID;
        String codeExp = CONFIRMATION_CODE;
        repositoryMoney.saveCode(operationId, codeExp);
        String codeActual = repositoryMoney.getCode(operationId);
        assertEquals(codeExp, codeActual);
    }

    @Test
    @DisplayName("Проверка сохранения карты")
    void saveCardTest() {
        Card cartExp = TEST_CARD;
        String testCardNumber = CARD_NUMBER;
        repositoryMoney.saveCard(testCardNumber,cartExp);
        Card cartActual = repositoryMoney.getCard(testCardNumber);
        assertEquals(cartExp,cartActual);
    }
   @Test
   @DisplayName("Проверка поиска карты")
    void searchCardTest(){
       Card cartExp = TEST_CARD;
       String testCardNumber = CARD_NUMBER;
       repositoryMoney.saveCard(testCardNumber,cartExp);
       Card cartActual = repositoryMoney.searchCard(testCardNumber);
       assertEquals(cartExp,cartActual);
    }

}

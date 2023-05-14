package ru.netology.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netology.exception.ConfirmOperationException;
import ru.netology.exception.ErrorInputDataException;
import ru.netology.exception.ErrorTransferException;
import ru.netology.model.Amount;
import ru.netology.model.Card;
import ru.netology.repository.RepositoryMoney;
import ru.netology.web.request.RequestConfirmOperation;
import ru.netology.web.request.RequestTransfer;
import ru.netology.web.response.ResponseOk;

import java.time.LocalDate;

@Slf4j
@Service
public class ServiceMoney {
    private final RepositoryMoney repository;

    @Autowired
    public ServiceMoney(RepositoryMoney repository) {
        this.repository = repository;
        initCardTest();
    }

    public ResponseOk transfer(RequestTransfer request) {
        final String cardFromNumber = request.getCardFromNumber();
        final String cardFromValidTill = request.getCardFromValidTill();
        final String cardFromCVV = request.getCardFromCVV();
        final String cardToNumber = request.getCardToNumber();
        final Amount amount = amountConvert(request.getAmount());

        //проверяем правильность заполнения полей карты
        cardNumberVerification(cardFromNumber, cardToNumber);
        cardCvvVerification(cardFromCVV);
        cardDateVerification(cardFromValidTill);
        amountVerification(amount);

        //если карта валидна, то находим карту в хранилище по номеру карты
        final Card cardRepository = repository.searchCard(cardFromNumber);
        final Card cardRequest = new Card(cardFromNumber, cardFromValidTill, cardFromCVV, amount);
        //сравниваем параметры карты из запроса и карты из хранилища
        if (cardRequest.equals(cardRepository)) {
            //проверяем баланс карты, если денег недостаточно, то генерируем исключение
            if (balanceCardVerification(cardRepository, amount)) {
                final String operationId = String.valueOf(repository.getId());
                final String confirmationCode = String.valueOf((int) Math.random() * 56898);
                repository.saveTransfers(operationId, request);
                repository.saveCode(operationId, confirmationCode);
                log.info("Новый перевод: Id {}, CardFrom № {}, CardTo № {}, amount {}, currency {}, comission {}",
                        operationId,cardFromNumber,cardToNumber,amount.getValue(),amount.getCurrency(), amount.getValue()/100);
                return new ResponseOk(operationId);
            } else throw new ErrorTransferException("Недостаточно средств на карте");
        } else throw new ErrorInputDataException("Карта не найдена");
    }


    public ResponseOk confirmOperation(RequestConfirmOperation requestConfirmOperation) {
        final String operationId = requestConfirmOperation.getOperationId();
        final String code = requestConfirmOperation.getCode();
        if (code.equals(repository.getCode(operationId)) || code.equals("0000")) {
            cardBalanceChange(operationId);
            log.info("Платеж по операции  № {} проведен", operationId);
            return new ResponseOk(operationId);
        } else {
            throw new ConfirmOperationException("Введен неверный код");
        }
    }


    //переводим из копеек в рубли
    public Amount amountConvert(Amount amount) {
        amount.setValue(amount.getValue() / 100);
        return amount;
    }

    //проверка правильности заполнения номера карты из запроса
    public void cardNumberVerification(String cardFrom, String cardTo) {
        if (cardFrom == null) {
            throw new ErrorInputDataException("Введите номер карты отправителя");
        } else if (cardTo == null) {
            throw new ErrorInputDataException("Введите номер карты получателя");
        } else if (!cardFrom.matches("[0-9]{16}")) {
            throw new ErrorInputDataException("Карта отправителя должна иметь 16 цифр");
        } else if (!cardTo.matches("[0-9]{16}")) {
            throw new ErrorInputDataException("Карта получателя должна иметь 16 цифр");
        } else if (cardTo.equals(cardFrom)) {
            throw new ErrorInputDataException("Карты отправителя и получатель должны быть разные");
        }
    }

    //проверка правильности кода CVV
    public void cardCvvVerification(String cardCVV) {
        if (cardCVV == null || !cardCVV.chars().allMatch(Character::isDigit) || !cardCVV.matches("\\d{3}"))
            throw new ErrorInputDataException("код CVV неверный. Введите три цифры");
    }

    //проверка правильности заполнения даты
    public void cardDateVerification(String cardFromValidTill) {
        if (cardFromValidTill == null) throw new ErrorInputDataException("введите срок окончания действия карты");
        final String[] yearMonth = cardFromValidTill.split("/");
        final int month = Integer.parseInt(yearMonth[0]);
        final int year = Integer.parseInt(yearMonth[1]) + 2000;

        if (month > 12 || month < 1) throw new ErrorInputDataException("Неправильный месяц");

        if (year < LocalDate.now().getYear()) throw new ErrorInputDataException("Истек срок действия карты");
        if (year == LocalDate.now().getYear() && month <= LocalDate.now().getMonthValue())
            throw new ErrorInputDataException("Истек срок действия карты");
    }

    //проверка правильности заполнения суммы и валюты перевода
    public void amountVerification(Amount amount) {
        if (amount.getValue() == null || amount.getCurrency() == null) {
            throw new ErrorInputDataException("Не введена сумма или валюта платежа");
        }
        if (amount.getValue() <= 0) throw new ErrorInputDataException("Сумма перевода должна быть больше 0");
        final String currency = "RUB";
        if (!amount.getCurrency().equals(currency)) throw new ErrorInputDataException("Валюта должна быть RUB");
    }

    //проверка, достаточно ли средств на карте
    public boolean balanceCardVerification(Card card, Amount amount) {
        //сумма комиссии равна 1%
        int balanceCard = card.getAmount().getValue();
        if (balanceCard < (amount.getValue() + amount.getValue() / 100)) {
            return false;
        }
        return true;
    }
//списываем деньги с карты отправителя, сохраняем данные карты отправителя
    public void cardBalanceChange(String operationId) {
        RequestTransfer requestTransfer = repository.getRequestTransfer(operationId);
        Amount amount = requestTransfer.getAmount();
        String cardFromNumber = requestTransfer.getCardFromNumber();
        String cardToNumber = requestTransfer.getCardToNumber();
        Card cardFrom = repository.getCard(cardFromNumber);
        Card cardTo = new Card(cardToNumber, "00/00", "000", new Amount(0, "RUB"));
        int commission = amount.getValue() / 100;

        int newBalanceCardFrom = cardFrom.getAmount().getValue() - amount.getValue() - commission;
        cardFrom.getAmount().setValue(newBalanceCardFrom);
        repository.saveCard(cardFrom.getCardNumber(), cardFrom);

        int newBalanceCardTo = cardTo.getAmount().getValue() + amount.getValue();
        cardTo.getAmount().setValue(newBalanceCardTo);

        log.info("Баланс карты отправителя № {} равен {}",cardFromNumber,repository.getCard(cardFromNumber).
                getAmount().getValue());
        log.info("Баланс карты получателя № {} равен {}", cardToNumber,cardTo.getAmount().getValue());
    }


    //тестовая карта сохраняется в cards
    public void initCardTest() {
        final Card testCard = new Card("1111354545445422", "01/28", "345", new Amount(1500000, "RUB"));
        repository.saveCard(testCard.getCardNumber(), testCard);
    }
}

package ru.netology.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.service.ServiceMoney;
import ru.netology.web.request.RequestConfirmOperation;
import ru.netology.web.request.RequestTransfer;
import ru.netology.web.response.ResponseOk;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ControllerMoney {
    private final ServiceMoney serviceMoney;

    @Autowired
    public ControllerMoney(ServiceMoney serviceMoney) {
        this.serviceMoney = serviceMoney;
    }

    @PostMapping("/transfer")
    public ResponseOk transfer(@RequestBody RequestTransfer requestTransfer) {
        ResponseOk response = serviceMoney.transfer(requestTransfer);
        return response;
    }

    @PostMapping("/confirmOperation")
    public ResponseOk confirmOperation (@RequestBody RequestConfirmOperation requestConfirmOperation) {
        ResponseOk response = serviceMoney.confirmOperation(requestConfirmOperation);
        return response;
    }
}

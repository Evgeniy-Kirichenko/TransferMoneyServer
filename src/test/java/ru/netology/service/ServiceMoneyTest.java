package ru.netology.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.model.Amount;
import ru.netology.web.request.RequestConfirmOperation;
import ru.netology.web.request.RequestTransfer;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceMoneyTest {
    @Autowired
    TestRestTemplate restTemplate;
    private static final int PORT = 5500;
    @Container
    private static final GenericContainer<?> testContainer = new GenericContainer<>("myapp")
            .withExposedPorts(PORT);

    @Test
    void contextLoads() {
        Integer testPort = testContainer.getMappedPort(PORT);
        String HOST = "http://localhost:";
        String REQUEST = "/transfer";
        RequestTransfer requestTransferTest = new RequestTransfer("1111354545445422", "1233444455550000", "345", "01/28"
                , new Amount(1500, "RUB"));
        ResponseEntity<String> entity =
                restTemplate.postForEntity(HOST + testPort + REQUEST, requestTransferTest, String.class);
        String idExp = "{\"operationId\":\"1\"}";
        String idActual = entity.getBody();
        Assertions.assertEquals(idExp, idActual);
    }
    @Test
    void contextLoaderConfirmOperationTest(){
        Integer testPort = testContainer.getMappedPort(PORT);
        String HOST = "http://localhost:";
        String REQUEST = "/confirmOperation";
        RequestConfirmOperation requestTest = new RequestConfirmOperation("1","0000");
        ResponseEntity<String> entity =
                restTemplate.postForEntity(HOST + testPort + REQUEST, requestTest, String.class);
        String idExp = "{\"operationId\":\"1\"}";
        String idActual = entity.getBody();
        Assertions.assertEquals(idExp, idActual);

    }

}

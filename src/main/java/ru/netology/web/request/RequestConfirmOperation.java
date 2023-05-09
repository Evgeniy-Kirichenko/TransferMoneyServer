package ru.netology.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestConfirmOperation {
    private String operationId;
    private String code;
}

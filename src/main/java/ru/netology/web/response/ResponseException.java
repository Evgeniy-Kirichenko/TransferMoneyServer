package ru.netology.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseException {
    private String messageError;
    private int id;
}

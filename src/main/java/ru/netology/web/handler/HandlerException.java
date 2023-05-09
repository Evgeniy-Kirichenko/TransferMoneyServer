package ru.netology.web.handler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.netology.exception.ConfirmOperationException;
import ru.netology.exception.ErrorInputDataException;
import ru.netology.exception.ErrorTransferException;
import ru.netology.web.response.ResponseException;
@Slf4j
@ControllerAdvice
public class HandlerException {
    @ExceptionHandler(ErrorInputDataException.class)
    public ResponseEntity<ResponseException> handleErrorInputDataException
            (@NonNull final ErrorInputDataException e){
        String message = "Некорректные данные карты: ";
        log.info(message +e.getMessage());
        return new ResponseEntity<>(new ResponseException(e.getMessage(),400), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ErrorTransferException.class)
    public ResponseEntity<ResponseException> handleErrorTransferException
            (@NonNull final ErrorTransferException e){
        String message = "Ошибка перевода: ";
        log.info(message +e.getMessage());
        return new ResponseEntity<>(new ResponseException(e.getMessage(),500), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConfirmOperationException.class)
    public ResponseEntity<ResponseException> handleConfirmOperationException
            (@NonNull final ConfirmOperationException e){
        String message = "Ошибка подтверждения: ";
        log.info(message +e.getMessage());
        return new ResponseEntity<>(new ResponseException(e.getMessage(),400), HttpStatus.BAD_REQUEST);
    }
}

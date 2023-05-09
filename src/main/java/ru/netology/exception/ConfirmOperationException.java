package ru.netology.exception;

public class ConfirmOperationException extends RuntimeException{
    public ConfirmOperationException(String message) {
        super(message);
    }
}

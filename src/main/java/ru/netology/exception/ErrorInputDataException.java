package ru.netology.exception;

public class ErrorInputDataException extends RuntimeException{
    public ErrorInputDataException(String message) {
        super(message);
    }
}

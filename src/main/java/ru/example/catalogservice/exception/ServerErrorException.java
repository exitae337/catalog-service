package ru.example.catalogservice.exception;

public class ServerErrorException extends RuntimeException {

    public ServerErrorException(String message) {
        super(message);
    }
}

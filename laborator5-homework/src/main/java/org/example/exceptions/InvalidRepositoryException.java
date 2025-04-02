package org.example.exceptions;

public class InvalidRepositoryException extends Exception {
    public InvalidRepositoryException(String message) {
        super(message);
    }
}
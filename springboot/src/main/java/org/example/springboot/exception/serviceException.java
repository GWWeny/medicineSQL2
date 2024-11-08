package org.example.springboot.exception;

import lombok.Getter;

@Getter
public class serviceException extends RuntimeException{
    private final String code;
    public serviceException(String code, String message) {
        super(message);
        this.code = code;
    }
    public serviceException(String message) {
        super(message);
        this.code = "438";
    }
}

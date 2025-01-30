package com.das.acs.exceptions;

import java.io.IOException;

public class ChessEngineException extends RuntimeException {
    public ChessEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}

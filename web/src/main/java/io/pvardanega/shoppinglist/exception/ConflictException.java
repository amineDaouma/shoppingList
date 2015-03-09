package io.pvardanega.shoppinglist.exception;

import static javax.ws.rs.core.Response.Status.CONFLICT;
import javax.ws.rs.WebApplicationException;

public class ConflictException extends WebApplicationException {

    public ConflictException(String message) {
        super(message, CONFLICT);
    }
}

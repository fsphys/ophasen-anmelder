package edu.kit.physik.ophasenanmelder.exception;

import de.m4rc3l.nova.core.exception.HttpException;
import org.springframework.http.HttpStatus;

public class EventTypeHasNoDrawException extends HttpException {

    private final static String TYPE = "EVENT_TYPE_HAS_NOW_DRAW";
    private final static String MESSAGE = "This event type needs no draw participation.";

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.EXPECTATION_FAILED;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}

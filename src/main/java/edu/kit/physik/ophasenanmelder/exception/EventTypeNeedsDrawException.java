package edu.kit.physik.ophasenanmelder.exception;

import de.m4rc3l.nova.core.exception.HttpException;
import org.springframework.http.HttpStatus;

public class EventTypeNeedsDrawException extends HttpException {

    private final static String TYPE = "EVENT_TYPE_NEEDS_DRAW_PARTICIPATION";
    private final static String MESSAGE = "This event type needs a draw participation.";

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

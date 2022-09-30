package edu.kit.physik.ophasenanmelder.exception;

import de.m4rc3l.nova.core.exception.HttpException;
import org.springframework.http.HttpStatus;

public class EventRegistrationNotOpenException extends HttpException {

    private final static String TYPE = "EVENT_REGISTRATION_NOT_OPEN";
    private final static String MESSAGE = "The registration is currently not open.";

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

package edu.kit.physik.ophasenanmelder.exception;

import de.m4rc3l.nova.core.exception.HttpException;
import org.springframework.http.HttpStatus;

public class MailAlreadyRegisteredException extends HttpException {

    private final static String TYPE = "MAIL_ALREADY_REGISTERED_EXCEPTION";
    private final static String MESSAGE = "The mail address is already registered.";

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
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

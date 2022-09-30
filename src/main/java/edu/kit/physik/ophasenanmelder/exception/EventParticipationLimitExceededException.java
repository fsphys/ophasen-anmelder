package edu.kit.physik.ophasenanmelder.exception;

import de.m4rc3l.nova.core.exception.HttpException;
import org.springframework.http.HttpStatus;

public class EventParticipationLimitExceededException extends HttpException {

    private final static String TYPE = "EVENT_PARTICIPATION_LIMIT_EXCEEDED";
    private final static String MESSAGE = "The maximum number of participants is exceeded.";

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

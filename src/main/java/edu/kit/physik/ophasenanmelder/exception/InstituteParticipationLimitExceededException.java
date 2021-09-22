package edu.kit.physik.ophasenanmelder.exception;

import net.getnova.framework.core.exception.HttpException;
import org.springframework.http.HttpStatus;

public class InstituteParticipationLimitExceededException extends HttpException {

    private final static String TYPE = "INSTITUTE_PARTICIPATION_LIMIT_EXCEEDED";
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

package edu.kit.physik.ophasenanmelder.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.getnova.framework.core.Validatable;
import net.getnova.framework.core.exception.ValidationException;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class EventType implements Validatable {

    private final UUID id;
    private final String name;
    private final OffsetDateTime registrationStartTime;
    private final OffsetDateTime registrationEndTime;

    @JsonCreator
    public EventType(final String name,
                     final OffsetDateTime registrationStartTime,
                     final OffsetDateTime registrationEndTime) {
        this.id = null;
        this.name = name;
        this.registrationStartTime = registrationStartTime;
        this.registrationEndTime = registrationEndTime;
    }

    @Override
    public void validate() throws ValidationException {
        if (this.name == null || this.name.isBlank())
            throw new ValidationException("name", "NOT_BLANK");

        if (this.name.length() > 255)
            throw new ValidationException("name", "TOO_LONG");

        if (this.registrationStartTime == null)
            throw new ValidationException("startTime", "NOT_NULL");

        if (this.registrationEndTime == null)
            throw new ValidationException("startTime", "NOT_NULL");

        if (this.registrationEndTime.isBefore(this.registrationStartTime))
            throw new ValidationException("registrationEndTime", "AFTER_REGISTRATION_START_TIME");
    }
}

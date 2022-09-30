package edu.kit.physik.ophasenanmelder.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.m4rc3l.nova.core.Validatable;
import de.m4rc3l.nova.core.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class EventType implements Validatable {

    private final UUID id;
    private final String name;
    private final OffsetDateTime registrationStartTime;
    private final OffsetDateTime registrationEndTime;

    private UUID eventDrawId;

    @JsonCreator
    public EventType(final String name,
                     final OffsetDateTime registrationStartTime,
                     final OffsetDateTime registrationEndTime,
                     final UUID eventDrawId) {
        this.id = null;
        this.name = name;
        this.registrationStartTime = registrationStartTime;
        this.registrationEndTime = registrationEndTime;
        this.eventDrawId = eventDrawId;
    }

    @Override
    public void validate() throws ValidationException {
        if (this.name == null || this.name.isBlank())
            throw new ValidationException("name", "NOT_BLANK");

        if (this.name.length() > 255)
            throw new ValidationException("name", "TOO_LONG");

        if (this.registrationStartTime == null)
            throw new ValidationException("registrationStartTime", "NOT_NULL");

        if (this.registrationEndTime == null)
            throw new ValidationException("registrationEndTime", "NOT_NULL");

        if (this.registrationEndTime.isBefore(this.registrationStartTime))
            throw new ValidationException("registrationEndTime", "AFTER_REGISTRATION_START_TIME");
    }
}

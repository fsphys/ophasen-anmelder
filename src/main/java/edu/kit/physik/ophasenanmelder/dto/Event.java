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
public class Event implements Validatable {

    private final UUID id;
    private final UUID eventTypeId;
    private final String name;
    private final String description;
    private final Integer maxParticipants;
    private final OffsetDateTime startTime;
    private final OffsetDateTime registrationStartTime;
    private final OffsetDateTime registrationEndTime;

    @JsonCreator
    public Event(final UUID eventTypeId,
                 final String name,
                 final String description,
                 final Integer maxParticipants,
                 final OffsetDateTime startTime,
                 final OffsetDateTime registrationStartTime,
                 final OffsetDateTime registrationEndTime) {
        this.id = null;
        this.eventTypeId = eventTypeId;
        this.name = name;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.startTime = startTime;
        this.registrationStartTime = registrationStartTime;
        this.registrationEndTime = registrationEndTime;
    }

    @Override
    public void validate() throws ValidationException {
        if (this.eventTypeId == null)
            throw new ValidationException("eventTypeId", "NOT_NULL");

        if (this.name == null || this.name.isBlank())
            throw new ValidationException("name", "NOT_BLANK");

        if (this.name.length() > 255)
            throw new ValidationException("name", "TOO_LONG");

        if (this.description == null || this.description.isBlank())
            throw new ValidationException("description", "NOT_BLANK");

        if (this.maxParticipants == null || this.maxParticipants <= 0)
            throw new ValidationException("maxParticipants", "MAX_PARTICIPANTS_POSITIVE");

        if (this.startTime == null)
            throw new ValidationException("startTime", "NOT_NULL");

        if (this.registrationStartTime == null)
            throw new ValidationException("startTime", "NOT_NULL");

        if (this.registrationEndTime == null)
            throw new ValidationException("startTime", "NOT_NULL");

        if (this.registrationEndTime.isBefore(this.registrationStartTime))
            throw new ValidationException("registrationEndTime", "AFTER_REGISTRATION_START_TIME");
    }
}

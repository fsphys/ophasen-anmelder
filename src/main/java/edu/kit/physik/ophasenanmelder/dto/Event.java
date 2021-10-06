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
    private final Integer freeSpots;
    private final Boolean needsHasTicket;
    private final Boolean needsBirthInformation;

    @JsonCreator
    public Event(final UUID eventTypeId,
                 final String name,
                 final String description,
                 final Integer maxParticipants,
                 final OffsetDateTime startTime,
                 final Boolean needsHasTicket,
                 final Boolean needsBirthInformation) {
        this.id = null;
        this.eventTypeId = eventTypeId;
        this.name = name;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.startTime = startTime;
        this.freeSpots = null;
        this.needsHasTicket = needsHasTicket;
        this.needsBirthInformation = needsBirthInformation;
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

        if (this.needsHasTicket == null)
            throw new ValidationException("needsHasTicket", "NOT_NULL");

        if (this.needsBirthInformation == null)
            throw new ValidationException("needsBirthInformation", "NOT_NULL");
    }
}

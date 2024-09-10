package edu.kit.physik.ophasenanmelder.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.m4rc3l.nova.core.Validatable;
import de.m4rc3l.nova.core.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;

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
    private final Integer freeSpots;
    private final Boolean needsBirthInformation;
    private final OffsetDateTime meetingTime;
    private final String meetingPoint;

    @JsonCreator
    public Event(final UUID eventTypeId,
                 final String name,
                 final String description,
                 final Integer maxParticipants,
                 final Boolean needsBirthInformation,
                 final OffsetDateTime meetingTime,
                 final String meetingPoint) {
        this.id = null;
        this.eventTypeId = eventTypeId;
        this.name = name;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.freeSpots = null;
        this.needsBirthInformation = needsBirthInformation;
        this.meetingTime = meetingTime;
        this.meetingPoint = meetingPoint;
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

        if (this.needsBirthInformation == null)
            throw new ValidationException("needsBirthInformation", "NOT_NULL");
    }
}

package edu.kit.physik.ophasenanmelder.dto;

import de.m4rc3l.nova.core.Validatable;
import de.m4rc3l.nova.core.exception.ValidationException;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class EventDraw implements Validatable {

    private final UUID id;

    private final OffsetDateTime drawTime;

    private final Boolean drawn;

    public EventDraw(final UUID id,
                     final OffsetDateTime drawTime,
                     final Boolean drawn) {
        this.id = id;
        this.drawTime = drawTime;
        this.drawn = drawn;
    }

    @Override
    public void validate() throws ValidationException {
        if (this.id == null)
            throw new ValidationException("id", "NOT_NULL");

        if (this.drawn == null)
            throw new ValidationException("drawn", "NOT_NULL");

        if (this.drawTime != null && (this.drawTime.isBefore(OffsetDateTime.now())) && !this.drawn)
            throw new ValidationException("drawTime", "IN_FUTURE");

    }
}

package edu.kit.physik.ophasenanmelder.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.getnova.framework.core.Validatable;
import net.getnova.framework.core.exception.ValidationException;

import java.util.UUID;

@Data
@AllArgsConstructor
public class EventType implements Validatable {

    private final UUID id;
    private final String name;

    @JsonCreator
    public EventType(final String name) {
        this.id = null;
        this.name = name;
    }

    @Override
    public void validate() throws ValidationException {
        if (this.name == null || this.name.isBlank())
            throw new ValidationException("name", "NOT_BLANK");

        if (this.name.length() > 255)
            throw new ValidationException("name", "TOO_LONG");
    }
}

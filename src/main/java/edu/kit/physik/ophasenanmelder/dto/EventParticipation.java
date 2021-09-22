package edu.kit.physik.ophasenanmelder.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.getnova.framework.core.Validatable;
import net.getnova.framework.core.exception.ValidationException;

import java.util.UUID;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
public class EventParticipation implements Validatable {

    private static final Pattern MAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private final UUID id;
    private final UUID eventId;
    private final String surname;
    private final String givenName;
    private final String mail;

    @JsonCreator
    public EventParticipation(final UUID eventId, final String surname, final String givenName, final String mail) {
        this.id = null;
        this.eventId = eventId;
        this.surname = surname;
        this.givenName = givenName;
        this.mail = mail;
    }

    @Override
    public void validate() throws ValidationException {
        if (this.eventId == null)
            throw new ValidationException("eventId", "NOT_NULL");

        if (this.surname == null || this.surname.isBlank())
            throw new ValidationException("surname", "NOT_BLANK");

        if (this.givenName == null || this.givenName.isBlank())
            throw new ValidationException("givenName", "NOT_BLANK");

        if (this.mail == null || !MAIL_PATTERN.matcher(this.mail).matches())
            throw new ValidationException("mail", "VALID_MAIL_ADDRESS");
    }
}

package edu.kit.physik.ophasenanmelder.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.m4rc3l.nova.core.Validatable;
import de.m4rc3l.nova.core.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;
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
    private final OffsetDateTime birthDate;
    private final String birthPlace;

    @JsonCreator
    public EventParticipation(final UUID eventId,
                              final String surname,
                              final String givenName,
                              final String mail,
                              final OffsetDateTime birthDate,
                              final String birthPlace) {
        this.id = null;
        this.eventId = eventId;
        this.surname = surname;
        this.givenName = givenName;
        this.mail = mail;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
    }

    @Override
    public void validate() throws ValidationException {
        if (this.eventId == null)
            throw new ValidationException("eventId", "NOT_NULL");

        if (this.surname == null || this.surname.isBlank())
            throw new ValidationException("surname", "NOT_BLANK");

        if (this.surname.length() > 255)
            throw new ValidationException("surname", "MAX_LENGTH");

        if (this.givenName == null || this.givenName.isBlank())
            throw new ValidationException("givenName", "NOT_BLANK");

        if (this.givenName.length() > 255)
            throw new ValidationException("giveName", "MAX_LENGTH");

        if (this.mail == null || !MAIL_PATTERN.matcher(this.mail).matches())
            throw new ValidationException("mail", "VALID_MAIL_ADDRESS");

        if (this.mail.length() > 255)
            throw new ValidationException("mail", "MAX_LENGTH");

        if (this.birthDate != null && this.birthDate.isAfter(OffsetDateTime.now()))
            throw new ValidationException("birthDate", "IN_PAST");

        if (this.birthPlace != null && this.birthPlace.isBlank())
            throw new ValidationException("birthPlace", "NOT_BLANK");

        if (this.birthPlace != null && this.birthPlace.length() > 255)
            throw new ValidationException("birthPlace", "MAX_LENGTH");
    }
}

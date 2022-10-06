package edu.kit.physik.ophasenanmelder.services.impl;

import edu.kit.physik.ophasenanmelder.converter.EventParticipationConverter;
import edu.kit.physik.ophasenanmelder.dto.Event;
import edu.kit.physik.ophasenanmelder.dto.EventParticipation;
import edu.kit.physik.ophasenanmelder.dto.EventType;
import edu.kit.physik.ophasenanmelder.exception.EventParticipationLimitExceededException;
import edu.kit.physik.ophasenanmelder.exception.EventRegistrationNotOpenException;
import edu.kit.physik.ophasenanmelder.exception.EventTypeNeedsDrawException;
import edu.kit.physik.ophasenanmelder.exception.MailAlreadyRegisteredException;
import edu.kit.physik.ophasenanmelder.model.EventParticipationModel;
import edu.kit.physik.ophasenanmelder.repository.EventParticipationRepository;
import edu.kit.physik.ophasenanmelder.services.EventParticipationService;
import edu.kit.physik.ophasenanmelder.services.EventService;
import edu.kit.physik.ophasenanmelder.services.EventTypeService;
import de.m4rc3l.nova.core.exception.NotFoundException;
import de.m4rc3l.nova.core.exception.ValidationException;
import de.m4rc3l.nova.core.service.AbstractCommonIdCrudService;
import de.m4rc3l.nova.core.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class EventParticipationServiceImpl extends AbstractCommonIdCrudService<EventParticipation, UUID, EventParticipationModel> implements EventParticipationService {

    private static final String MAIL_REGISTRATION_TEXT = """
            Hallo %s,

            deine Anmeldung bei "%s %s" war erfolgreich.
            Benutze diesen Link, um dich abzumelden: %s/participation/%s
            Viel Spaß bei der Veranstaltung.


            %s %s

            %s
                        

            Dies ist eine automatisch generierte Nachricht, bitte antworte nicht darauf.
            """;

    private static final String MAIL_UN_REGISTRATION_TEXT = """
            Hallo %s

            deine Abmeldung bei "%s %s" war erfolgreich.

            Dies ist eine automatisch generierte Nachricht, bitte antworte nicht darauf.
            """;

    private final EventTypeService eventTypeService;
    private final EventService eventService;
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final String frontendUrl;

    public EventParticipationServiceImpl(final EventParticipationRepository repository,
                                         final EventParticipationConverter converter,
                                         final EventTypeService eventTypeService,
                                         final EventService eventService,
                                         final JavaMailSender mailSender,
                                         final MailProperties mailProperties,
                                         @Value("${FRONTEND_URL:http://localhost:4200}") final String frontendUrl) {
        super("EVENT_PARTICIPATION", repository, converter);
        this.eventTypeService = eventTypeService;
        this.eventService = eventService;
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
        this.frontendUrl = frontendUrl;
    }

    @Override
    public EventParticipation save(final EventParticipation dto) {
        ValidationUtils.validate(dto);

        final Event event = this.eventService.findById(dto.getEventId());
        final EventType eventType = this.eventTypeService.findById(event.getEventTypeId());
        if (this.eventTypeService.eventTypeHasDrawEnabled(eventType)) {
            throw new EventTypeNeedsDrawException();
        }

        final OffsetDateTime now = OffsetDateTime.now();

        if (event.getNeedsBirthInformation() && dto.getBirthDate() == null)
            throw new ValidationException("birthDate", "NOT_NULL");

        if (event.getNeedsBirthInformation() && dto.getBirthPlace() == null)
            throw new ValidationException("birthPlace", "NOT_NULL");

        if (!event.getNeedsBirthInformation() && dto.getBirthDate() != null)
            throw new ValidationException("birthDate", "NULL");

        if (!event.getNeedsBirthInformation() && dto.getBirthPlace() != null)
            throw new ValidationException("birthPlace", "NULL");

        if (now.isBefore(eventType.getRegistrationStartTime()) || now.isAfter(eventType.getRegistrationEndTime()))
            throw new EventRegistrationNotOpenException();

        if (event.getMaxParticipants() <= countParticipants(dto.getEventId()))
            throw new EventParticipationLimitExceededException();

        if (((EventParticipationRepository) this.repository).countAllByEventEventTypeIdAndMail(eventType.getId(), dto.getMail()) > 0)
            throw new MailAlreadyRegisteredException();

        final EventParticipation eventParticipation = this.converter.toDto(this.repository.save(this.converter.toModel(dto)));
        try {
            this.sendRegistrationMail(eventParticipation);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException(e);
        }
        return eventParticipation;
    }

    @Override
    public void delete(UUID id) {
        final EventParticipationModel eventParticipation = this.repository.findById(id).orElseThrow(() -> new NotFoundException(this.name));

        try {
            this.sendUnRegistrationMail(eventParticipation);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException(e);
        }

        this.repository.delete(eventParticipation);
    }

    @Override
    public int countParticipants(final UUID eventId) {
        return ((EventParticipationRepository) this.repository).countAllByEventId(eventId);
    }

    @Override
    public void sendRegistrationMail(final EventParticipation participation) throws UnsupportedEncodingException, MessagingException {
        final MimeMessage message = this.mailSender.createMimeMessage();
        message.addFrom(new Address[]{new InternetAddress(this.mailProperties.getUsername(),
                this.mailProperties.getProperties().get("sender"),
                StandardCharsets.UTF_8.name())});
        message.setSubject("Deine Anmeldung wurde bestätigt");
        message.setRecipients(Message.RecipientType.TO, participation.getMail());

        final Event event = this.eventService.findById(participation.getEventId());
        final EventType eventType = this.eventTypeService.findById(event.getEventTypeId());
        message.setText(String.format(
                MAIL_REGISTRATION_TEXT,
                participation.getGivenName(),
                eventType.getName(),
                event.getName(),
                this.frontendUrl,
                participation.getId(),
                eventType.getName(),
                event.getName(),
                event.getDescription()
        ), StandardCharsets.UTF_8.name());

        this.mailSender.send(message);
    }

    @Override
    public void sendUnRegistrationMail(final EventParticipationModel participation) throws UnsupportedEncodingException, MessagingException {
        final MimeMessage message = this.mailSender.createMimeMessage();
        message.addFrom(new Address[]{new InternetAddress(this.mailProperties.getUsername(),
                this.mailProperties.getProperties().get("sender"),
                StandardCharsets.UTF_8.name())});
        message.setSubject("Deine Abmeldung wurde bestätigt");
        message.setRecipients(Message.RecipientType.TO, participation.getMail());

        final Event event = this.eventService.findById(participation.getEvent().getId());
        final EventType eventType = this.eventTypeService.findById(event.getEventTypeId());

        message.setText(String.format(
                MAIL_UN_REGISTRATION_TEXT,
                participation.getGivenName(),
                eventType.getName(),
                event.getName()
        ), StandardCharsets.UTF_8.name());

        this.mailSender.send(message);
    }
}

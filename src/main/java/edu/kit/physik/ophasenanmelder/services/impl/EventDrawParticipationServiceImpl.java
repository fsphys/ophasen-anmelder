package edu.kit.physik.ophasenanmelder.services.impl;

import de.m4rc3l.nova.core.exception.NotFoundException;
import de.m4rc3l.nova.core.exception.ValidationException;
import de.m4rc3l.nova.core.service.AbstractCommonIdCrudService;
import de.m4rc3l.nova.core.utils.ValidationUtils;
import edu.kit.physik.ophasenanmelder.converter.EventDrawParticipationConverter;
import edu.kit.physik.ophasenanmelder.dto.Event;
import edu.kit.physik.ophasenanmelder.dto.EventDraw;
import edu.kit.physik.ophasenanmelder.dto.EventDrawParticipation;
import edu.kit.physik.ophasenanmelder.dto.EventType;
import edu.kit.physik.ophasenanmelder.exception.EventRegistrationNotOpenException;
import edu.kit.physik.ophasenanmelder.exception.EventTypeHasNoDrawException;
import edu.kit.physik.ophasenanmelder.exception.MailAlreadyRegisteredException;
import edu.kit.physik.ophasenanmelder.model.EventDrawParticipationModel;
import edu.kit.physik.ophasenanmelder.model.EventParticipationModel;
import edu.kit.physik.ophasenanmelder.repository.EventDrawParticipationRepository;
import edu.kit.physik.ophasenanmelder.repository.EventParticipationRepository;
import edu.kit.physik.ophasenanmelder.services.EventDrawParticipationService;
import edu.kit.physik.ophasenanmelder.services.EventDrawService;
import edu.kit.physik.ophasenanmelder.services.EventService;
import edu.kit.physik.ophasenanmelder.services.EventTypeService;
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
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventDrawParticipationServiceImpl extends AbstractCommonIdCrudService<EventDrawParticipation, UUID, EventDrawParticipationModel> implements EventDrawParticipationService {

    private static final String MAIL_REGISTRATION_TEXT = """
            Hallo %s,

            deine Anmeldung zur Verlosung bei "%s %s" war erfolgreich.
            Benutze diesen Link, um dich abzumelden: %s/draw/participation/%s
            Ob du einen Platz erhalten hast, erfährst du am %s Uhr.


            %s %s

            %s
                        

            Dies ist eine automatisch generierte Nachricht, bitte antworte nicht darauf.
            """;
    private static final String MAIL_UN_REGISTRATION_TEXT = """
            Hallo %s

            deine Abmeldung von der Verlosung bei "%s %s" war erfolgreich.

            Dies ist eine automatisch generierte Nachricht, bitte antworte nicht darauf.
            """;
    private final EventService eventService;
    private final EventTypeService eventTypeService;
    private final EventDrawService eventDrawService;
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final String frontendUrl;

    public EventDrawParticipationServiceImpl(final EventDrawParticipationRepository repository,
                                             final EventDrawParticipationConverter converter,
                                             final EventService eventService,
                                             final EventTypeService eventTypeService,
                                             final EventDrawService eventDrawService,
                                             final JavaMailSender mailSender,
                                             final MailProperties mailProperties,
                                             @Value("${FRONTEND_URL:http://localhost:4200}") final String frontendUrl) {
        super("EVENT_DRAW_PARTICIPATION", repository, converter);
        this.eventService = eventService;
        this.eventTypeService = eventTypeService;
        this.eventDrawService = eventDrawService;
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
        this.frontendUrl = frontendUrl;
    }

    @Override
    public EventDrawParticipation save(final EventDrawParticipation dto) {
        dto.setMail(dto.getMail().toLowerCase().strip());
        ValidationUtils.validate(dto);

        final Event event = this.eventService.findById(dto.getEventId());
        final EventType eventType = this.eventTypeService.findById(event.getEventTypeId());
        final EventDraw eventDraw = this.eventDrawService.findById(eventType.getEventDrawId());
        if (!this.eventTypeService.eventTypeHasDrawEnabled(eventType)) {
            throw new EventTypeHasNoDrawException();
        }

        final OffsetDateTime now = OffsetDateTime.now();

        if (eventDraw.getDrawTime().isBefore(now)) {
            throw new EventRegistrationNotOpenException();
        }

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

        if (((EventDrawParticipationRepository) this.repository).existsByMailAndEventId(dto.getMail(), event.getId())) {
            throw new MailAlreadyRegisteredException();
        }

        final EventDrawParticipation eventDrawParticipation = this.converter.toDto(this.repository.save(this.converter.toModel(dto)));
        try {
            this.sendDrawRegistrationMail(eventDrawParticipation);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException(e);
        }
        return eventDrawParticipation;
    }

    @Override
    public void delete(final UUID id) {
        final EventDrawParticipationModel eventDrawParticipationModel = this.repository.findById(id).orElseThrow(() -> new NotFoundException(this.name));

        try {
            this.sendDrawUnRegistrationMail(eventDrawParticipationModel);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException(e);
        }

        this.repository.delete(eventDrawParticipationModel);
    }

    @Override
    public void sendDrawRegistrationMail(final EventDrawParticipation drawParticipation) throws UnsupportedEncodingException, MessagingException {
        final MimeMessage message = this.mailSender.createMimeMessage();
        message.addFrom(new Address[]{new InternetAddress(this.mailProperties.getUsername(),
                this.mailProperties.getProperties().get("sender"),
                StandardCharsets.UTF_8.name())});
        message.setSubject("Deine Anmeldung zur Verlosung wurde bestätigt");
        message.setRecipients(Message.RecipientType.TO, drawParticipation.getMail());

        final Event event = this.eventService.findById(drawParticipation.getEventId());
        final EventType eventType = this.eventTypeService.findById(event.getEventTypeId());
        final EventDraw eventDraw = this.eventDrawService.findById(eventType.getEventDrawId());
        message.setText(String.format(
                MAIL_REGISTRATION_TEXT,
                drawParticipation.getGivenName(),
                eventType.getName(),
                event.getName(),
                this.frontendUrl,
                drawParticipation.getId(),
                eventDraw.getDrawTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                eventType.getName(),
                event.getName(),
                event.getDescription()
        ), StandardCharsets.UTF_8.name());

        this.mailSender.send(message);
    }

    @Override
    public void sendDrawUnRegistrationMail(final EventDrawParticipationModel drawParticipation) throws UnsupportedEncodingException, MessagingException {
        final MimeMessage message = this.mailSender.createMimeMessage();
        message.addFrom(new Address[]{new InternetAddress(this.mailProperties.getUsername(),
                this.mailProperties.getProperties().get("sender"),
                StandardCharsets.UTF_8.name())});
        message.setSubject("Deine Abmeldung von der Verlosung wurde bestätigt");
        message.setRecipients(Message.RecipientType.TO, drawParticipation.getMail());

        final Event event = this.eventService.findById(drawParticipation.getEvent().getId());
        final EventType eventType = this.eventTypeService.findById(event.getEventTypeId());

        message.setText(String.format(
                MAIL_UN_REGISTRATION_TEXT,
                drawParticipation.getGivenName(),
                eventType.getName(),
                event.getName()
        ), StandardCharsets.UTF_8.name());

        this.mailSender.send(message);
    }
}

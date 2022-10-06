package edu.kit.physik.ophasenanmelder.services.impl;

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
import edu.kit.physik.ophasenanmelder.repository.EventDrawParticipationRepository;
import edu.kit.physik.ophasenanmelder.repository.EventParticipationRepository;
import edu.kit.physik.ophasenanmelder.services.EventDrawParticipationService;
import edu.kit.physik.ophasenanmelder.services.EventDrawService;
import edu.kit.physik.ophasenanmelder.services.EventService;
import edu.kit.physik.ophasenanmelder.services.EventTypeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.UUID;

public class EventDrawParticipationServiceImpl extends AbstractCommonIdCrudService<EventDrawParticipation, UUID, EventDrawParticipationModel> implements EventDrawParticipationService {

    private static final String MAIL_REGISTRATION_TEXT = """
            Hallo %s,

            deine Anmeldung bei "%s %s" war erfolgreich.
            Benutze diesen Link, um dich abzumelden: %s/participation/%s
            Viel Spaß bei der Veranstaltung.


            %s %s

            %s
                        

            Dies ist eine automatisch generierte Nachricht, bitte antworte nicht darauf.
            """;

    private final EventService eventService;
    private final EventTypeService eventTypeService;
    private final EventParticipationRepository eventParticipationRepository;
    private final EventDrawService eventDrawService;
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final String frontendUrl;

    public EventDrawParticipationServiceImpl(final EventDrawParticipationRepository repository,
                                             final EventDrawParticipationConverter converter,
                                             final EventService eventService,
                                             final EventTypeService eventTypeService,
                                             final EventParticipationRepository eventParticipationRepository,
                                             final EventDrawService eventDrawService,
                                             final JavaMailSender mailSender,
                                             final MailProperties mailProperties,
                                             @Value("${FRONTEND_URL:http://localhost:4200}") final String frontendUrl) {
        super("EVENT_DRAW_PARTICIPATION", repository, converter);
        this.eventService = eventService;
        this.eventTypeService = eventTypeService;
        this.eventParticipationRepository = eventParticipationRepository;
        this.eventDrawService = eventDrawService;
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
        this.frontendUrl = frontendUrl;
    }

    @Override
    public EventDrawParticipation save(final EventDrawParticipation dto) {
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

        if (this.eventParticipationRepository.countAllByEventEventTypeIdAndMail(eventType.getId(), dto.getMail()) > 0)
            throw new MailAlreadyRegisteredException();

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
        super.delete(id);
    }

    @Override
    public void sendDrawRegistrationMail(final EventDrawParticipation drawParticipation) throws UnsupportedEncodingException, MessagingException {
        final MimeMessage message = this.mailSender.createMimeMessage();
        message.addFrom(new Address[]{new InternetAddress(this.mailProperties.getUsername(),
                this.mailProperties.getProperties().get("sender"),
                StandardCharsets.UTF_8.name())});
        message.setSubject("Deine Verlosung-Anmeldung wurde bestätigt");
        message.setRecipients(Message.RecipientType.TO, drawParticipation.getMail());

        final Event event = this.eventService.findById(drawParticipation.getEventId());
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
}

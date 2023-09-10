package edu.kit.physik.ophasenanmelder.services.impl;

import de.m4rc3l.nova.core.exception.ValidationException;
import de.m4rc3l.nova.core.service.AbstractCommonIdCrudService;
import edu.kit.physik.ophasenanmelder.converter.EventDrawConverter;
import edu.kit.physik.ophasenanmelder.converter.EventParticipationConverter;
import edu.kit.physik.ophasenanmelder.converter.EventTypeConverter;
import edu.kit.physik.ophasenanmelder.dto.Event;
import edu.kit.physik.ophasenanmelder.dto.EventDraw;
import edu.kit.physik.ophasenanmelder.dto.EventDrawParticipation;
import edu.kit.physik.ophasenanmelder.dto.EventParticipation;
import edu.kit.physik.ophasenanmelder.dto.EventType;
import edu.kit.physik.ophasenanmelder.exception.EventDrawTooEarlyException;
import edu.kit.physik.ophasenanmelder.exception.EventTypeAlreadyDrawnException;
import edu.kit.physik.ophasenanmelder.exception.EventTypeHasNoDrawException;
import edu.kit.physik.ophasenanmelder.model.EventDrawModel;
import edu.kit.physik.ophasenanmelder.model.EventTypeModel;
import edu.kit.physik.ophasenanmelder.repository.EventDrawParticipationRepository;
import edu.kit.physik.ophasenanmelder.repository.EventDrawRepository;
import edu.kit.physik.ophasenanmelder.repository.EventParticipationRepository;
import edu.kit.physik.ophasenanmelder.repository.EventTypeRepository;
import edu.kit.physik.ophasenanmelder.services.EventDrawService;
import edu.kit.physik.ophasenanmelder.services.EventService;
import edu.kit.physik.ophasenanmelder.services.EventTypeService;
import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

@Service
public class EventTypeServiceImpl extends AbstractCommonIdCrudService<EventType, UUID, EventTypeModel> implements EventTypeService {

    private static final String MAIL_REGISTRATION_TEXT = """
            Hallo %s,

            deine Anmeldung bei "%s %s" war erfolgreich.
            Benutze diesen Link, um dich abzumelden: %s/participation/%s
            Viel Spaß bei der Veranstaltung.


            %s %s

            %s
                        

            Dies ist eine automatisch generierte Nachricht, bitte antworte nicht darauf.
            """;

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final String frontendUrl;
    private final EventService eventService;
    private final EventDrawService eventDrawService;
    private final EventDrawConverter eventDrawConverter;
    private final EventDrawRepository eventDrawRepository;
    private final EventParticipationConverter eventParticipationConverter;
    private final EventParticipationRepository eventParticipationRepository;
    private final EventDrawParticipationRepository eventDrawParticipationRepository;

    public EventTypeServiceImpl(final JavaMailSender mailSender,
                                final MailProperties mailProperties,
                                @Value("${FRONTEND_URL:http://localhost:4200}") final String frontendUrl,
                                final EventTypeRepository repository,
                                final EventTypeConverter converter,
                                final EventService eventService,
                                final EventDrawService eventDrawService,
                                final EventDrawConverter eventDrawConverter,
                                final EventDrawRepository eventDrawRepository,
                                final EventParticipationConverter eventParticipationConverter,
                                final EventParticipationRepository eventParticipationRepository,
                                final EventDrawParticipationRepository eventDrawParticipationRepository) {
        super("EVENT_TYPE", repository, converter);
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
        this.frontendUrl = frontendUrl;
        this.eventService = eventService;
        this.eventDrawService = eventDrawService;
        this.eventDrawConverter = eventDrawConverter;
        this.eventDrawRepository = eventDrawRepository;
        this.eventParticipationConverter = eventParticipationConverter;
        this.eventParticipationRepository = eventParticipationRepository;
        this.eventDrawParticipationRepository = eventDrawParticipationRepository;
    }

    @Override
    public EventType save(final EventType dto) {
        if (((EventTypeRepository) this.repository).findByName(dto.getName()).isPresent())
            throw new ValidationException("name", "UNIQUE");

        return super.save(dto);
    }

    @Override
    public void delete(final UUID id) {
        this.eventService.findAllByType(id).forEach(event -> this.eventService.delete(event.getId()));
        super.delete(id);
    }

    @Override
    public boolean eventTypeHasDrawEnabled(final EventType eventType) {
        if (eventType.getEventDrawId() == null) return false;
        final EventDraw eventDraw = this.eventDrawService.findById(eventType.getEventDrawId());
        return !eventDraw.getDrawn();
    }

    @Override
    public void draw(final UUID id) {
        final EventType eventType = this.findById(id);
        if (eventType.getEventDrawId() == null) {
            throw new EventTypeHasNoDrawException();
        }
        final EventDraw eventDraw = this.eventDrawService.findById(eventType.getEventDrawId());
        final OffsetDateTime now = OffsetDateTime.now();
        if (!eventDraw.getDrawTime().isAfter(now)) {
            throw new EventDrawTooEarlyException();
        }

        if (eventDraw.getDrawn()) {
            throw new EventTypeAlreadyDrawnException();
        }

        this.evaluate(eventType);
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
        final EventType eventType = this.findById(event.getEventTypeId());
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

    void evaluate(final EventType eventType) {
        final EventDraw eventDraw = this.eventDrawService.findById(eventType.getEventDrawId());
        final Set<Event> events = this.eventService.findAllByType(eventType.getId());
        final TreeMap<Integer, Event> eventDrawParticipationCountMap = new TreeMap<>();
        for (Event event : events) {
            final Set<EventDrawParticipation> allDrawParticipationByEvent = this.eventService.getAllDrawParticipationByEvent(event);
            eventDrawParticipationCountMap.put(allDrawParticipationByEvent.size(), event);
        }

        Map.Entry<Integer, Event> entry;
        while ((entry = eventDrawParticipationCountMap.pollLastEntry()) != null) {
            Event event = entry.getValue();
            this.evaluateEvent(event);
            events.remove(event);

            for (Event event1 : events) {
                final Set<EventDrawParticipation> allDrawParticipationByEvent = this.eventService.getAllDrawParticipationByEvent(event1);
                eventDrawParticipationCountMap.clear();
                eventDrawParticipationCountMap.put(allDrawParticipationByEvent.size(), event1);
            }
        }

        eventDraw.setDrawn(true);
        final EventDrawModel eventDrawModel = this.eventDrawRepository.findById(eventDraw.getId()).orElseThrow();
        this.eventDrawConverter.override(eventDrawModel, eventDraw);
        this.eventDrawRepository.save(eventDrawModel);
    }

    void evaluateEvent(final Event event) {
        final List<EventDrawParticipation> drawParticipationList = new ArrayList<>(this.eventService.getAllDrawParticipationByEvent(event));
        Collections.shuffle(drawParticipationList);
        for (int i = 0; i < event.getMaxParticipants(); i++) {
            if (i >= drawParticipationList.size()) break;
            final EventDrawParticipation eventDrawParticipation = drawParticipationList.get(i);

            EventParticipation dto = new EventParticipation(
                    eventDrawParticipation.getEventId(),
                    eventDrawParticipation.getSurname(),
                    eventDrawParticipation.getGivenName(),
                    eventDrawParticipation.getMail(),
                    eventDrawParticipation.getBirthDate(),
                    eventDrawParticipation.getBirthPlace()
            );
            final EventParticipation eventParticipation = this.eventParticipationConverter.toDto(
                    this.eventParticipationRepository.save(this.eventParticipationConverter.toModel(dto))
            );
            try {
                this.sendRegistrationMail(eventParticipation);
            } catch (UnsupportedEncodingException | MessagingException e) {
                throw new RuntimeException(e);
            }

            this.eventDrawParticipationRepository.deleteAllByMail(eventDrawParticipation.getMail());
        }

        for (int i = event.getMaxParticipants(); i < drawParticipationList.size(); i++) {
            this.eventDrawParticipationRepository.deleteById(drawParticipationList.get(i).getId());
        }
    }
}

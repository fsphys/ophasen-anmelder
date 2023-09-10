package edu.kit.physik.ophasenanmelder.services;

import de.m4rc3l.nova.core.service.CrudService;
import edu.kit.physik.ophasenanmelder.dto.EventParticipation;
import edu.kit.physik.ophasenanmelder.dto.EventType;

import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public interface EventTypeService extends CrudService<EventType, UUID> {
    boolean eventTypeHasDrawEnabled(EventType eventType);
    void draw(UUID id);
    void sendRegistrationMail(EventParticipation participation) throws UnsupportedEncodingException, MessagingException;
}

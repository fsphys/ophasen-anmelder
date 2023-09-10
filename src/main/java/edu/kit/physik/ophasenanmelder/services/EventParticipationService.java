package edu.kit.physik.ophasenanmelder.services;

import de.m4rc3l.nova.core.service.CrudService;
import edu.kit.physik.ophasenanmelder.dto.EventParticipation;
import edu.kit.physik.ophasenanmelder.model.EventParticipationModel;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public interface EventParticipationService extends CrudService<EventParticipation, UUID> {

    int countParticipants(UUID eventId);

    void sendRegistrationMail(EventParticipation participation) throws UnsupportedEncodingException, MessagingException;

    void sendUnRegistrationMail(EventParticipationModel participation) throws UnsupportedEncodingException, MessagingException;
}

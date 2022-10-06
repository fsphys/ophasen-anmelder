package edu.kit.physik.ophasenanmelder.services;

import de.m4rc3l.nova.core.service.CrudService;
import edu.kit.physik.ophasenanmelder.dto.EventDrawParticipation;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public interface EventDrawParticipationService extends CrudService<EventDrawParticipation, UUID> {

    void sendDrawRegistrationMail(EventDrawParticipation drawParticipation) throws UnsupportedEncodingException, MessagingException;
}

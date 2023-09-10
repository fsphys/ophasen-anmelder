package edu.kit.physik.ophasenanmelder.services;

import de.m4rc3l.nova.core.service.CrudService;
import edu.kit.physik.ophasenanmelder.dto.EventDrawParticipation;
import edu.kit.physik.ophasenanmelder.model.EventDrawParticipationModel;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public interface EventDrawParticipationService extends CrudService<EventDrawParticipation, UUID> {

    void sendDrawRegistrationMail(EventDrawParticipation drawParticipation) throws UnsupportedEncodingException, MessagingException;
    void sendDrawUnRegistrationMail(EventDrawParticipationModel drawParticipation) throws UnsupportedEncodingException, MessagingException;
}

package edu.kit.physik.ophasenanmelder.services;

import edu.kit.physik.ophasenanmelder.dto.InstituteParticipation;
import net.getnova.framework.core.service.CrudService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public interface InstituteParticipationService extends CrudService<InstituteParticipation, UUID> {

    int countParticipants(UUID instituteId);

    void sendMail(InstituteParticipation participation) throws UnsupportedEncodingException, MessagingException;
}

package edu.kit.physik.ophasenanmelder.services.impl;

import edu.kit.physik.ophasenanmelder.converter.InstituteParticipationConverter;
import edu.kit.physik.ophasenanmelder.dto.InstituteParticipation;
import edu.kit.physik.ophasenanmelder.exception.InstituteParticipationLimitExceededException;
import edu.kit.physik.ophasenanmelder.model.InstituteParticipationModel;
import edu.kit.physik.ophasenanmelder.repository.InstituteParticipationRepository;
import edu.kit.physik.ophasenanmelder.services.InstituteParticipationService;
import edu.kit.physik.ophasenanmelder.services.InstituteService;
import net.getnova.framework.core.service.AbstractCommonIdCrudService;
import net.getnova.framework.core.utils.ValidationUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class InstituteParticipationServiceImpl extends AbstractCommonIdCrudService<InstituteParticipation, UUID, InstituteParticipationModel> implements InstituteParticipationService {

    private static final String MAIL_TEXT = """
            Hallo %s,
                            
            deine Anmeldung bei der Institutsführung des %s war erfolgreich.
            Benutze diesen Link, um dich abzumelden: https://diesisteintollerlink.edu/abmelden/%s
                                    
            Liebe Grüße
            Deine Fachschaft
            """;

    private final InstituteService instituteService;
    private final JavaMailSender mailSender;

    public InstituteParticipationServiceImpl(final InstituteParticipationRepository repository,
                                             final InstituteParticipationConverter converter,
                                             final InstituteService instituteService,
                                             final JavaMailSender mailSender) {
        super("INSTITUTE_PARTICIPATION", repository, converter);
        this.instituteService = instituteService;
        this.mailSender = mailSender;
    }

    @Override
    public InstituteParticipation save(final InstituteParticipation dto) {
        ValidationUtils.validate(dto);

        if (instituteService.findById(dto.getInstituteId()).getMaxParticipants() <= countParticipants(dto.getInstituteId()))
            throw new InstituteParticipationLimitExceededException();

        final InstituteParticipation instituteParticipation = this.converter.toDto(this.repository.save(this.converter.toModel(dto)));
        try {
            this.sendMail(instituteParticipation);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException(e);
        }
        return instituteParticipation;
    }

    @Override
    public int countParticipants(final UUID instituteId) {
        return ((InstituteParticipationRepository) this.repository).countAllByInstituteId(instituteId);
    }

    @Override
    public void sendMail(InstituteParticipation participation) throws UnsupportedEncodingException, MessagingException {

        final MimeMessage message = this.mailSender.createMimeMessage();
        message.addFrom(new Address[]{new InternetAddress("mail@gmail.com", "KIT Physik Institutsführung Anmeldung", StandardCharsets.UTF_8.name())});
        message.setSubject("Deine Anmeldung wurde bestätigt");
        message.setRecipients(Message.RecipientType.TO, participation.getMail());
        message.setText(String.format(
                MAIL_TEXT,
                participation.getGivenName(),
                this.instituteService.findById(participation.getInstituteId()).getName(),
                participation.getId()
        ), StandardCharsets.UTF_8.name());

        this.mailSender.send(message);
    }
}

package edu.kit.physik.ophasenanmelder.controller.impl;

import de.m4rc3l.nova.core.controller.AbstractCrudController;
import edu.kit.physik.ophasenanmelder.controller.EventDrawParticipationController;
import edu.kit.physik.ophasenanmelder.dto.EventDrawParticipation;
import edu.kit.physik.ophasenanmelder.services.EventDrawParticipationService;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class EventDrawParticipationControllerImpl extends AbstractCrudController<EventDrawParticipation, UUID> implements EventDrawParticipationController {

    public EventDrawParticipationControllerImpl(final EventDrawParticipationService service) {
        super(service);
    }
}

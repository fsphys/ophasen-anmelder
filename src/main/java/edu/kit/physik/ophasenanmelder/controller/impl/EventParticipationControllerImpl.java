package edu.kit.physik.ophasenanmelder.controller.impl;

import edu.kit.physik.ophasenanmelder.controller.EventParticipationController;
import edu.kit.physik.ophasenanmelder.dto.EventParticipation;
import edu.kit.physik.ophasenanmelder.services.EventParticipationService;
import de.m4rc3l.nova.core.controller.AbstractCrudController;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class EventParticipationControllerImpl extends AbstractCrudController<EventParticipation, UUID> implements EventParticipationController {

    public EventParticipationControllerImpl(final EventParticipationService service) {
        super(service);
    }
}

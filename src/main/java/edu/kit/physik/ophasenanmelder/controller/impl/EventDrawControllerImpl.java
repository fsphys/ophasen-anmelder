package edu.kit.physik.ophasenanmelder.controller.impl;

import de.m4rc3l.nova.core.controller.AbstractCrudController;
import edu.kit.physik.ophasenanmelder.controller.EventDrawController;
import edu.kit.physik.ophasenanmelder.dto.EventDraw;
import edu.kit.physik.ophasenanmelder.services.EventDrawService;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class EventDrawControllerImpl extends AbstractCrudController<EventDraw, UUID> implements EventDrawController {

    public EventDrawControllerImpl(final EventDrawService service) {
        super(service);
    }
}

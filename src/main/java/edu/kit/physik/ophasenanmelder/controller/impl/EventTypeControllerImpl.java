package edu.kit.physik.ophasenanmelder.controller.impl;

import edu.kit.physik.ophasenanmelder.controller.EventTypeController;
import edu.kit.physik.ophasenanmelder.dto.EventType;
import edu.kit.physik.ophasenanmelder.services.EventTypeService;
import de.m4rc3l.nova.core.controller.AbstractCrudController;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class EventTypeControllerImpl extends AbstractCrudController<EventType, UUID> implements EventTypeController {

    public EventTypeControllerImpl(final EventTypeService service) {
        super(service);
    }

    @Override
    public void draw(UUID id) {

    }
}

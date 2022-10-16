package edu.kit.physik.ophasenanmelder.controller.impl;

import de.m4rc3l.nova.core.controller.AbstractCrudController;
import edu.kit.physik.ophasenanmelder.controller.EventTypeController;
import edu.kit.physik.ophasenanmelder.dto.EventType;
import edu.kit.physik.ophasenanmelder.services.EventTypeService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class EventTypeControllerImpl extends AbstractCrudController<EventType, UUID> implements EventTypeController {

    public EventTypeControllerImpl(final EventTypeService service) {
        super(service);
    }

    @Transactional
    @Override
    public void draw(final UUID id) {
        ((EventTypeService) this.service).draw(id);
    }
}

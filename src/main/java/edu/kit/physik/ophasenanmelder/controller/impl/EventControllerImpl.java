package edu.kit.physik.ophasenanmelder.controller.impl;

import edu.kit.physik.ophasenanmelder.controller.EventController;
import edu.kit.physik.ophasenanmelder.dto.Event;
import edu.kit.physik.ophasenanmelder.services.EventService;
import net.getnova.framework.core.controller.AbstractCrudController;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
public class EventControllerImpl extends AbstractCrudController<Event, UUID> implements EventController {

    public EventControllerImpl(final EventService service) {
        super(service);
    }

    @Override
    public Set<Event> findAllByTypeId(final UUID typeId) {
        return ((EventService) this.service).findAllByType(typeId);
    }
}

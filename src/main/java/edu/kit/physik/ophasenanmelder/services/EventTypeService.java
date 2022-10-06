package edu.kit.physik.ophasenanmelder.services;

import de.m4rc3l.nova.core.service.CrudService;
import edu.kit.physik.ophasenanmelder.dto.EventType;

import java.util.UUID;

public interface EventTypeService extends CrudService<EventType, UUID> {
    boolean eventTypeHasDrawEnabled(EventType eventType);
}

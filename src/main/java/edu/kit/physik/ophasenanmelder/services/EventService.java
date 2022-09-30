package edu.kit.physik.ophasenanmelder.services;

import edu.kit.physik.ophasenanmelder.dto.Event;
import de.m4rc3l.nova.core.service.CrudService;

import java.util.Set;
import java.util.UUID;

public interface EventService extends CrudService<Event, UUID> {

    Set<Event> findAllByType(UUID typeId);
}

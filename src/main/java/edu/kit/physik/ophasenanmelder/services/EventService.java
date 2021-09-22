package edu.kit.physik.ophasenanmelder.services;

import edu.kit.physik.ophasenanmelder.dto.Event;
import net.getnova.framework.core.service.CrudService;

import java.util.Set;
import java.util.UUID;

public interface EventService extends CrudService<Event, UUID> {

    Set<Event> findAllByType(UUID typeId);
}

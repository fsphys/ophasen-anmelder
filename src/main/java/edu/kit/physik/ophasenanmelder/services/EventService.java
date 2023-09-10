package edu.kit.physik.ophasenanmelder.services;

import de.m4rc3l.nova.core.service.CrudService;
import edu.kit.physik.ophasenanmelder.dto.Event;
import edu.kit.physik.ophasenanmelder.dto.EventDrawParticipation;

import java.util.Set;
import java.util.UUID;

public interface EventService extends CrudService<Event, UUID> {

    Set<Event> findAllByType(UUID typeId);

    Set<EventDrawParticipation> getAllDrawParticipationByEvent(Event event);
}

package edu.kit.physik.ophasenanmelder.services;

import edu.kit.physik.ophasenanmelder.dto.EventType;
import de.m4rc3l.nova.core.service.CrudService;

import java.util.List;
import java.util.UUID;

public interface EventTypeService extends CrudService<EventType, UUID> {

    List<EventType> findAllByEventDrawId(UUID eventDrawId);
}

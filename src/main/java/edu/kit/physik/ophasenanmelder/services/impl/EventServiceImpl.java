package edu.kit.physik.ophasenanmelder.services.impl;

import edu.kit.physik.ophasenanmelder.converter.EventConverter;
import edu.kit.physik.ophasenanmelder.dto.Event;
import edu.kit.physik.ophasenanmelder.model.EventModel;
import edu.kit.physik.ophasenanmelder.repository.EventRepository;
import edu.kit.physik.ophasenanmelder.services.EventService;
import net.getnova.framework.core.service.AbstractCommonIdCrudService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl extends AbstractCommonIdCrudService<Event, UUID, EventModel> implements EventService {

    public EventServiceImpl(final EventRepository repository, final EventConverter converter) {
        super("EVENT", repository, converter);
    }

    @Override
    public Set<Event> findAllByType(final UUID typeId) {
        return ((EventRepository) this.repository).findAllByEventTypeId(typeId)
                .stream()
                .map(this.converter::toDto)
                .collect(Collectors.toSet());
    }
}

package edu.kit.physik.ophasenanmelder.services.impl;

import edu.kit.physik.ophasenanmelder.converter.EventConverter;
import edu.kit.physik.ophasenanmelder.dto.Event;
import edu.kit.physik.ophasenanmelder.model.EventModel;
import edu.kit.physik.ophasenanmelder.repository.EventParticipationRepository;
import edu.kit.physik.ophasenanmelder.repository.EventRepository;
import edu.kit.physik.ophasenanmelder.services.EventService;
import de.m4rc3l.nova.core.exception.ValidationException;
import de.m4rc3l.nova.core.service.AbstractCommonIdCrudService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl extends AbstractCommonIdCrudService<Event, UUID, EventModel> implements EventService {

    private final EventParticipationRepository eventParticipationRepository;

    public EventServiceImpl(final EventRepository repository,
                            final EventConverter converter,
                            final EventParticipationRepository eventParticipationRepository) {
        super("EVENT", repository, converter);
        this.eventParticipationRepository = eventParticipationRepository;
    }

    @Override
    public Event save(final Event dto) {
        if (((EventRepository) this.repository).findByName(dto.getName()).isPresent())
            throw new ValidationException("name", "UNIQUE");

        return super.save(dto);
    }

    @Override
    public void delete(final UUID id) {
        this.eventParticipationRepository.deleteAllByEventId(id);
        super.delete(id);
    }

    @Override
    public Set<Event> findAllByType(final UUID typeId) {
        return ((EventRepository) this.repository).findAllByEventTypeId(typeId)
                .stream()
                .map(this.converter::toDto)
                .collect(Collectors.toSet());
    }
}

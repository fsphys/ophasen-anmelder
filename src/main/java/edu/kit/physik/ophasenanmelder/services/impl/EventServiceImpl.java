package edu.kit.physik.ophasenanmelder.services.impl;

import de.m4rc3l.nova.core.exception.ValidationException;
import de.m4rc3l.nova.core.service.AbstractCommonIdCrudService;
import edu.kit.physik.ophasenanmelder.converter.EventConverter;
import edu.kit.physik.ophasenanmelder.converter.EventDrawParticipationConverter;
import edu.kit.physik.ophasenanmelder.dto.Event;
import edu.kit.physik.ophasenanmelder.dto.EventDrawParticipation;
import edu.kit.physik.ophasenanmelder.model.EventModel;
import edu.kit.physik.ophasenanmelder.repository.EventDrawParticipationRepository;
import edu.kit.physik.ophasenanmelder.repository.EventParticipationRepository;
import edu.kit.physik.ophasenanmelder.repository.EventRepository;
import edu.kit.physik.ophasenanmelder.services.EventService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl extends AbstractCommonIdCrudService<Event, UUID, EventModel> implements EventService {

    private final EventParticipationRepository eventParticipationRepository;
    private final EventDrawParticipationRepository eventDrawParticipationRepository;
    private final EventDrawParticipationConverter eventDrawParticipationConverter;

    public EventServiceImpl(final EventRepository repository,
                            final EventConverter converter,
                            final EventParticipationRepository eventParticipationRepository,
                            final EventDrawParticipationRepository eventDrawParticipationRepository,
                            final EventDrawParticipationConverter eventDrawParticipationConverter) {
        super("EVENT", repository, converter);
        this.eventParticipationRepository = eventParticipationRepository;
        this.eventDrawParticipationRepository = eventDrawParticipationRepository;
        this.eventDrawParticipationConverter = eventDrawParticipationConverter;
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
        this.eventDrawParticipationRepository.deleteAllByEventId(id);
        super.delete(id);
    }

    @Override
    public Set<Event> findAllByType(final UUID typeId) {
        return ((EventRepository) this.repository).findAllByEventTypeId(typeId)
                .stream()
                .map(this.converter::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<EventDrawParticipation> getAllDrawParticipationByEvent(final Event event) {
        return this.eventDrawParticipationRepository.findAllByEventId(event.getId())
                .stream().map(this.eventDrawParticipationConverter::toDto)
                .collect(Collectors.toSet());
    }
}

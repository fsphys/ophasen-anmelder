package edu.kit.physik.ophasenanmelder.services.impl;

import edu.kit.physik.ophasenanmelder.converter.EventTypeConverter;
import edu.kit.physik.ophasenanmelder.dto.EventType;
import edu.kit.physik.ophasenanmelder.model.EventTypeModel;
import edu.kit.physik.ophasenanmelder.repository.EventTypeRepository;
import edu.kit.physik.ophasenanmelder.services.EventService;
import edu.kit.physik.ophasenanmelder.services.EventTypeService;
import de.m4rc3l.nova.core.exception.ValidationException;
import de.m4rc3l.nova.core.service.AbstractCommonIdCrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventTypeServiceImpl extends AbstractCommonIdCrudService<EventType, UUID, EventTypeModel> implements EventTypeService {

    private final EventService eventService;

    public EventTypeServiceImpl(final EventTypeRepository repository,
                                final EventTypeConverter converter,
                                final EventService eventService) {
        super("EVENT_TYPE", repository, converter);
        this.eventService = eventService;
    }

    @Override
    public EventType save(final EventType dto) {
        if (((EventTypeRepository) this.repository).findByName(dto.getName()).isPresent())
            throw new ValidationException("name", "UNIQUE");

        return super.save(dto);
    }

    @Override
    public void delete(final UUID id) {
        this.eventService.findAllByType(id).forEach(event -> this.eventService.delete(event.getId()));
        super.delete(id);
    }

    @Override
    public List<EventType> findAllByEventDrawId(final UUID eventDrawId) {
        return ((EventTypeRepository) this.repository).findAllByEventDrawId(eventDrawId).stream().map((this.converter::toDto)).toList();
    }
}

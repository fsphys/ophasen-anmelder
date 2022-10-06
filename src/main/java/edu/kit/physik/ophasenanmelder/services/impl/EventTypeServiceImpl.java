package edu.kit.physik.ophasenanmelder.services.impl;

import de.m4rc3l.nova.core.exception.ValidationException;
import de.m4rc3l.nova.core.service.AbstractCommonIdCrudService;
import edu.kit.physik.ophasenanmelder.converter.EventTypeConverter;
import edu.kit.physik.ophasenanmelder.dto.EventDraw;
import edu.kit.physik.ophasenanmelder.dto.EventType;
import edu.kit.physik.ophasenanmelder.model.EventTypeModel;
import edu.kit.physik.ophasenanmelder.repository.EventTypeRepository;
import edu.kit.physik.ophasenanmelder.services.EventDrawService;
import edu.kit.physik.ophasenanmelder.services.EventService;
import edu.kit.physik.ophasenanmelder.services.EventTypeService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventTypeServiceImpl extends AbstractCommonIdCrudService<EventType, UUID, EventTypeModel> implements EventTypeService {

    private final EventService eventService;
    private final EventDrawService eventDrawService;

    public EventTypeServiceImpl(final EventTypeRepository repository,
                                final EventTypeConverter converter,
                                final EventService eventService,
                                final EventDrawService eventDrawService) {
        super("EVENT_TYPE", repository, converter);
        this.eventService = eventService;
        this.eventDrawService = eventDrawService;
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
    public boolean eventTypeHasDrawEnabled(final EventType eventType) {
        if (eventType.getEventDrawId() == null) return false;
        final EventDraw eventDraw = this.eventDrawService.findById(eventType.getEventDrawId());
        return !eventDraw.getDrawn();
    }
}

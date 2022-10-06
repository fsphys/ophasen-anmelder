package edu.kit.physik.ophasenanmelder.services.impl;

import de.m4rc3l.nova.core.service.AbstractCommonIdCrudService;
import edu.kit.physik.ophasenanmelder.converter.EventDrawConverter;
import edu.kit.physik.ophasenanmelder.converter.EventTypeConverter;
import edu.kit.physik.ophasenanmelder.dto.EventDraw;
import edu.kit.physik.ophasenanmelder.dto.EventType;
import edu.kit.physik.ophasenanmelder.model.EventDrawModel;
import edu.kit.physik.ophasenanmelder.model.EventTypeModel;
import edu.kit.physik.ophasenanmelder.repository.EventDrawRepository;
import edu.kit.physik.ophasenanmelder.repository.EventTypeRepository;
import edu.kit.physik.ophasenanmelder.services.EventDrawService;
import edu.kit.physik.ophasenanmelder.services.EventTypeService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class EventDrawServiceImpl extends AbstractCommonIdCrudService<EventDraw, UUID, EventDrawModel> implements EventDrawService {

    private final EventTypeRepository eventTypeRepository;

    public EventDrawServiceImpl(final EventDrawRepository repository,
                                final EventDrawConverter converter,
                                final EventTypeRepository eventTypeRepository) {
        super("EVENT_DRAW", repository, converter);
        this.eventTypeRepository = eventTypeRepository;
    }

    @Override
    public void delete(final UUID id) {
        this.eventTypeRepository.findAllByEventDrawId(id).forEach((eventType -> {
            eventType.setEventDraw(null);
            this.eventTypeRepository.save(eventType);
        }));
        super.delete(id);
    }
}

package edu.kit.physik.ophasenanmelder.converter;

import edu.kit.physik.ophasenanmelder.dto.Event;
import edu.kit.physik.ophasenanmelder.model.EventModel;
import edu.kit.physik.ophasenanmelder.model.EventTypeModel;
import edu.kit.physik.ophasenanmelder.repository.EventParticipationRepository;
import edu.kit.physik.ophasenanmelder.repository.EventTypeRepository;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.core.Converter;
import net.getnova.framework.core.exception.NotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventConverter implements Converter<EventModel, Event> {

    private final EventTypeRepository eventTypeRepository;
    private final EventParticipationRepository eventParticipationRepository;

    @Override
    public EventModel toModel(final Event dto) {
        final EventTypeModel eventType = this.eventTypeRepository
                .findById(dto.getEventTypeId())
                .orElseThrow(() -> new NotFoundException("EVENT_TYPE_NOT_FOUND"));

        return new EventModel(
                eventType,
                dto.getName(),
                dto.getDescription(),
                dto.getMaxParticipants(),
                dto.getStartTime(),
                dto.getNeedsHasTicket(),
                dto.getNeedsBirthInformation()
        );
    }

    @Override
    public Event toDto(final EventModel model) {
        return new Event(
                model.getId(),
                model.getEventType().getId(),
                model.getName(),
                model.getDescription(),
                model.getMaxParticipants(),
                model.getStartTime(),
                model.getMaxParticipants() - this.eventParticipationRepository.countAllByEventId(model.getId()),
                model.getNeedsHasTicket(),
                model.getNeedsBirthInformation()
        );
    }

    @Override
    public void override(final EventModel model, final Event dto) {
        final EventTypeModel eventType = this.eventTypeRepository
                .findById(dto.getEventTypeId())
                .orElseThrow(() -> new NotFoundException("EVENT_TYPE_NOT_FOUND"));

        model.setEventType(eventType);
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setMaxParticipants(dto.getMaxParticipants());
        model.setStartTime(dto.getStartTime());
        model.setNeedsHasTicket(dto.getNeedsHasTicket());
        model.setNeedsBirthInformation(dto.getNeedsBirthInformation());
    }

    @Override
    public void merge(final EventModel model, final Event dto) {
        throw new UnsupportedOperationException();
    }
}

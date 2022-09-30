package edu.kit.physik.ophasenanmelder.converter;

import de.m4rc3l.nova.core.Converter;
import de.m4rc3l.nova.core.exception.NotFoundException;
import edu.kit.physik.ophasenanmelder.dto.EventType;
import edu.kit.physik.ophasenanmelder.model.EventDrawModel;
import edu.kit.physik.ophasenanmelder.model.EventTypeModel;
import edu.kit.physik.ophasenanmelder.repository.EventDrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventTypeConverter implements Converter<EventTypeModel, EventType> {

    private final EventDrawRepository eventDrawRepository;

    @Override
    public EventTypeModel toModel(final EventType dto) {

        final EventDrawModel eventDraw = dto.getEventDrawId() == null ? null : this.eventDrawRepository
                .findById(dto.getEventDrawId())
                .orElseThrow(() -> new NotFoundException("EVENT_DRAW_NOT_FOUND"));

        return new EventTypeModel(dto.getName(),
                dto.getRegistrationStartTime(),
                dto.getRegistrationEndTime(),
                eventDraw);
    }

    @Override
    public EventType toDto(final EventTypeModel model) {
        return new EventType(
                model.getId(),
                model.getName(),
                model.getRegistrationStartTime(),
                model.getRegistrationEndTime(),
                model.getEventDraw() == null ? null : model.getEventDraw().getId()
        );
    }

    @Override
    public void override(final EventTypeModel model, final EventType dto) {
        model.setName(dto.getName());
        model.setRegistrationStartTime(dto.getRegistrationStartTime());
        model.setRegistrationEndTime(dto.getRegistrationEndTime());
        model.setEventDraw(dto.getEventDrawId() == null ? null : this.eventDrawRepository
                .findById(dto.getEventDrawId())
                .orElseThrow(() -> new NotFoundException("EVENT_DRAW_NOT_FOUND")));
    }

    @Override
    public void merge(final EventTypeModel model, final EventType dto) {
        throw new UnsupportedOperationException();
    }
}

package edu.kit.physik.ophasenanmelder.converter;

import edu.kit.physik.ophasenanmelder.dto.EventType;
import edu.kit.physik.ophasenanmelder.model.EventTypeModel;
import net.getnova.framework.core.Converter;
import org.springframework.stereotype.Component;

@Component
public class EventTypeConverter implements Converter<EventTypeModel, EventType> {

    @Override
    public EventTypeModel toModel(final EventType dto) {
        return new EventTypeModel(dto.getName());
    }

    @Override
    public EventType toDto(final EventTypeModel model) {
        return new EventType(model.getId(), model.getName());
    }

    @Override
    public void override(final EventTypeModel model, final EventType dto) {
        model.setName(dto.getName());
    }

    @Override
    public void merge(final EventTypeModel model, final EventType dto) {
        throw new UnsupportedOperationException();
    }
}

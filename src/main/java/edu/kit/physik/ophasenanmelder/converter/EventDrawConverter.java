package edu.kit.physik.ophasenanmelder.converter;

import de.m4rc3l.nova.core.Converter;
import edu.kit.physik.ophasenanmelder.dto.EventDraw;
import edu.kit.physik.ophasenanmelder.model.EventDrawModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventDrawConverter implements Converter<EventDrawModel, EventDraw> {

    @Override
    public EventDrawModel toModel(EventDraw dto) {

        return new EventDrawModel(
                dto.getDrawTime(),
                dto.getDrawn()
        );
    }

    @Override
    public EventDraw toDto(EventDrawModel model) {
        return new EventDraw(
                model.getId(),
                model.getDrawTime(),
                model.getDrawn()
        );
    }

    @Override
    public void override(EventDrawModel model, EventDraw dto) {
        model.setDrawTime(dto.getDrawTime());
        model.setDrawn(dto.getDrawn());
    }

    @Override
    public void merge(EventDrawModel model, EventDraw dto) {
        throw new UnsupportedOperationException();
    }
}

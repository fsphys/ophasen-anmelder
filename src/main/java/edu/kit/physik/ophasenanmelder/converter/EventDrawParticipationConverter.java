package edu.kit.physik.ophasenanmelder.converter;

import de.m4rc3l.nova.core.Converter;
import de.m4rc3l.nova.core.exception.NotFoundException;
import edu.kit.physik.ophasenanmelder.dto.EventDrawParticipation;
import edu.kit.physik.ophasenanmelder.model.EventDrawParticipationModel;
import edu.kit.physik.ophasenanmelder.model.EventModel;
import edu.kit.physik.ophasenanmelder.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventDrawParticipationConverter implements Converter<EventDrawParticipationModel, EventDrawParticipation> {

    private final EventRepository eventRepository;

    @Override
    public EventDrawParticipationModel toModel(final EventDrawParticipation dto) {
        final EventModel eventModel = this.eventRepository
                .findById(dto.getEventId())
                .orElseThrow(() -> new NotFoundException("EVENT_NOT_FOUND"));

        return new EventDrawParticipationModel(
                eventModel,
                dto.getSurname(),
                dto.getGivenName(),
                dto.getMail(),
                dto.getBirthDate(),
                dto.getBirthPlace()
        );
    }

    @Override
    public EventDrawParticipation toDto(final EventDrawParticipationModel model) {
        return new EventDrawParticipation(
                model.getId(),
                model.getEvent().getId(),
                model.getSurname(),
                model.getGivenName(),
                model.getMail(),
                model.getBirthDate(),
                model.getBirthPlace()
        );
    }

    @Override
    public void override(final EventDrawParticipationModel model, final EventDrawParticipation dto) {
        final EventModel eventModel = this.eventRepository
                .findById(dto.getEventId())
                .orElseThrow(() -> new NotFoundException("EVENT_NOT_FOUND"));

        model.setEvent(eventModel);
        model.setSurname(dto.getSurname());
        model.setGivenName(dto.getGivenName());
        model.setBirthDate(dto.getBirthDate());
        model.setBirthPlace(dto.getBirthPlace());
    }

    @Override
    public void merge(final EventDrawParticipationModel model, final EventDrawParticipation dto) {
        throw new UnsupportedOperationException();
    }
}

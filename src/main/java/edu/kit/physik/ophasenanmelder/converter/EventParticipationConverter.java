package edu.kit.physik.ophasenanmelder.converter;

import de.m4rc3l.nova.core.Converter;
import de.m4rc3l.nova.core.exception.NotFoundException;
import edu.kit.physik.ophasenanmelder.dto.EventParticipation;
import edu.kit.physik.ophasenanmelder.model.EventModel;
import edu.kit.physik.ophasenanmelder.model.EventParticipationModel;
import edu.kit.physik.ophasenanmelder.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventParticipationConverter implements Converter<EventParticipationModel, EventParticipation> {

    private final EventRepository eventRepository;

    @Override
    public EventParticipationModel toModel(final EventParticipation dto) {
        final EventModel eventModel = this.eventRepository
                .findById(dto.getEventId())
                .orElseThrow(() -> new NotFoundException("EVENT_NOT_FOUND"));

        return new EventParticipationModel(
                eventModel,
                dto.getSurname(),
                dto.getGivenName(),
                dto.getMail(),
                dto.getBirthDate(),
                dto.getBirthPlace()
        );
    }

    @Override
    public EventParticipation toDto(final EventParticipationModel model) {
        return new EventParticipation(
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
    public void override(final EventParticipationModel model, final EventParticipation dto) {
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
    public void merge(final EventParticipationModel model, final EventParticipation dto) {
        throw new UnsupportedOperationException();
    }
}

package edu.kit.physik.ophasenanmelder.repository;

import edu.kit.physik.ophasenanmelder.model.EventParticipationModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventParticipationRepository extends CrudRepository<EventParticipationModel, UUID> {

    int countAllByEventId(UUID id);

    int countAllByEventEventTypeIdAndMail(UUID eventTypeId, String mail);

    void deleteAllByEventId(UUID eventId);
}

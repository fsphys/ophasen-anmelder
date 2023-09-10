package edu.kit.physik.ophasenanmelder.repository;

import edu.kit.physik.ophasenanmelder.model.EventDrawParticipationModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface EventDrawParticipationRepository extends CrudRepository<EventDrawParticipationModel, UUID> {

    void deleteAllByEventId(UUID eventId);

    Set<EventDrawParticipationModel> findAllByEventId(UUID eventId);

    void deleteAllByMail(String mail);

    boolean existsByMailAndEventId(String mail, UUID eventId);
}

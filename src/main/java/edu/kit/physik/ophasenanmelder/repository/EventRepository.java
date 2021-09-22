package edu.kit.physik.ophasenanmelder.repository;

import edu.kit.physik.ophasenanmelder.model.EventModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface EventRepository extends CrudRepository<EventModel, UUID> {

    Set<EventModel> findAllByEventTypeId(UUID eventTypeId);
}

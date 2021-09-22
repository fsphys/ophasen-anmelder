package edu.kit.physik.ophasenanmelder.repository;

import edu.kit.physik.ophasenanmelder.model.EventTypeModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventTypeRepository extends CrudRepository<EventTypeModel, UUID> {
}

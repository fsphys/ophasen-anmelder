package edu.kit.physik.ophasenanmelder.repository;

import edu.kit.physik.ophasenanmelder.model.EventDrawModel;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EventDrawRepository extends CrudRepository<EventDrawModel, UUID> {
}

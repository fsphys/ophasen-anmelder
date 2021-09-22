package edu.kit.physik.ophasenanmelder.repository;

import edu.kit.physik.ophasenanmelder.model.InstituteParticipationModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InstituteParticipationRepository extends CrudRepository<InstituteParticipationModel, UUID> {

    int countAllByInstituteId(UUID id);
}

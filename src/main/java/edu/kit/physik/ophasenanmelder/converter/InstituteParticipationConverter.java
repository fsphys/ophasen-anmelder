package edu.kit.physik.ophasenanmelder.converter;

import edu.kit.physik.ophasenanmelder.dto.InstituteParticipation;
import edu.kit.physik.ophasenanmelder.model.InstituteModel;
import edu.kit.physik.ophasenanmelder.model.InstituteParticipationModel;
import edu.kit.physik.ophasenanmelder.repository.InstituteRepository;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.core.Converter;
import net.getnova.framework.core.exception.NotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InstituteParticipationConverter implements Converter<InstituteParticipationModel, InstituteParticipation> {

    private final InstituteRepository instituteRepository;

    @Override
    public InstituteParticipationModel toModel(InstituteParticipation dto) {
        final InstituteModel instituteModel = this.instituteRepository.findById(dto.getInstituteId()).orElseThrow(() -> new NotFoundException("INSTITUTE_NOT_FOUND"));
        return new InstituteParticipationModel(instituteModel, dto.getSurname(), dto.getGivenName(), dto.getMail());
    }

    @Override
    public InstituteParticipation toDto(InstituteParticipationModel model) {
        return new InstituteParticipation(model.getId(), model.getInstitute().getId(), model.getSurname(), model.getGivenName(), model.getMail());
    }

    @Override
    public void override(InstituteParticipationModel model, InstituteParticipation dto) {
        final InstituteModel instituteModel = this.instituteRepository.findById(dto.getInstituteId()).orElseThrow(() -> new NotFoundException("INSTITUTE_NOT_FOUND"));
        model.setInstitute(instituteModel);
        model.setSurname(dto.getSurname());
        model.setGivenName(dto.getGivenName());
    }

    @Override
    public void merge(InstituteParticipationModel model, InstituteParticipation dto) {
        throw new UnsupportedOperationException();
    }
}

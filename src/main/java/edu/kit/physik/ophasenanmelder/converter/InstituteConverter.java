package edu.kit.physik.ophasenanmelder.converter;

import edu.kit.physik.ophasenanmelder.dto.Institute;
import edu.kit.physik.ophasenanmelder.model.InstituteModel;
import net.getnova.framework.core.Converter;
import org.springframework.stereotype.Component;

@Component
public class InstituteConverter implements Converter<InstituteModel, Institute> {

    @Override
    public InstituteModel toModel(Institute dto) {
        return new InstituteModel(dto.getName(), dto.getDescription(), dto.getMaxParticipants(), dto.getStartTime());
    }

    @Override
    public Institute toDto(InstituteModel model) {
        return new Institute(model.getId(), model.getName(), model.getDescription(), model.getMaxParticipants(), model.getStartTime());
    }

    @Override
    public void override(InstituteModel model, Institute dto) {
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setMaxParticipants(dto.getMaxParticipants());
        model.setStartTime(dto.getStartTime());
    }

    @Override
    public void merge(InstituteModel model, Institute dto) {
        throw new UnsupportedOperationException();
    }
}

package edu.kit.physik.ophasenanmelder.services.impl;

import edu.kit.physik.ophasenanmelder.converter.InstituteConverter;
import edu.kit.physik.ophasenanmelder.dto.Institute;
import edu.kit.physik.ophasenanmelder.model.InstituteModel;
import edu.kit.physik.ophasenanmelder.repository.InstituteRepository;
import edu.kit.physik.ophasenanmelder.services.InstituteService;
import net.getnova.framework.core.service.AbstractCommonIdCrudService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InstituteServiceImpl extends AbstractCommonIdCrudService<Institute, UUID, InstituteModel> implements InstituteService {

    public InstituteServiceImpl(final InstituteRepository repository, final InstituteConverter converter) {
        super("INSTITUTE", repository, converter);
    }
}

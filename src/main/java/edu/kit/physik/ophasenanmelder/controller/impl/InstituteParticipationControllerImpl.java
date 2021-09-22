package edu.kit.physik.ophasenanmelder.controller.impl;

import edu.kit.physik.ophasenanmelder.controller.InstituteParticipationController;
import edu.kit.physik.ophasenanmelder.dto.InstituteParticipation;
import edu.kit.physik.ophasenanmelder.services.InstituteParticipationService;
import net.getnova.framework.core.controller.AbstractCrudController;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class InstituteParticipationControllerImpl extends AbstractCrudController<InstituteParticipation, UUID> implements InstituteParticipationController {

    public InstituteParticipationControllerImpl(InstituteParticipationService service) {
        super(service);
    }
}

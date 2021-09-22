package edu.kit.physik.ophasenanmelder.controller.impl;

import edu.kit.physik.ophasenanmelder.controller.InstituteController;
import edu.kit.physik.ophasenanmelder.dto.Institute;
import edu.kit.physik.ophasenanmelder.services.InstituteService;
import net.getnova.framework.core.controller.AbstractCrudController;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class InstituteControllerImpl extends AbstractCrudController<Institute, UUID> implements InstituteController {

    public InstituteControllerImpl(final InstituteService service) {
        super(service);
    }
}

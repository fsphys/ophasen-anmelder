package edu.kit.physik.ophasenanmelder.controller;

import edu.kit.physik.ophasenanmelder.dto.InstituteParticipation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.getnova.framework.core.controller.CrudController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Tag(name = "Institute Participation")
@RequestMapping("/institute/participation")
public interface InstituteParticipationController extends CrudController<InstituteParticipation, UUID> {

}

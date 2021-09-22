package edu.kit.physik.ophasenanmelder.controller;

import edu.kit.physik.ophasenanmelder.dto.EventParticipation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.getnova.framework.core.controller.CrudController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Tag(name = "Event Participation")
@RequestMapping("/event/participation")
public interface EventParticipationController extends CrudController<EventParticipation, UUID> {
}

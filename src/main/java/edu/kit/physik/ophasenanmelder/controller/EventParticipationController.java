package edu.kit.physik.ophasenanmelder.controller;

import de.m4rc3l.nova.core.controller.CrudController;
import edu.kit.physik.ophasenanmelder.dto.EventParticipation;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/event/participation")
public interface EventParticipationController extends CrudController<EventParticipation, UUID> {
}

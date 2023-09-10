package edu.kit.physik.ophasenanmelder.controller;

import de.m4rc3l.nova.core.controller.CrudController;
import edu.kit.physik.ophasenanmelder.dto.EventDrawParticipation;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/event/draw/participation")
public interface EventDrawParticipationController extends CrudController<EventDrawParticipation, UUID> {
}

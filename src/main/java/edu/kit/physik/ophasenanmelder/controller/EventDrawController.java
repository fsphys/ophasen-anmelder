package edu.kit.physik.ophasenanmelder.controller;

import de.m4rc3l.nova.core.controller.CrudController;
import edu.kit.physik.ophasenanmelder.dto.EventDraw;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Tag(name = "Event Draw")
@RequestMapping("/event/draw")
public interface EventDrawController extends CrudController<EventDraw, UUID> {
}

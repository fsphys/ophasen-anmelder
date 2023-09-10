package edu.kit.physik.ophasenanmelder.controller;

import de.m4rc3l.nova.core.controller.CrudController;
import edu.kit.physik.ophasenanmelder.dto.EventType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/event/type")
public interface EventTypeController extends CrudController<EventType, UUID> {

    @GetMapping(path = "/{id}/draw")
    void draw(@PathVariable UUID id);
}

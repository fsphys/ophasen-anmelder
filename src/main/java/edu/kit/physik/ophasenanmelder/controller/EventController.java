package edu.kit.physik.ophasenanmelder.controller;

import de.m4rc3l.nova.core.controller.CrudController;
import edu.kit.physik.ophasenanmelder.dto.Event;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;
import java.util.UUID;

@RequestMapping("/event")
public interface EventController extends CrudController<Event, UUID> {

    @GetMapping(path = "/byType/{typeId}")
    Set<Event> findAllByTypeId(@PathVariable UUID typeId);
}

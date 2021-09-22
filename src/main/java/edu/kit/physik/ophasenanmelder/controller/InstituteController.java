package edu.kit.physik.ophasenanmelder.controller;

import edu.kit.physik.ophasenanmelder.dto.Institute;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.getnova.framework.core.controller.CrudController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Tag(name = "Institute")
@RequestMapping("/institute")
public interface InstituteController extends CrudController<Institute, UUID> {
}

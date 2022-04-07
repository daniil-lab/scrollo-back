package com.inst.base.controller.action;

import com.inst.base.dto.action.ActionDTO;
import com.inst.base.request.PageRequestParams;
import com.inst.base.service.action.ActionService;
import com.inst.base.util.PageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/action")
@Tag(name = "Action API")
@SecurityRequirement(name = "Bearer")
public class ActionController {
    @Autowired
    private ActionService actionService;

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/")
    public ResponseEntity<PageResponse<ActionDTO>> getAllActions(
            @RequestParam
            @Valid
                PageRequestParams params
    ) {
        return new ResponseEntity<>(actionService.getActions(params), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/no-viewed")
    public ResponseEntity<PageResponse<ActionDTO>> getNoViewedActions(
            @RequestParam
            @Valid
                    PageRequestParams params
    ) {
        return new ResponseEntity<>(actionService.getNoReadActions(params), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/view/{actionId}")
    public ResponseEntity<ActionDTO> viewAction(
            @PathVariable
                    UUID actionId
    ) {
        return new ResponseEntity<>(new ActionDTO(actionService.viewAction(actionId)), HttpStatus.OK);
    }
}

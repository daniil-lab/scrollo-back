package com.inst.base.service.action;

import com.inst.base.dto.action.ActionDTO;
import com.inst.base.entity.action.Action;
import com.inst.base.request.PageRequestParams;
import com.inst.base.util.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface ActionService {
    PageResponse<ActionDTO> getActions(PageRequestParams params);

    PageResponse<ActionDTO> getNoReadActions(PageRequestParams params);

    Action viewAction(UUID actionId);
}

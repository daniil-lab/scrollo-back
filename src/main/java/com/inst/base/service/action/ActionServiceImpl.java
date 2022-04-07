package com.inst.base.service.action;

import com.inst.base.dto.action.ActionDTO;
import com.inst.base.entity.action.Action;
import com.inst.base.entity.user.User;
import com.inst.base.repository.action.ActionRepository;
import com.inst.base.request.PageRequestParams;
import com.inst.base.util.AuthHelper;
import com.inst.base.util.PageResponse;
import com.inst.base.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActionServiceImpl implements ActionService {
    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private AuthHelper authHelper;

    @Override
    public PageResponse<ActionDTO> getActions(PageRequestParams params) {
        Page<Action> page = actionRepository.findAll(PageRequest.of(params.getPage(), params.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt")));

        return new PageResponse<ActionDTO>(page.map(ActionDTO::new).stream().toList(), params.getPage(), page.getTotalPages(), page.getTotalElements());
    }

    @Override
    public PageResponse<ActionDTO> getNoReadActions(PageRequestParams params) {
        Page<Action> page = actionRepository.findByViewedByReceiver(false, PageRequest.of(params.getPage(), params.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt")));

        return new PageResponse<ActionDTO>(page.map(ActionDTO::new).stream().toList(), params.getPage(), page.getTotalPages(), page.getTotalElements());
    }

    @Override
    public Action viewAction(UUID actionId) {
        User user = authHelper.getUserFromAuthCredentials();

        Action action = actionRepository.findById(actionId).orElseThrow(() -> {
            throw new ServiceException("Action not found", HttpStatus.NOT_FOUND);
        });

        if(!action.getReceiver().getId().equals(user.getId()))
            throw new ServiceException("It`s not your action", HttpStatus.BAD_REQUEST);

        action.setViewedByReceiver(true);

        actionRepository.save(action);

        return action;
    }
}

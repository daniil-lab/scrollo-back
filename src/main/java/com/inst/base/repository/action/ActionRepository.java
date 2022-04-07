package com.inst.base.repository.action;

import com.inst.base.entity.action.Action;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActionRepository extends JpaRepository<Action, UUID> {
    Page<Action> findByViewedByReceiver(Boolean receiver, Pageable pageable);
}

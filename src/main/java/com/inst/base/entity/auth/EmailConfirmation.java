package com.inst.base.entity.auth;

import com.inst.base.entity.abstracted.TimeAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EmailConfirmation extends TimeAudit {
    @Id
    private UUID id = UUID.randomUUID();

    private String email;

    private String code;

    private ConfirmationState state = ConfirmationState.WAIT_CONFIRM;

    private int enterCount;

    private Instant expiredAt;
}

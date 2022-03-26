package com.inst.base.request.chat;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CreateChatRequest {
    @NotNull
    private UUID userId;
}

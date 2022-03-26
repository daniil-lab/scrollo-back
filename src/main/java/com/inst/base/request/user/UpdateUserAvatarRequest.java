package com.inst.base.request.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UpdateUserAvatarRequest {
    @NotNull
    private MultipartFile avatar;
}

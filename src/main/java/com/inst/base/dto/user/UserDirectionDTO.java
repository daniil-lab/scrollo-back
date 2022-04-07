package com.inst.base.dto.user;

import com.inst.base.entity.user.UserDirection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDirectionDTO {
    private String name;

    private String displayName;

    public UserDirectionDTO(UserDirection ud) {
        if(ud == null)
            return;

        this.name = ud.name();
        this.displayName = ud.getDisplayName();
    }
}

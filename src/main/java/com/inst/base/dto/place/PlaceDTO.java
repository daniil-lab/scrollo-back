package com.inst.base.dto.place;

import com.inst.base.entity.place.Place;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PlaceDTO {
    private UUID id;

    private String name;

    public PlaceDTO(Place p) {
        if(p == null)
            return;

        this.id = p.getId();
        this.name = p.getName();
    }
}

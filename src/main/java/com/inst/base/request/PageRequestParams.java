package com.inst.base.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
public class PageRequestParams {
    @Min(0)
    private Integer page;

    @Min(10)
    private Integer pageSize;
}

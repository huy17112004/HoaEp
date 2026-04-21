package com.dearfloral.common.api;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageMeta {
    private Integer page;
    private Integer limit;
    private Long totalItems;
    private Integer totalPages;
}

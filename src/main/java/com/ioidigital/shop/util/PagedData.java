package com.ioidigital.shop.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedData<T> {

    private int pageNo;
    private int elementPerPage;
    private Long totalElements;
    private int totalPages;

    private List<T> elementList;

    public PagedData<T> emptyPage() {
        return PagedData.<T>builder()
                .pageNo(0)
                .elementPerPage(0)
                .totalElements(0L)
                .totalPages(0)
                .elementList(List.of())
                .build();
    }
}

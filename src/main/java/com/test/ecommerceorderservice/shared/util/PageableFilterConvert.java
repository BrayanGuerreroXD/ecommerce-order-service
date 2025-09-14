package com.test.ecommerceorderservice.shared.util;

import com.test.ecommerceorderservice.shared.dto.request.FilterRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PageableFilterConvert {
    public Pageable createPageAndSort(FilterRequest filterRequest) {
        filterRequest.setPage((filterRequest.getPage()) > 0 ? filterRequest.getPage() - 1 : filterRequest.getPage());
        Pageable pageable;
        if (filterRequest.getColumn() == null || filterRequest.getColumn().isEmpty()) {
            pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());
        } else {
            pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize(),
                    this.createSort(filterRequest.getOrder(), filterRequest.getColumn()));
        }
        return pageable;
    }

    public Sort createSort(String order, String column) {
        if ("desc".equalsIgnoreCase(order)) {
            return Sort.by(Sort.Direction.DESC, column);
        } else {
            return Sort.by(Sort.Direction.ASC, column);
        }
    }
}

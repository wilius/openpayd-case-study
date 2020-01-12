package com.openpayd.exchange.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.openpayd.exchange.gateway.serdes.PageTokenSerializer;
import com.openpayd.exchange.model.PageToken;

import java.util.List;

public class PagedResponse<T> {
    private final int totalPages;
    private final long totalElements;
    private final int currentPage;

    private final List<T> items;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = PageTokenSerializer.class)
    private final PageToken nextPageToken;

    public PagedResponse(int totalPages,
                         long totalElements,
                         int currentPage,
                         List<T> items,
                         PageToken nextPageToken) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.items = items;
        this.nextPageToken = nextPageToken;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<T> getItems() {
        return items;
    }

    public PageToken getNextPageToken() {
        return nextPageToken;
    }
}

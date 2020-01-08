package com.openpayd.exchange.model;

public abstract class PageToken {
    private final int pageNumber;
    private final int pageSize;

    public PageToken(int pageNumber,
                     int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }
}

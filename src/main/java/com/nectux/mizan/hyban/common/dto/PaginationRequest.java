package com.nectux.mizan.hyban.common.dto;

public class PaginationRequest {
    private Integer limit;
    private Integer offset;
    private String search;

    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
    public Integer getOffset() { return offset; }
    public void setOffset(Integer offset) { this.offset = offset; }
    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }
}

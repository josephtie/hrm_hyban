package com.nectux.mizan.hyban.common.dto;

public class PaginationIdRequest {
    private Long id;
    private Integer offset;
    private Integer limit;
    private String search;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getOffset() { return offset; }
    public void setOffset(Integer offset) { this.offset = offset; }
    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }
}

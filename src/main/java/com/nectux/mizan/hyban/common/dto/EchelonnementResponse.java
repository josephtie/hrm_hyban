package com.nectux.mizan.hyban.common.dto;

import java.util.List;

public class EchelonnementResponse<T> {
    private List<T> rows;
    private T row;
    private Integer total;
    private String result;
    private String message;

    public List<T> getRows() { return rows; }
    public void setRows(List<T> rows) { this.rows = rows; }
    public T getRow() { return row; }
    public void setRow(T row) { this.row = row; }
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

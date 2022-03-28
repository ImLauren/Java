package com.company;

import java.util.List;

public class DBTable {
    private String tableName;  //表名
    private int rowNumber;  //行数
    private int colNumber;  //列数???
    private String keys;

    private List<List<String>> tableValue;  //table值

    public DBTable() {   //无参构造函数
    }

    public DBTable(String tableName) {
        this.tableName = tableName;
    }

    public List<List<String>> getTableValue() {
        return tableValue;
    }

    public void setTableValue(List<List<String>> tableValue) {
        this.tableValue = tableValue;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getColNumber() {
        return colNumber;
    }

    public void setColNumber(int colNumber) {
        this.colNumber = colNumber;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

}
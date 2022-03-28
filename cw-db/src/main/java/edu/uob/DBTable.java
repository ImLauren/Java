package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class DBTable {
    private String DBname;
    private String tableName;
    private String keys;
    int id;
    List<List<String>> tableValue;

    public String getDBname() {return DBname;}

    public void setDBname(String DBname) {this.DBname = DBname;}

    public DBTable(String tableName) {
        this.tableName = tableName;
        id=0;
    }

    public String getTableName() {return tableName;}

    public void setTableName(String tableName) {this.tableName = tableName;}

    public List<List<String>> getTableValue() {
        return tableValue;
    }

    public void setTableValue(List<List<String>> tableValue) {
        this.tableValue = tableValue;
    }

    public int getRowNumber() {return tableValue.size();}

    public int getColNumber() {return tableValue.get(0).size();}

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public int initID(){
        int row = getRowNumber();
        if (row-1 > 0){
            id = Integer.valueOf(tableValue.get(row-1).get(0));
        }else{
            id = 0;
        }
        return id;
    }

    public void setID(int id){this.id = id;}

    public int getId(){return id;}

    public void addCol(){
        int row = getRowNumber();
        int col = getColNumber();
        for (int i=0; i<row;i++){
            (tableValue.get(i)).add(col,"NULL");
        }
    }

    public void deleteCol(int col){
        int row = getRowNumber();
        for (int i=0; i<row && col>0; i++){
            (tableValue.get(i)).remove(col);
        }
    }

    public void addRow(){
        id++;
        int row = getRowNumber();
        int col = getColNumber();
        ArrayList <String> cols = new ArrayList<>();
        for (int j=0; j<col;j++){
            cols.add(j,"NULL");
        }
        String idStr = String.valueOf(id);
        cols.set(0,idStr);  //write id
        tableValue.add(row, cols);
    }

    public void deleteRow(int row){
        int col = getColNumber();
        if(row>0){
            for (int j=col-1; j>=0;j--){
                tableValue.get(row).remove(j);
            }
            tableValue.remove(row);
        }
    }

    public void writeValue(int row, int col, String value){
        tableValue.get(row).set(col,value);
    }

}

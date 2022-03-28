package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class DataBase {

    private String DBname;  //database name
    List<DBTable> tables;  //DB-->Tables

    public DataBase(String DBname) {
        this.DBname = DBname;
        setTables();
    }

    public String getDBname() {
        return DBname;
    }

    public void setDBname(String DBname) {
        this.DBname = DBname;
    }

    public void setTables(){
        if (tables==null){
            tables = new ArrayList<>();
        }
    }

    public List<DBTable> getTables(){
        return tables;
    }

}

package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class sqlCreateCmd extends DBcmd{

    public sqlCreateCmd() {

    }

    public void createSetup(){
        setAttriList();
        setTableNames();
    }

    public void runCreate() throws DBException{
        if (commandType.equalsIgnoreCase("CreateDatabase")){
            createDB();
        }else if(commandType.equalsIgnoreCase("CreateTable")){
            createTable();
        }
    }

    public void createDB() throws DBException{
        if (!isPlainText(DBname)){
            throw new DBException("[ERROR] Illegal DaraBase Name!");
        }
        RepeatOp repeatOp = new RepeatOp(DBList);
        boolean existDB = repeatOp.existDB(DBname);
        if (existDB){
            throw new DBException("[ERROR] DataBase Already exists!");
        }
        DataBase newDB = new DataBase(DBname);
        DBList.add(newDB);
        //add DB into idCounter
        DBidCount newIdCount = new DBidCount();
        newIdCount.DBName = DBname;
        idCounts.add(newIdCount);

        result = "[OK] DataBase " + DBname + " has been created";
    }

    public void createTable() throws DBException{
        RepeatOp repeatOp = new RepeatOp(DBList);
        int DBPos = repeatOp.findCurrentDB(currentDB);
        boolean existTable = repeatOp.existTable(DBPos,tableNames.get(0));
        if (existTable){
            throw new DBException("[ERROR] Table Already exists!");
        }

        //create table
        if (!isPlainText(tableNames.get(0))){
            throw new DBException("[ERROR] Illegal Table Name!");
        }
        DBTable newTable = new DBTable(tableNames.get(0));     //only the first one has value
        //whether table exists
        int idDBNum = 0;
        for (int i=0; i<idCounts.size();i++){
            if (currentDB.equalsIgnoreCase(idCounts.get(i).DBName)){
                idDBNum = i;
                break;
            }
        }
        for (int i=0; i<idCounts.get(idDBNum).tableNames.size();i++){
            if (tableNames.get(0).equalsIgnoreCase(idCounts.get(idDBNum).tableNames.get(i))){
                newTable.setID(idCounts.get(idDBNum).ids.get(i));
                break;
            }
        }

        newTable.setDBname(currentDB);
        newTable.setKeys("id");
        List<List<String>> tableValue = new ArrayList<>();
        List<String> rowValue = new ArrayList<>();
        rowValue.add("id");
        for (int i=0; i<attriList.size();i++){
            if (!isPlainText(attriList.get(i))){
                throw new DBException("[ERROR] Illegal AttributeName Name!");
            }
            if (attriList.get(i).equalsIgnoreCase("id")){
                throw new DBException("[ERROR] ID is Illegal AttributeName Name!");
            }
            rowValue.add(attriList.get(i));
        }
        tableValue.add(rowValue);
        newTable.setTableValue(tableValue);
        //add table to DBList
        DBList.get(DBPos).tables.add(newTable);
        //add table to idCounter
        addTtoIdcount(DBPos,tableNames.get(0),newTable.getId());

        result = "[OK] Table " + tableNames.get(0) + " has been created";
    }

    public void addTtoIdcount(int DBPos, String TName, int id){
        for (int i=0; i<idCounts.size();i++){
            String tmpName = DBList.get(DBPos).getDBname();
            if (idCounts.get(i).DBName.equalsIgnoreCase(tmpName)){
                idCounts.get(i).tableNames.add(TName);
                idCounts.get(i).ids.add(id);
            }
        }
    }

}

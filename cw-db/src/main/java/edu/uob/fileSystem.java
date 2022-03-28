package edu.uob;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class fileSystem {
    File databaseDirectory;

    public fileSystem(File databaseDirectory) {
        this.databaseDirectory = databaseDirectory;
    }

    public List<DataBase> loadDB() throws IOException{
        List<DataBase> DBList = new ArrayList<>();
        File folders[] = databaseDirectory.listFiles();  //All folders under the storage path (DB) (storage path)
        for (int i=0; i<folders.length;i++){
            String DBname = folders[i].getName();
            DataBase newDB = new DataBase(DBname);  //Name of the database
            File files[] = folders[i].listFiles();  //one DB : TABLES
            for (int j=0; j<files.length;j++){
                DBTable newTable = loadFile(files[j],DBname);
                newDB.tables.add(newTable);   //Add table to DB
            }
            DBList.add(newDB);
        }
        return DBList;
    }

    public DBTable loadFile(File fileToOpen, String DBName) throws IOException {
        FileReader reader = new FileReader(fileToOpen);
        BufferedReader buffReader = new BufferedReader(reader);
        String line = null;
        List<List<String>> tableValue = new ArrayList<>();  //store table value
        int count = 0;  //pointer
        while ((line = buffReader.readLine()) != null) {
            List<String> rowValue = new ArrayList<>();
            String tmpStr[] = line.split("\t");  //tab
            for (int i = 0; i < tmpStr.length; i++) {
                rowValue.add(i, tmpStr[i]);
            }
            tableValue.add(count, rowValue);
            count++;
        }
        buffReader.close();
        String tableName = fileToOpen.getName();
        tableName = tableName.substring(0,tableName.indexOf("."));
        DBTable sqlTable = new DBTable(tableName);  //table name（without .tab）
        sqlTable.setDBname(DBName);  //DBname
        sqlTable.setTableValue(tableValue);
        sqlTable.setKeys("id");
        int tmp = sqlTable.initID();
        return sqlTable;
    }

    public void saveDB(List<DataBase> DBList) throws IOException {
        for (int i=0; i<DBList.size();i++){
            String DBName = DBList.get(i).getDBname();
            String folderPos = databaseDirectory + File.separator + DBName;
            File folder = new File(folderPos);
            if(!folder.exists()){
                folder.mkdir();  //create folder
            }
            for (int j=0; j<DBList.get(i).tables.size();j++){
                saveTable(DBList.get(i).tables.get(j));
            }
        }
    }

    public void saveTable(DBTable table) throws IOException {
        String filePos = databaseDirectory + File.separator +table.getDBname()+ File.separator;
        String fileName = table.getTableName()+".tab";
        File file = new File(filePos,fileName);
        if (!file.exists()) {   //create file
            file.createNewFile();
        }
        //not exist --> create, exist --> overwrite
        FileWriter writeFile = new FileWriter(file);
        for (int i=0; i<table.getRowNumber();i++){
            for (int j=0; j<table.getColNumber();j++){
                writeFile.write(table.tableValue.get(i).get(j));
                if (j != table.getColNumber()-1) {
                    writeFile.write("\t");
                }else{
                    writeFile.write("\n");
                }
            }
        }
        writeFile.close();
    }

    public void deleteDB(DataBase DB) throws DBException{
        String folderPos = databaseDirectory + File.separator + DB.getDBname() +File.separator;
        File folder = new File(folderPos);
        if (folder.exists()){
            File[] files = folder.listFiles();
            for (int i=0; i<files.length;i++){  //Delete all tables under folder
                files[i].delete();
            }
            folder.delete();
        }else{
            throw new DBException("[ERROR] FIle Doesn't Exist!");
        }
    }

    public void deleteTable(DBTable table) throws DBException{
        String filePos = databaseDirectory + File.separator +table.getDBname()+ File.separator
                + table.getTableName()+".tab";
        File file = new File(filePos);
        if (file.exists()){
            file.delete();
        }else{
            throw new DBException("[ERROR] FIle Doesn't Exist!");
        }
    }

    public List<DBidCount> setIDs(List<DataBase> DBList){
        List<DBidCount> idCounts = new ArrayList<>();
        for (int i=0;i<DBList.size();i++){
            DBidCount idCount = new DBidCount();
            idCount.DBName = DBList.get(i).getDBname();
            for (int j=0; j<DBList.get(i).tables.size();j++){
                idCount.tableNames.add(DBList.get(i).tables.get(j).getTableName());
                idCount.ids.add(DBList.get(i).tables.get(j).initID());
            }
            idCounts.add(idCount);
        }
        return idCounts;
    }

    public List<DBidCount> resetIDs(List<DataBase> DBList, List<DBidCount> idCounts){
        for (int i=0; i<DBList.size();i++){
            for (int j=0; j< idCounts.size();j++){
                //same DB
                if (DBList.get(i).getDBname().equalsIgnoreCase(idCounts.get(j).DBName)){
                    for (int k=0; k<DBList.get(i).tables.size();k++){
                        //same table
                        for (int t=0; t<idCounts.get(j).tableNames.size();t++){
                            if (DBList.get(i).tables.get(k).getTableName().equalsIgnoreCase(idCounts.get(j).tableNames.get(t))){
                                idCounts.get(j).ids.set(t,(DBList.get(i).tables.get(k).getId()));
                            }
                        }
                    }
                }
            }
        }
        return idCounts;
    }

}

package edu.uob;

import java.io.File;

public class sqlDropCmd extends DBcmd{
    String dropName;    //database or table name
    File databaseDirectory;

    public sqlDropCmd() {}

    public void runDrop() throws DBException{
        if (!isPlainText(dropName)){
            throw new DBException("[ERROR] Illegal Structure Name!");
        }
        if (commandType.equalsIgnoreCase("DropDatabase")){
            dropDB();
        }else if(commandType.equalsIgnoreCase("DropTable")){
            dropTable();
        }
    }

    public void dropDB() throws DBException{
        RepeatOp repeatOp = new RepeatOp(DBList);
        int DBPos = repeatOp.findCurrentDB(dropName);
        boolean existDB = repeatOp.existDB(dropName);
        if (!existDB){
            throw new DBException("[ERROR] DataBase does not exist!");
        }
        fileSystem deleteFile = new fileSystem(databaseDirectory);
        deleteFile.deleteDB(DBList.get(DBPos));
        //delete DB in DBList
        for (int i=0; i<DBList.get(DBPos).tables.size();i++){
            DBList.get(DBPos).tables.remove(0);
        }
        DBList.remove(DBPos);
        result = "[OK] DataBase " + dropName + " has been dropped";
    }

    public void dropTable() throws DBException{
        RepeatOp repeatOp = new RepeatOp(DBList);
        int DBPos = repeatOp.findCurrentDB(currentDB);
        boolean existTable = repeatOp.existTable(DBPos,dropName);
        if (!existTable){
            throw new DBException("[ERROR] Table does not exist!");
        }
        //delete file and table
        for (int i=0; i<DBList.get(DBPos).tables.size();i++){
            String tmpTName = DBList.get(DBPos).tables.get(i).getTableName();
            if (tmpTName.equalsIgnoreCase(dropName)){
                fileSystem deleteFile = new fileSystem(databaseDirectory);
                deleteFile.deleteTable(DBList.get(DBPos).tables.get(i));
                DBList.get(DBPos).tables.remove(i);
            }
        }
        result = "[OK] Table " + dropName + " has been dropped";
    }
}

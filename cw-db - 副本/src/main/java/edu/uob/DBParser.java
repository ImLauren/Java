package edu.uob;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBParser {
    List<String> commandWord;
    public static int pointer;  //count
    static List<DataBase> DBList;   //DATABASE
    static String currentDB;
    File databaseDirectory;
    static String result;
    static List<DBidCount> idCounts;

    public DBParser(List<String> commandWord, int pointer, String currentDB, List<DataBase> DBList) throws DBException{
        this.commandWord = commandWord;
        this.pointer = pointer;
        this.currentDB = currentDB;
        this.DBList = DBList;

    }

    public void setDitect(File databaseDirectory){
        this.databaseDirectory = databaseDirectory;
    }

    public void setIdCounts(List<DBidCount> idCounts){this.idCounts = idCounts;}

    public String query(DBServer dBServer){return result;}

    public void runCmd() throws DBException{
        Command();
    }

    public void Command() throws DBException{
        //; && not the first one --> end
        if ((commandWord.get(pointer).equals(";")) && pointer!=0){
            return;
        }
        //only ;  -->error
        if ((commandWord.get(pointer).equals(";")) && pointer==0){
            throw new DBException("[ERROR] A single ';' cannot work!");
        }

        if (commandWord.get(pointer).equalsIgnoreCase("Use")){
            pointer++;
            sqlUse();
        }else if(commandWord.get(pointer).equalsIgnoreCase("Create")){
            pointer++;
            sqlCreate();
        }else if(commandWord.get(pointer).equalsIgnoreCase("Drop")){
            pointer++;
            sqlDrop();
        }else if(commandWord.get(pointer).equalsIgnoreCase("Alter")){
            pointer++;
            sqlAlter();
        }else if(commandWord.get(pointer).equalsIgnoreCase("Insert")){
            pointer++;
            sqlInsert();
        }else if(commandWord.get(pointer).equalsIgnoreCase("Select")){
            pointer++;
            sqlSelect();
        }else if(commandWord.get(pointer).equalsIgnoreCase("Update")){
            pointer++;
            sqlUpdate();
        }else if(commandWord.get(pointer).equalsIgnoreCase("Delete")){
            pointer++;
            sqlDelete();
        }else if(commandWord.get(pointer).equalsIgnoreCase("Join")){
            pointer++;
            sqlJoin();
        }else{
            throw new DBException("[ERROR] Expecting a Command!");
        }

        Command();
    }

    public void sqlUse() throws DBException{
        sqlUseCmd inputUse = new sqlUseCmd();
        inputUse.commandType = "Use";
        inputUse.DBList = DBList;
        //Pointer < length of the semicolon subtracted from the command line -- > DBname value
        if (pointer<(commandWord.size()-1)){
            inputUse.DBname = commandWord.get(pointer);
            inputUse.existDB();
            currentDB = inputUse.DBname;
            result = "[OK] " + currentDB + " is using";
        }else{
            throw new DBException("[ERROR] Expecting a DatabaseName!");
        }
        pointer++;
    }

    public void sqlCreate() throws DBException{
        sqlCreateCmd inputCreate = new sqlCreateCmd();
        inputCreate.DBList = DBList;
        inputCreate.currentDB = currentDB;
        inputCreate.idCounts = idCounts;
        if(commandWord.get(pointer).equalsIgnoreCase("DATABASE")){
            inputCreate.commandType = "CreateDatabase";
            pointer++;
            if (pointer<(commandWord.size()-1)){
                inputCreate.DBname = commandWord.get(pointer);
            }else{
                throw new DBException("[ERROR] Expecting a DatabaseName!");
            }
            pointer++;
        }else if(commandWord.get(pointer).equalsIgnoreCase("TABLE")){
            inputCreate.commandType ="CreateTable";
            inputCreate.createSetup();
            pointer++;
            if (pointer<(commandWord.size()-1)){
                inputCreate.tableNames.add(commandWord.get(pointer));
            }else{
                throw new DBException("[ERROR] Expecting a TableName!");
            }
            pointer++;
            sqlAttributeList(inputCreate);
        }else{
            throw new DBException("[ERROR] Expecting a Create Structure!");
        }
        inputCreate.runCreate();   //execute
        DBList = inputCreate.DBList;   //reset DBList
        idCounts = inputCreate.idCounts;
        result = inputCreate.getResult();
    }

    public void sqlDrop() throws DBException{
        sqlDropCmd inputDrop = new sqlDropCmd();
        inputDrop.DBList = DBList;
        inputDrop.currentDB = currentDB;
        inputDrop.databaseDirectory = databaseDirectory;
        if (commandWord.get(pointer).equalsIgnoreCase("DATABASE")){
            inputDrop.commandType = "DropDatabase";
        }else if(commandWord.get(pointer).equalsIgnoreCase("TABLE")){
            inputDrop.commandType = "DropTable";
        }else{
            throw new DBException("[ERROR] Expecting a Drop Structure!");
        }
        pointer++;
        if (pointer<(commandWord.size()-1)){
            inputDrop.dropName = commandWord.get(pointer);
        }else{
            throw new DBException("[ERROR] Expecting a StructureName!");
        }
        inputDrop.runDrop();
        result = inputDrop.getResult();
        pointer++;
    }

    public void sqlAlter() throws DBException{
        if (commandWord.get(pointer).equalsIgnoreCase("TABLE")){
            pointer++;
        }else{
            throw new DBException("[ERROR] Expecting 'ALTER TABLE'!");
        }
        sqlAlterCmd inputAlter = new sqlAlterCmd();
        inputAlter.commandType = "Alter";
        inputAlter.alterTName = commandWord.get(pointer);
        inputAlter.DBList = DBList;
        inputAlter.currentDB = currentDB;
        pointer++;
        //AlterationType
        if (commandWord.get(pointer).equalsIgnoreCase("ADD")){
            inputAlter.alterType = "ADD";
        }else if(commandWord.get(pointer).equalsIgnoreCase("DROP")){
            inputAlter.alterType = "DROP";
        }else{
            throw new DBException("[ERROR] Expecting an AlterationType!");
        }
        pointer++;
        //AttributeName
        if (pointer<(commandWord.size()-1)){
            inputAlter.attriName = commandWord.get(pointer);
        }else{
            throw new DBException("[ERROR] Expecting an AttributeName!");
        }
        inputAlter.runAlter();
        result = inputAlter.getResult();
        pointer++;
    }

    public void sqlInsert() throws DBException{
        if (commandWord.get(pointer).equalsIgnoreCase("INTO")){
            pointer++;
        }else{
            throw new DBException("[ERROR] Expecting 'INSERT INTO'!");
        }
        //insert tableName
        sqlInsertCmd inputInsert = new sqlInsertCmd();
        inputInsert.commandType = "Insert";
        inputInsert.insertTName = commandWord.get(pointer);
        inputInsert.DBList = DBList;
        inputInsert.currentDB = currentDB;
        pointer++;
        //VALUES
        if (commandWord.get(pointer).equalsIgnoreCase("VALUES")){
            pointer++;
        }else{
            throw new DBException("[ERROR] Expecting a 'VALUES'!");
        }
        //(
        if (commandWord.get(pointer).equalsIgnoreCase("(")){
            pointer++;
        }else{
            throw new DBException("[ERROR] Expecting a '('!");
        }
        //value list '(' followed by ')'
        if (commandWord.get(pointer).equalsIgnoreCase(")")){
            throw new DBException("[ERROR] Expecting AttributeList Value!");
        }else{
            while(!commandWord.get(pointer).equalsIgnoreCase(")")){
                inputInsert.valueList.add(commandWord.get(pointer));
                pointer++;
                if ((commandWord.get(pointer).equalsIgnoreCase(","))){
                    pointer++; //comma --> ++
                    if (commandWord.get(pointer).equalsIgnoreCase("）")){
                        //Comma followed by) --> missing value
                        throw new DBException("[ERROR] Expecting a Value!");
                    }
                }
            }
            pointer++;
        }
        inputInsert.runInsert();
        result = inputInsert.getResult();
    }

    public void sqlSelect() throws DBException{
        sqlSelectCmd inputSelect = new sqlSelectCmd();
        inputSelect.commandType = "Select";
        inputSelect.DBList = DBList;
        inputSelect.currentDB = currentDB;
        //WildAttribList
        if (commandWord.get(pointer).equalsIgnoreCase("*")){
            inputSelect.tableAll = true;
        }else{
            inputSelect.selectAttriList();
            sqlAttributeList(inputSelect);
            pointer = pointer-2;
        }
        pointer++;
        //from
        if (commandWord.get(pointer).equalsIgnoreCase("FROM")){
            pointer++;
        }else{
            throw new DBException("[ERROR] Expecting a 'FROM'!");
        }
        //table name
        if(pointer<(commandWord.size()-1)){
            inputSelect.selectTName = commandWord.get(pointer);
        }else{
            throw new DBException("[ERROR] Expecting a TableName!");
        }
        pointer++;
        //end or WHERE
        if (commandWord.get(pointer).equalsIgnoreCase(";")){
            inputSelect.runSelect();
            result = inputSelect.getResult();
            return;
        }else if(commandWord.get(pointer).equalsIgnoreCase("WHERE")){
            pointer++;
            inputSelect.selectCond();
            sqlCondition(inputSelect);
        }else{
            throw new DBException("[ERROR] Expecting a 'WHERE'!");
        }
        inputSelect.runSelect();
        result = inputSelect.getResult();
    }

    public void sqlUpdate() throws DBException{
        sqlUpdateCmd inputUpdate = new sqlUpdateCmd();
        inputUpdate.commandType = "Update";
        inputUpdate.updateTNames = commandWord.get(pointer);
        inputUpdate.DBList = DBList;
        inputUpdate.currentDB = currentDB;
        pointer++;
        if (commandWord.get(pointer).equalsIgnoreCase("SET")){
            pointer++;
        }else{
            throw new DBException("[ERROR] Expecting a 'SET'!");
        }
        //name value list
        sqlNameList(inputUpdate);
        //WHERE
        if (commandWord.get(pointer).equalsIgnoreCase("WHERE")){
            pointer++;
            inputUpdate.updateCond();
            sqlCondition(inputUpdate);
        }else{
            throw new DBException("[ERROR] Expecting a 'WHERE'!");
        }
        inputUpdate.runUpdate();
        result = inputUpdate.getResult();
    }

    public void sqlDelete() throws DBException{
        if (commandWord.get(pointer).equalsIgnoreCase("FROM")){
            pointer++;
        }else{
            throw new DBException("[ERROR] Expecting a 'FROM'!");
        }

        sqlDeleteCmd inputDelete = new sqlDeleteCmd();
        inputDelete.commandType = "Delete";
        inputDelete.deleteTName = commandWord.get(pointer);

        inputDelete.DBList = DBList;
        inputDelete.currentDB = currentDB;
        pointer++;

        if (commandWord.get(pointer).equalsIgnoreCase("WHERE")){
            pointer++;
            inputDelete.deleteCond();
            sqlCondition(inputDelete);
        }else{
            throw new DBException("[ERROR] Expecting a 'WHERE'!");
        }
        inputDelete.runDelete();
        result = inputDelete.getResult();
    }

    public void sqlJoin() throws DBException{
        sqlJoinCmd inputJoin = new sqlJoinCmd();
        inputJoin.commandType = "Join";
        inputJoin.DBList = DBList;
        inputJoin.currentDB = currentDB;
        inputJoin.joinSetup();
        //first table name   list.number==0
        inputJoin.tableNames.add(commandWord.get(pointer));
        pointer++;
        if (commandWord.get(pointer).equalsIgnoreCase("AND")){
            pointer++;
        }else{
            throw new DBException("[ERROR] Expecting an 'AND'!");
        }
        //second table name    list.number==1
        inputJoin.tableNames.add(commandWord.get(pointer));
        pointer++;
        if (commandWord.get(pointer).equalsIgnoreCase("ON")){
            pointer++;
        }else{
            throw new DBException("[ERROR] Expecting a 'ON'!");
        }
        //first AttributeName
        inputJoin.attriList.add(commandWord.get(pointer));
        pointer++;
        if (commandWord.get(pointer).equalsIgnoreCase("AND")){
            pointer++;
        }else{
            throw new DBException("[ERROR] Expecting an 'AND'!");
        }
        //second AttributeName
        inputJoin.attriList.add(commandWord.get(pointer));
        pointer++;
        inputJoin.runJoin();
        result = inputJoin.getResult();
    }

    public void sqlAttributeList(DBcmd inputAttr) throws DBException{
        if(commandWord.get(pointer).equalsIgnoreCase("(")){
            pointer++;
        }else if(inputAttr.commandType.equals("CreateDatabase")||inputAttr.commandType.equals("CreateTable")){
            return;
        }
        //'(' followed by ')'
        if (commandWord.get(pointer).equalsIgnoreCase(")")){
            throw new DBException("[ERROR] Expecting AttributeList Value!");
        }else{
            while(!(commandWord.get(pointer).equalsIgnoreCase(")") ||
                    commandWord.get(pointer).equalsIgnoreCase("FROM"))){
                inputAttr.attriList.add(commandWord.get(pointer));
                pointer++;
                if ((commandWord.get(pointer).equalsIgnoreCase(","))){
                    pointer++;
                    if (commandWord.get(pointer).equalsIgnoreCase(")")){
                        throw new DBException("[ERROR] Expecting an AttributeName!");
                    }
                }
            }
            pointer++;
        }
    }

    public void sqlCondition(DBcmd inputCond) throws DBException{
        int count = pointer; //count brackets
        int frontBracket = 0;
        int backBracket = 0;
        if (commandWord.get(pointer).equalsIgnoreCase(";")){
            throw new DBException("[ERROR] Expecting a Condition!");
        }
        while(!commandWord.get(count).equalsIgnoreCase(";")){
            if (commandWord.get(count).equalsIgnoreCase("(")){
                frontBracket++;
            }else if(commandWord.get(count).equalsIgnoreCase(")")){
                backBracket++;
            }
            count++;
        }
        if (frontBracket != backBracket){
            throw new DBException("[ERROR] Brackets Mismatch!");
        }
        //Change the condition into a Reverse Polish style and pass the result to selectcmd
        sqlRePolish inputPolish = new sqlRePolish(commandWord,pointer);
        pointer = inputPolish.setRePolish(commandWord,pointer);
        inputCond.conditions = inputPolish.getConditions();
    }

    public void sqlNameList(sqlUpdateCmd inputUpdate) throws DBException{
        inputUpdate.setUpdateValue();
        int count=pointer;  //count whether there is a WHERE
        while (count<commandWord.size() && !commandWord.get(count).equalsIgnoreCase("WHERE")){
            count++;
        }
        if (count==commandWord.size()){
            throw new DBException("[ERROR] Expecting a 'WHERE'!");
        }
        //After determining WHERE, use WHERE to judge the end of the change value
        while(!commandWord.get(pointer).equalsIgnoreCase("WHERE")) {
            List<String> attriNames = new ArrayList<>();
            attriNames.add(commandWord.get(pointer));   //AttributeName
            pointer++;
            if (commandWord.get(pointer).equalsIgnoreCase("=")) {
                pointer++;
            } else {
                throw new DBException("[ERROR] Expecting a '='!");
            }
            if (commandWord.get(pointer).equalsIgnoreCase(",")){   //'=' followed by ','
                throw new DBException("[ERROR] Expecting a Value!");
            }
            attriNames.add(commandWord.get(pointer));   //value
            inputUpdate.updateValue.add(attriNames);   //Save a set of change values to updatecmd
            pointer++;
            if (commandWord.get(pointer).equalsIgnoreCase(",")){
                pointer++;
                if (commandWord.get(pointer).equalsIgnoreCase("WHERE")){
                    throw new DBException("[ERROR] Expecting an AttributeName!");
                }
            }
        }
    }


}

package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class sqlInsertCmd extends DBcmd{
    String insertTName;    //table name
    static List <String> valueList;

    public sqlInsertCmd() {
        valueList = new ArrayList<>();
    }

    public void runInsert() throws DBException{
        if (!isPlainText(insertTName)){
            throw new DBException("[ERROR] Illegal Table Name!");
        }
        RepeatOp repeatOp = new RepeatOp(DBList);
        int DBPos = repeatOp.findCurrentDB(currentDB);
        int TPos = repeatOp.findTable(currentDB,insertTName);
        int insertLen = valueList.size();
        int rowLen = DBList.get(DBPos).tables.get(TPos).getColNumber();
        if (insertLen != rowLen-1){   //size not equal --> error
            throw new DBException("[ERROR] Size of ValueList is not correct!");
        }
        insertValue(DBPos,TPos);
    }

    public void insertValue(int DBPos, int TPos) throws DBException{
        for (int i=0; i<valueList.size();i++){
            if (!isValue(valueList.get(i))){
                throw new DBException("[ERROR] Value '"+ valueList.get(i)+ "' is Illegal!");
            }
            valueList.set(i,deleteQuot(valueList.get(i)));  //delete quotation
        }
        DBList.get(DBPos).tables.get(TPos).addRow();
        int row = DBList.get(DBPos).tables.get(TPos).getRowNumber();
        for (int i=0; i<valueList.size();i++){
            DBList.get(DBPos).tables.get(TPos).writeValue(row-1,i+1, valueList.get(i));
        }
        result = "[OK] Value has been Inserted";
    }
}

package edu.uob;

import java.util.Collections;
import java.util.List;

public class sqlDeleteCmd extends DBcmd{
    String deleteTName;  //table name

    public sqlDeleteCmd() {}

    public void deleteCond(){ setConditions(); }

    public void runDelete() throws DBException{
        if(!isPlainText(deleteTName)){
            throw new DBException("[ERROR] Illegal Table Name!");
        }
        RepeatOp repeatOp = new RepeatOp(DBList);
        int DBPos = repeatOp.findCurrentDB(currentDB);
        int TPos = repeatOp.findTable(currentDB,deleteTName);
        checkCond(DBPos,TPos,repeatOp);

        List<Integer> rowNumber = repeatOp.cond(DBList.get(DBPos).tables.get(TPos).tableValue,conditions);
        Collections.sort(rowNumber);
        for (int i=rowNumber.size()-1;i>=0;i--){
            DBList.get(DBPos).tables.get(TPos).deleteRow(rowNumber.get(i));
        }
        result = "[OK] Value has been deleted in "+ deleteTName;
        clearCond();
    }



}

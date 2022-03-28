package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class sqlUpdateCmd extends DBcmd{
    String updateTNames;   //table name
    List<List<String>> updateValue;   //Storage change value; AttributeName(0), value(1)

    public sqlUpdateCmd() {}

    public void setUpdateValue(){
        updateValue = new ArrayList<>();
    }

    public void updateCond(){setConditions();}

    public void runUpdate() throws DBException{
        RepeatOp repeatOp = new RepeatOp(DBList);
        int DBPos = repeatOp.findCurrentDB(currentDB);
        int TPos = repeatOp.findTable(currentDB,updateTNames);
        if(!isPlainText(updateTNames)){
            throw new DBException("[ERROR] Illegal Table Name!");
        }
        for (int i=0; i<updateValue.size();i++){
            if (!isPlainText(updateValue.get(i).get(0))){
                throw new DBException("[ERROR] Illegal AttributeName!");
            }
            if (!repeatOp.existAttri(DBPos,TPos,updateValue.get(i).get(0))){
                throw new DBException("[ERROR] AttributeName "+updateValue.get(i).get(0)+" does not exist!");
            }
            if (!isValue(updateValue.get(i).get(1))){
                throw new DBException("[ERROR] Illegal Value!");
            }
            if (updateValue.get(i).get(0).equalsIgnoreCase("ID")){
                throw new DBException("[ERROR] 'ID' cannot be Updated!");
            }
        }
        checkCond(DBPos,TPos,repeatOp);
        writeValue(DBPos,TPos,repeatOp);
        result = "[OK] Value has been updated in "+updateTNames;
        clearCond();
    }

    public void writeValue(int DBPos, int TPos, RepeatOp repeatOp) throws DBException{
        List<Integer> rowNumber = repeatOp.cond(DBList.get(DBPos).tables.get(TPos).tableValue,conditions);
        //Remove single quotes
        for (int i=0; i<updateValue.size();i++){
            updateValue.get(i).set(1,deleteQuot(updateValue.get(i).get(1)));
        }
        //Write data
        for (int i=0; i<rowNumber.size();i++){
            for (int j=0; j<updateValue.size();j++){
                for (int k=0;k<DBList.get(DBPos).tables.get(TPos).tableValue.get(0).size();k++){
                    String tmpName = DBList.get(DBPos).tables.get(TPos).tableValue.get(0).get(k);
                    if (tmpName.equalsIgnoreCase(updateValue.get(j).get(0))){
                        DBList.get(DBPos).tables.get(TPos).writeValue(rowNumber.get(i),k,updateValue.get(j).get(1));
                    }
                }
            }
        }
    }


}

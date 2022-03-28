package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class sqlSelectCmd extends DBcmd{
    Boolean tableAll;
    String selectTName;   //table name
    static List<List<String>> resultValue;

    public sqlSelectCmd() {
        tableAll = false;
    }

    public void selectAttriList(){
        setAttriList();
    }

    public void selectCond(){
        setConditions();
    }

    public void runSelect() throws DBException{
        if(!isPlainText(selectTName)){
            throw new DBException("[ERROR] Illegal Table Name!");
        }
        RepeatOp repeatOp = new RepeatOp(DBList);
        int DBPos = repeatOp.findCurrentDB(currentDB);
        int TPos = repeatOp.findTable(currentDB,selectTName);
        if (tableAll!=true){
            for (int i=0; i<attriList.size();i++){
                //attributename --> plain text?
                if (!isPlainText(attriList.get(i))){
                    throw new DBException("[ERROR] Illegal AttributeName "+attriList.get(i)+" !");
                }
                //attributename --> exist?
                boolean existName = repeatOp.existAttri(DBPos,TPos,attriList.get(i));
                if (!existName){
                    throw new DBException("[ERROR] AttributeName "+attriList.get(i)+" dose not exist!");
                }
            }
        }
        resultValue = new ArrayList<>();
        if (conditions==null){
            noCond(DBPos, TPos);
        }else{
            withCond(DBPos, TPos);
        }
        writeResult(resultValue);
        clearCond();
    }

    public void noCond(int DBPos, int TPos) throws DBException{
        if (tableAll){
            resultValue = DBList.get(DBPos).tables.get(TPos).tableValue;
        }else{
            int row = DBList.get(DBPos).tables.get(TPos).getRowNumber();
            int col = DBList.get(DBPos).tables.get(TPos).getColNumber();
            List<Integer> valuePos = new ArrayList<>();
            for (int k=0; k<attriList.size();k++){
                for (int j=0; j<col;j++){
                    String tmpValue=DBList.get(DBPos).tables.get(TPos).tableValue.get(0).get(j);
                       if (tmpValue.equalsIgnoreCase(attriList.get(k))){
                           valuePos.add(j);
                           break;
                       }
                }
            }
            for (int i=0; i<row;i++){
                List<String> rowValue = new ArrayList<>();
                for (int j=0; j<valuePos.size();j++){
                    String tmpValue = DBList.get(DBPos).tables.get(TPos).tableValue.get(i).get(valuePos.get(j));
                    rowValue.add(tmpValue);
                }
                resultValue.add(rowValue);
            }
        }
    }

    public void withCond(int DBPos, int TPos) throws DBException{
        RepeatOp repeatOp = new RepeatOp(DBList);
        noCond(DBPos,TPos);  //Filter the attriname first
        List<List<String>> tmpResult = new ArrayList<>();
        checkCond(DBPos,TPos,repeatOp);
        //Number of filtered rows
        List<Integer> rowNumber = repeatOp.cond(DBList.get(DBPos).tables.get(TPos).tableValue,conditions);
        List<String> firstRow = resultValue.get(0);
        tmpResult.add(firstRow);
        if (rowNumber!=null){
            for (int i=0; i<rowNumber.size();i++){
                List<String> rowValue = resultValue.get(rowNumber.get(i));
                tmpResult.add(rowValue);
            }
        }
        //clear condition
        if (conditions.size() > 0) {
            conditions.subList(0, conditions.size()).clear();
        }
        resultValue = tmpResult;
    }

}

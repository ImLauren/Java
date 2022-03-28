package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class sqlJoinCmd extends DBcmd{

    static List<List<String>> resultValue;

    public sqlJoinCmd() {
    }

    public void joinSetup(){
        setAttriList();
        setTableNames();
    }

    public void runJoin() throws DBException{
        RepeatOp repeatOp = new RepeatOp(DBList);
        int DBPos = repeatOp.findCurrentDB(currentDB);
        String tableName1 = tableNames.get(0);
        String tableName2 = tableNames.get(1);
        int TPos1 = repeatOp.findTable(currentDB,tableName1);
        int TPos2 = repeatOp.findTable(currentDB,tableName2);
        String attriName1 = attriList.get(0);
        String attriName2 = attriList.get(1);
        //check table name
        if (!isPlainText(tableName1) || !isPlainText(tableName2)){
            throw new DBException("[ERROR] Illegal Table Name!");
        }
        if (!repeatOp.existTable(DBPos,tableName1)){
            throw new DBException("[ERROR] Table "+tableName1+" does not exist!");
        }
        if (!repeatOp.existTable(DBPos,tableName2)){
            throw new DBException("[ERROR] Table "+tableName2+" does not exist!");
        }
        //check AttributeName
        if (!isPlainText(attriName1) || !isPlainText(attriName2)){
            throw new DBException("[ERROR] Illegal AttributeName!");
        }
        if (!repeatOp.existAttri(DBPos,TPos1,attriName1)){
            throw new DBException("[ERROR] AttributeName "+attriName1+" does not exist!");
        }
        if (!repeatOp.existAttri(DBPos,TPos2,attriName2)){
            throw new DBException("[ERROR] AttributeName "+attriName2+" does not exist!");
        }
        innerJoin(attriName1,attriName2,DBPos,TPos1,TPos2);
        addID();
        writeResult(resultValue);
    }

    public void innerJoin(String attriName1, String attriName2, int DBPos, int TPos1, int TPos2){
        resultValue = new ArrayList<>();
        int attrNum1 = findAttrNum(DBPos,TPos1,attriName1);
        int attrNum2 = findAttrNum(DBPos,TPos2,attriName2);
        int row1 = DBList.get(DBPos).tables.get(TPos1).getRowNumber();
        int row2 = DBList.get(DBPos).tables.get(TPos2).getRowNumber();
        int col1 = DBList.get(DBPos).tables.get(TPos1).getColNumber();
        int col2 = DBList.get(DBPos).tables.get(TPos2).getColNumber();
        List<String> rowPart1 = new ArrayList<>();
        List<String> rowPart2 = new ArrayList<>();
        List<String> firstRow1 = new ArrayList<>();
        List<String> firstRow2 = new ArrayList<>();
        firstRow1 = getPartRow(DBPos,TPos1,0,col1,attrNum1);
        firstRow2 = getPartRow(DBPos,TPos2,0,col2,attrNum2);
        firstRow1.addAll(firstRow2);
        resultValue.add(firstRow1);

        for (int i=1; i<row1;i++){
            String value1 = DBList.get(DBPos).tables.get(TPos1).tableValue.get(i).get(attrNum1);
            value1 = trueFalse(value1);
            for (int j=1; j<row2;j++){
                String value2 = DBList.get(DBPos).tables.get(TPos2).tableValue.get(j).get(attrNum2);
                value2 = trueFalse(value2);
                if (value1.equals(value2)){
                    rowPart1 = getPartRow(DBPos,TPos1,i,col1,attrNum1);
                    rowPart2 = getPartRow(DBPos,TPos2,j,col2,attrNum2);
                    rowPart1.addAll(rowPart2);
                    resultValue.add(rowPart1);
                }
            }
        }

    }

    public List<String> getPartRow(int DBPos, int TPos, int row, int col,int attrNum ){
        List<String> rowPart = new ArrayList<>();
        for (int j=0; j<col;j++){
            if (j!=attrNum && j!=0){
                rowPart.add(DBList.get(DBPos).tables.get(TPos).tableValue.get(row).get(j));
            }
        }
        return rowPart;
    }

    public int findAttrNum(int DBPos, int TPos, String attriName){
        int attrNum=0;
        int col = DBList.get(DBPos).tables.get(TPos).getColNumber();
        for (int j=0; j<col;j++){
            String tmpName = DBList.get(DBPos).tables.get(TPos).tableValue.get(0).get(j);
            if (tmpName.equalsIgnoreCase(attriName)){
                attrNum = j;
            }
        }
        return attrNum;
    }

    public String trueFalse(String value){
        if (value.equalsIgnoreCase("TRUE") || value.equalsIgnoreCase("FALSE")){
            value = value.toUpperCase();
        }
        return value;
    }

    public void addID(){
        int id=1;
        //add one after each row
        for (int i=0; i<resultValue.size();i++){
            resultValue.get(i).add("NULL");
        }
        //Move back a bit
        for (int i=0; i<resultValue.size();i++){
            for (int j=resultValue.get(0).size()-1; j>0;j--){
                resultValue.get(i).set(j,resultValue.get(i).get(j-1));
            }
        }
        //add id
        for (int i=0; i<resultValue.size();i++){
            if (i==0){
                resultValue.get(i).set(i,"id");
            }else{
                resultValue.get(i).set(0,String.valueOf(id));
                id++;
            }
        }
    }
}

package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class DBcmd {
    static List<Condition> conditions;
    List<String> tableNames;
    List <String> attriList;
    String DBname;
    String commandType;
    static List<DataBase> DBList;  //all DataBases
    String currentDB;
    static String result;
    static List<DBidCount> idCounts;

    public DBcmd() {}

    public void setAttriList(){
        if (attriList==null){
            attriList = new ArrayList<>();
        }
    }

    public void setTableNames(){
        if (tableNames == null){
            tableNames = new ArrayList<>();
        }
    }

    public void setConditions(){
        if (conditions == null){
            conditions = new ArrayList<>();
        }
    }

    public String getResult(){
        return result;
    }

    public boolean isPlainText(String name){
        boolean result = false;
        if (name!=null && name.matches("(\\w)+")) {   //[0—9A—Z_a—z]
            result = true;
        }
        return result;
    }

    public boolean isValue(String value){
        boolean result;
        if (value.startsWith("'") && value.endsWith("'")){
            result = true;
        }else if (value.matches("^-?\\d+$")){  //int
            result = true;
        }else if (value.matches("^(-?\\d+)(\\.\\d+)?$")){   //float
            result = true;
        }else if (value.equalsIgnoreCase("TRUE") || value.equalsIgnoreCase("FALSE")){
            result = true;
        }else{
            result = false;
        }
        return result;
    }

    public String deleteQuot(String value){
        if (value.startsWith("'") && value.endsWith("'")){
            int len = value.length();
            value = value.substring(1,len-1);
        }
        return value;
    }

    public void checkCond(int DBPos, int TPos, RepeatOp repeatOp) throws DBException{
        for (int i=0; i<conditions.size();i++){
            //condition: attributename --> Illegal?
            if ((conditions.get(i).logicOp==null) && (!isPlainText(conditions.get(i).AttriName))){
                throw new DBException("[ERROR] Illegal AttributeName "+conditions.get(i).AttriName+" !");
            }
            //condition: attributename --> in table?
            if ((conditions.get(i).logicOp==null) && (!repeatOp.existAttri(DBPos,TPos,conditions.get(i).AttriName))){
                throw new DBException("[ERROR] AttributeName "+conditions.get(i).AttriName+" does not exist!");
            }
            //value  -->Illegal?
            if ((conditions.get(i).logicOp==null) && (!isValue(conditions.get(i).Value))){
                throw new DBException("[ERROR] Illegal Value!");
            }
            //value --> in table?
            if ((conditions.get(i).logicOp==null) && (conditions.get(i).Operator.equalsIgnoreCase("==")) &&
                    (!valueInTable(DBPos,TPos,conditions.get(i).AttriName,conditions.get(i).Value))){
                throw new DBException("[ERROR] Value"+conditions.get(i).Value+" does not exist!");
            }
        }
    }

    public boolean valueInTable(int DBPos, int TPos,String attriName, String value){
        RepeatOp repeatOp = new RepeatOp(DBList);
        int attriNum = repeatOp.findAttriNum( DBList.get(DBPos).tables.get(TPos).tableValue,attriName);
        int row = DBList.get(DBPos).tables.get(TPos).getRowNumber();
        for (int i=0; i<row;i++){
            String tmpValue = DBList.get(DBPos).tables.get(TPos).tableValue.get(i).get(attriNum);
            value = deleteQuot(value);
            if(tmpValue.equals(value)){
                return true;
            }
        }
        return false;
    }

    public void writeResult(List<List<String>> resultValue){
        result = "[OK]"+"\r\n";
        for (int i=0; i<resultValue.size();i++){
            for (int j=0; j<resultValue.get(0).size();j++){
                result = result + resultValue.get(i).get(j);
                result = result +"\t";
            }
            result = result +"\r\n" ;
        }
    }

    public void clearCond(){
        conditions = null;
    }

}

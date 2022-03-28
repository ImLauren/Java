package edu.uob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RepeatOp {
    List<DataBase> DBList;

    public RepeatOp(List<DataBase> DBList) {
        this.DBList = DBList;
    }

    public int findCurrentDB(String currentDB) throws DBException{
        if (currentDB==null){
            throw new DBException("[ERROR] No Available DataBase!");
        }
        int result = -1;
        for (int i=0; i<DBList.size();i++){
            String tmpName = DBList.get(i).getDBname();
            if (tmpName.equalsIgnoreCase(currentDB)){
                result = i;
            }
        }
        if (result==-1){
            throw new DBException("[ERROR] No Available DataBase!");
        }
        return result;
    }

    public int findTable(String currentDB, String tableName) throws DBException{
        int result=-1;
        int DBPos = findCurrentDB(currentDB);
        for (int i=0; i<DBList.get(DBPos).tables.size();i++){
            String tmpName = DBList.get(DBPos).tables.get(i).getTableName();
            if (tmpName.equalsIgnoreCase(tableName)){
                result=i;
            }
        }
        if (result==-1){
            throw new DBException("[ERROR] No Available Table!");
        }
        return result;
    }

    public boolean existDB(String DBname) throws DBException{
        boolean result = false;
        for (int i=0; i<DBList.size();i++){
            if (DBList.get(i).getDBname().equalsIgnoreCase(DBname)){
                return true;
            }
        }
        return result;
    }

    public boolean existTable(int DBPos, String TName) throws DBException{
        boolean result = false;
        for (int i=0; i<DBList.get(DBPos).tables.size();i++){
            String tmpName =DBList.get(DBPos).tables.get(i).getTableName();
            if (tmpName.equalsIgnoreCase(TName)){
                return true;
            }
        }
        return result;
    }

    public boolean existAttri(int DBPos, int TPos,String attriName) throws DBException{
        boolean result = false;
        int col = DBList.get(DBPos).tables.get(TPos).getColNumber();
        for (int j=0; j<col;j++){
            String tmpName = DBList.get(DBPos).tables.get(TPos).tableValue.get(0).get(j);
            if (tmpName.equalsIgnoreCase(attriName)){
                return true;
            }
        }
        return result;
    }

    public List<Integer> cond(List<List<String>> table, List<Condition> conditions) throws DBException{
        List<Integer> result = new ArrayList<>();
        sqlStack opStack = new sqlStack(); //Establish stack storage operators, etc
        Condition cond1;
        Condition cond2;
        Condition condRe;

        if (conditions.size()==1){
            conditions.get(0).setRowNumber(condOp(table,conditions.get(0)).getRowNumber());
        }

        for (int i=0; i<conditions.size();i++){
            if (conditions.get(i).logicOp==null){   //Non operator push on stack
                opStack.pushCond(conditions.get(i));
            }else{
                if (!opStack.isEmptyCond()){     //Two conditions pop up when an operator is encountered
                    cond1 = opStack.popCond();
                    cond2 = opStack.popCond();
                    if (conditions.get(i).logicOp.equalsIgnoreCase("OR")){
                        if (cond1.getRowNumber()==null){
                            cond1 = condOp(table,cond1);
                        }
                        if (cond2.getRowNumber()==null){
                            cond2 = condOp(table,cond2);
                        }
                        Condition newCond = condOR(cond1,cond2);
                        opStack.pushCond(newCond);
                    }else if(conditions.get(i).logicOp.equalsIgnoreCase("AND")){
                        if (cond1.getRowNumber()==null){
                            cond1 = condOp(table,cond1);
                        }
                        if (cond2.getRowNumber()==null){
                            cond2 = condOp(table,cond2);
                        }
                        Condition newCond = condAND(cond1,cond2);
                        opStack.pushCond(newCond);
                    }
                }
            }
        }
        if(!opStack.isEmptyCond()){
            condRe = opStack.popCond();
            result = condRe.getRowNumber();
        }
        return result;
    }

    public Condition condOp(List<List<String>> table, Condition cond) throws DBException{
        List<Integer> rowNumber = new ArrayList<>();
        String op = cond.Operator;
        if (op.equalsIgnoreCase("==") || op.equalsIgnoreCase("!=")){
            rowNumber = condPreEqual(table,cond,op);
        }else if(op.equalsIgnoreCase(">") || op.equalsIgnoreCase("<") ||
                op.equalsIgnoreCase(">=") || op.equalsIgnoreCase("<=")){
            rowNumber = condCompare(table,cond,op);
        }else if(op.equalsIgnoreCase("LIKE")){
            rowNumber = condLike(table,cond);
        }
        cond.setRowNumber(rowNumber);
        return cond;
    }

    public Condition condAND(Condition cond1,Condition cond2) throws DBException{
        Condition result = new Condition();
        List<Integer> rowNumber = new ArrayList<>();
        for (int i=0; i<cond1.getRowNumber().size();i++){
            for (int j=0; j<cond2.getRowNumber().size();j++){
                if(Objects.equals(cond1.getRowNumber().get(i), cond2.getRowNumber().get(j))){
                    rowNumber.add(cond1.getRowNumber().get(i));
                }
            }
        }
        Collections.sort(rowNumber);
        result.setRowNumber(rowNumber);
        return result;
    }

    public Condition condOR(Condition cond1,Condition cond2) throws DBException{
        Condition result = new Condition();
        List<Integer> rowNumber = new ArrayList<>();
        rowNumber = cond1.getRowNumber();
        for (int j=0; j<cond2.getRowNumber().size();j++){
            int flag = 1;
            for(int i=0;i<rowNumber.size();i++){
                if (Objects.equals(cond2.getRowNumber().get(j), rowNumber.get(i))){
                    flag=0;
                }
            }
            if (flag == 1){
                rowNumber.add(cond2.getRowNumber().get(j));
            }
        }
        Collections.sort(rowNumber);
        result.setRowNumber(rowNumber);
        return result;
    }

    public List<Integer> condPreEqual(List<List<String>> table,Condition cond, String op) throws DBException{
        List<Integer> result = new ArrayList<>();
        String attriName = cond.AttriName;
        String value = cond.Value;
        if (value.startsWith("'") && value.endsWith("'")){
            int len = value.length();
            value = value.substring(1,len-1);
        }
        if(value.equalsIgnoreCase("TRUE")||value.equals("FALSE")||value.equals("NULL")){
            value = value.toUpperCase();
        }
        int attriNum = findAttriNum(table,attriName);
        for (int i=1; i<table.size();i++){
            if(table.get(i).get(attriNum).equalsIgnoreCase("TRUE")||
                    table.get(i).get(attriNum).equalsIgnoreCase("FALSE")||
                    table.get(i).get(attriNum).equalsIgnoreCase("NULL")){
                table.get(i).set(attriNum,table.get(i).get(attriNum).toUpperCase());
            }
            if (op.equalsIgnoreCase("==")){
                result = condEqual(result,table,i,attriNum,value);
            }else if(op.equalsIgnoreCase("!=")){
                result = condNotEqual(result,table,i,attriNum,value);
            }
        }
        return result;
    }

    public List<Integer> condEqual(List<Integer> result, List<List<String>> table, int i, int attriNum, String value){
        if (table.get(i).get(attriNum).equals(value)){
            result.add(i);
        }
        return result;
    }

    public List<Integer> condNotEqual(List<Integer> result, List<List<String>> table, int i, int attriNum, String value) throws DBException{
        if (!table.get(i).get(attriNum).equals(value)){
            result.add(i);
        }
        return result;
    }

    public List<Integer> condCompare(List<List<String>> table,Condition cond,String op) throws DBException{
        List<Integer> result = new ArrayList<>();
        String attriName = cond.AttriName;
        String value = cond.Value;
        if (! (value.matches("^-?\\d+$") || value.matches("^(-?\\d+)(\\.\\d+)?$"))){
            throw new DBException("[ERROR] Value Cannot Do This Operator!");
        }
        float tmpValue = Float.parseFloat(value);
        int attriNum = findAttriNum(table,attriName);

        if (op.equalsIgnoreCase(">")){
            result = condGreater(table,attriNum,tmpValue,result);
        }else if(op.equalsIgnoreCase("<")){
            result = condLess(table,attriNum,tmpValue,result);
        }else if(op.equalsIgnoreCase(">=")){
            result = condGreaterEuq(table,attriNum,tmpValue,result);
        }else if(op.equalsIgnoreCase("<=")){
            result = condLessEuq(table,attriNum,tmpValue,result);
        }

        return result;
    }

    public List<Integer> condGreater(List<List<String>> table, int attriNum, float tmpValue, List<Integer> result){
        for (int i=1; i<table.size();i++){
            float tmpNum = Float.parseFloat(table.get(i).get(attriNum));
            if (tmpNum>tmpValue){
                result.add(i);
            }
        }
        return result;
    }

    public List<Integer> condLess(List<List<String>> table, int attriNum, float tmpValue, List<Integer> result) throws DBException{
        for (int i=1; i<table.size();i++){
            float tmpNum = Float.parseFloat(table.get(i).get(attriNum));
            if (tmpNum<tmpValue){
                result.add(i);
            }
        }
        return result;
    }

    public List<Integer> condGreaterEuq(List<List<String>> table, int attriNum, float tmpValue, List<Integer> result) throws DBException{
        for (int i=1; i<table.size();i++){
            float tmpNum = Float.parseFloat(table.get(i).get(attriNum));
            if (tmpNum>=tmpValue){
                result.add(i);
            }
        }
        return result;
    }

    public List<Integer> condLessEuq(List<List<String>> table, int attriNum, float tmpValue, List<Integer> result) throws DBException{
        for (int i=1; i<table.size();i++){
            float tmpNum = Float.parseFloat(table.get(i).get(attriNum));
            if (tmpNum<=tmpValue){
                result.add(i);
            }
        }
        return result;
    }

    public List<Integer> condLike(List<List<String>> table,Condition cond) throws DBException{
        List<Integer> result = new ArrayList<>();
        String attriName = cond.AttriName;
        String value = cond.Value;
        if (value.startsWith("'") && value.endsWith("'")){
            int len = value.length();
            value = value.substring(1,len-1);
        }
        int attriNum = findAttriNum(table,attriName);
        for (int i=1; i<table.size();i++){
            if (table.get(i).get(attriNum).contains(value)){
                result.add(i);
            }
        }
        return result;
    }

    public int findAttriNum(List<List<String>> table, String attriName){
        int attriNum = 0;
        for (int j=0; j<table.get(0).size();j++){
            if(table.get(0).get(j).equalsIgnoreCase(attriName)){
                attriNum = j;
            }
        }
        return attriNum;
    }

}

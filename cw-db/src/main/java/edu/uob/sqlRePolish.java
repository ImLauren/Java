package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class sqlRePolish {

    sqlStack expStack = new sqlStack(); //Establish stack storage operators, etc
    List<String> commandWord;
    int pointer;
    static List<Condition> conditions = new ArrayList<>();  //List of Reverse Polish expressions

    public sqlRePolish(List<String> commandWord, int pointer) throws DBException{
        this.commandWord = commandWord;
        this.pointer = pointer;
    }

    public int setRePolish(List<String> commandWord, int pointer) throws DBException{
        clearCond();
        while(!commandWord.get(pointer).equalsIgnoreCase(";")){
            String tmpStr = commandWord.get(pointer);
            if (tmpStr.equalsIgnoreCase("(")){
                expStack.push(tmpStr);      //Bracket pop in stack (string)
            }else if(tmpStr.equalsIgnoreCase(")")){
                //Until the left parenthesis is encountered
                while (!expStack.peek().equals("(")) {
                    Condition inputLog = new Condition();
                    //Pop up the symbol stack to the data set (only OR or AND), change it to condition first
                    inputLog.logicOp = expStack.pop();
                    conditions.add(inputLog);
                }
                expStack.pop();   //Pop up "(", eliminate parentheses
            }else if(tmpStr.equalsIgnoreCase("AND") || tmpStr.equalsIgnoreCase("OR")){
                expStack.push(tmpStr);  //Direct stack
            }else{  //condition -->  eg. id==1
                Condition inputCon = new Condition();
                inputCon.AttriName = tmpStr;
                pointer++;
                tmpStr = commandWord.get(pointer);
                if(tmpStr.equals("==")||tmpStr.equals(">")||tmpStr.equals("<")||tmpStr.equals(">=")||
                        tmpStr.equals("<=")||tmpStr.equals("!=")||tmpStr.equalsIgnoreCase("LIKE")){
                    inputCon.Operator = tmpStr;
                }else{
                    throw new DBException("[ERROR] Expecting an Operator!");
                }
                pointer++;
                tmpStr = commandWord.get(pointer);
                if (tmpStr.equals(")")||tmpStr.equals("(")||tmpStr.equalsIgnoreCase("AND")||
                tmpStr.equalsIgnoreCase("OR")){
                    throw new DBException("[ERROR] Expecting a Value!");
                }else{
                    inputCon.Value = tmpStr;
                }
                conditions.add(inputCon);   //Put the condition on the stack
            }
            pointer++;
        }
        //Add the remaining symbols in the symbol stack to the data set
        while (!expStack.isEmpty()){
            Condition inputLog = new Condition();
            inputLog.logicOp = expStack.pop();
            conditions.add(inputLog);
        }
        return pointer;
    }

    public void clearCond(){
        if (conditions!=null){
            //clear condition
            if (conditions.size() > 0) {
                conditions.subList(0, conditions.size()).clear();
            }
        }
    }

    public List<Condition> getConditions(){
        return conditions;
    }


}

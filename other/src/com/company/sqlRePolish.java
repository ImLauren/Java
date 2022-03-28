package com.company;

import java.util.ArrayList;
import java.util.List;

public class sqlRePolish {

    sqlStack expStack = new sqlStack(); //建立栈 存放运算符等
    List<String> commandWord;
    int pointer;
    static List<Condition> conditions = new ArrayList<>();  //后缀表达式的list

    public sqlRePolish(List<String> commandWord, int pointer) throws DBException{  //将commandWord和pointer传入
        this.commandWord = commandWord;
        this.pointer = pointer;
    }

    public int setRePolish(List<String> commandWord, int pointer) throws DBException{
        while(!commandWord.get(pointer).equalsIgnoreCase(";")){
            String tmpStr = commandWord.get(pointer); //现在指向的字符串
            if (tmpStr.equalsIgnoreCase("(")){
                expStack.push(tmpStr);   //括号直接入栈(String)
            }else if(tmpStr.equalsIgnoreCase(")")){
                while (!expStack.peek().equals("(")) {  //直到遇到左括号
                    Condition inputLog = new Condition();
                    inputLog.logicOp = expStack.pop(); //弹出符号栈到数据集合(符号只有or或者and),先变为Condition
                    conditions.add(inputLog);  //弹出符号栈到数据集合(符号只有or或者and)
                }
                expStack.pop();   //弹出 "(" ,就是消除括号
            }else if(tmpStr.equalsIgnoreCase("AND") || tmpStr.equalsIgnoreCase("OR")){
                expStack.push(tmpStr);  //直接入栈
            }else{  //纯condition -->  eg. id==1
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
                conditions.add(inputCon);   //把condition入栈
            }
            pointer++;
        }
        //将剩余在符号栈中的符号加入数据集合
        while (!expStack.isEmpty()){
            Condition inputLog = new Condition();
            inputLog.logicOp = expStack.pop();
            conditions.add(inputLog);
        }
        return pointer;
    }



}

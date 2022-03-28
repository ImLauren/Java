package com.company;

import java.util.List;

public class sqlSelect {
    List<String> commandWord;
    int count;  //计数ArrayList到第几个单词

    public sqlSelect(List<String> commandWord, int count) {
        this.commandWord = commandWord;
        this.count = count;

        wildAttribList();

    }

    public void wildAttribList(){
        if (commandWord.get(count).equalsIgnoreCase("*")){
            //操作
        }else{
            sqlAttributeList inputAttributeList = new sqlAttributeList(commandWord,count);
            count = inputAttributeList.count;
        }

        count++;
        if (commandWord.get(count).equalsIgnoreCase("FROM")){
            count++;
        }else{
            //报错
        }

        //tablename test
        count++;

        if (commandWord.get(count).equalsIgnoreCase("WHERE")){
            count++;
            sqlCondition inputCondition = new sqlCondition(commandWord,count);
            count = inputCondition.count;
        }


    }



}

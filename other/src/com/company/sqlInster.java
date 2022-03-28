package com.company;

import java.util.List;

public class sqlInster {

    List<String> commandWord;
    int count;  //计数ArrayList到第几个单词

    public sqlInster(List<String> commandWord, int count) {
        this.commandWord = commandWord;
        this.count = count;

        if (commandWord.get(count).equalsIgnoreCase("INTO")){
            count++;
            insterTable();
        }else{
            //报错
        }

        count++;
    }

    public void insterTable(){
        //table name

        count++;

        String tmpStr = commandWord.get(count).substring(0,7);
        if (tmpStr.equalsIgnoreCase("VALUES(")){
            sqlValueList inputValue = new sqlValueList(commandWord,count);
            count = inputValue.count;
        }
    }

}

package com.company;

import java.util.List;

public class sqlAlter {

    List<String> commandWord;
    int count;  //计数ArrayList到第几个单词

    public sqlAlter(List<String> commandWord, int count) {
        this.commandWord = commandWord;
        this.count = count;

        if (commandWord.get(count).equalsIgnoreCase("TABLE")){
            count++;
            alterTableName();
        }else{
            //报错
        }
    }

    public void alterTableName(){
        //text
        count++;
        alterationType();
    }

    public void alterationType(){
        //???
        count++;
        attributeName();
    }

    public void attributeName(){
        //text

        count++;
    }

}

package com.company;

import java.util.List;

public class sqlCreate {

    List<String> commandWord;
    int count;  //计数ArrayList到第几个单词

    public sqlCreate(List<String> commandWord, int count) {
        this.commandWord = commandWord;
        this.count = count;

        if(commandWord.get(count).equalsIgnoreCase("DATABASE")){
            count++;
            createDatabase();
        }else if(commandWord.get(count).equalsIgnoreCase("TABLE")){
            count++;
            createTable();
        }
    }

    public void createDatabase(){
        count++;
    }

    public void createTable(){
        //name
        count++;

        //是否有AttributeList?
        String flag = commandWord.get(count).substring(0);
        if (flag.equals("(")){
            sqlAttributeList inputAttributeList = new sqlAttributeList(commandWord,count);
            count = inputAttributeList.count;
        }
    }

}

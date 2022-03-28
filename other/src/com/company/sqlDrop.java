package com.company;

import java.util.List;

public class sqlDrop {

    List<String> commandWord;
    int count;  //计数ArrayList到第几个单词

    public sqlDrop(List<String> commandWord, int count) {
        this.commandWord = commandWord;
        this.count = count;

        if (commandWord.get(count).equalsIgnoreCase("DATABASE")){
            count++;
            dropDatabase();
        }else if (commandWord.get(count).equalsIgnoreCase("TABLE")){
            count++;
            dropTable();
        }

        count++;
    }

    public void dropDatabase(){
        //drop name
    }

    public void dropTable(){
        //drop name
    }

}

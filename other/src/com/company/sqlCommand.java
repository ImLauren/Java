package com.company;

import java.util.List;

public class sqlCommand {

    List<String> commandWord;
    int count;  //计数ArrayList到第几个单词

    public sqlCommand(List<String> commandWord, int count) {
        this.commandWord = commandWord;
        this.count = count;
        Command(commandWord,count);
    }

    public void Command(List<String> commandWord, int count){
        //如果为；则结束
        if ((commandWord.get(count).equals(";")) && count!=0){
            return;
        }

        if (commandWord.get(count).equalsIgnoreCase("Use")){
            count++;
            sqlUse inputUse = new sqlUse(commandWord,count);
            count = inputUse.count;
        }else if(commandWord.get(count).equalsIgnoreCase("Create")){
            count++;
            sqlCreate inputCreate = new sqlCreate(commandWord,count);
            count = inputCreate.count;
        }else if(commandWord.get(count).equalsIgnoreCase("Drop")){
            count++;
            sqlDrop inputDrop = new sqlDrop(commandWord,count);
            count = inputDrop.count;
        }else if(commandWord.get(count).equalsIgnoreCase("Alter")){
            count++;
            sqlAlter inputAlter = new sqlAlter(commandWord,count);
            count = inputAlter.count;
        }else if(commandWord.get(count).equalsIgnoreCase("Insert")){
            count++;
            sqlInster inputInsert = new sqlInster(commandWord,count);
            count = inputInsert.count;
        }else if(commandWord.get(count).equalsIgnoreCase("Select")){
            count++;
            sqlSelect inputSelect = new sqlSelect(commandWord,count);
            count = inputSelect.count;
        }else if(commandWord.get(count).equalsIgnoreCase("Update")){
            sqlUpdate inputUpdate = new sqlUpdate();
        }else if(commandWord.get(count).equalsIgnoreCase("Delete")){
            sqlDelete inputDelete = new sqlDelete();
        }else if(commandWord.get(count).equalsIgnoreCase("Join")){
            sqlJoin inputJoin = new sqlJoin();
        }else{
            //报错
        }

        Command(commandWord,count);

    }
}

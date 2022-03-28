package com.company;

import java.util.List;

public class sqlCondition {
    List<String> commandWord;
    int count;  //计数ArrayList到第几个单词

    public sqlCondition(List<String> commandWord, int count) {
        this.commandWord = commandWord;
        this.count = count;

    }

}

package com.company;

import java.util.List;

public class sqlValueList {
    List<String> commandWord;
    int count;  //计数ArrayList到第几个单词

    public sqlValueList(List<String> commandWord, int count) {
        this.commandWord = commandWord;
        this.count = count;

        //不在最后count++
    }
}

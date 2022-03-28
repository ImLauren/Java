package com.company;

import java.util.List;

public class sqlUse {

    List<String> commandWord;
    int count;  //计数ArrayList到第几个单词

    public sqlUse(List<String> commandWord, int count) {
        this.commandWord = commandWord;
        this.count = count;

        //database的use???  写一个函数判断“”内的text
        //如果name里面有空格 --> 操作 --> count++

        count++;
    }


}

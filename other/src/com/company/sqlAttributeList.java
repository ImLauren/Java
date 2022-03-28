package com.company;

import java.util.List;

public class sqlAttributeList {

    List<String> commandWord;
    int count;  //计数ArrayList到第几个单词

    public sqlAttributeList(List<String> commandWord, int count) {
        this.commandWord = commandWord;
        this.count = count;

        int lenStr = commandWord.get(count).length();
        if (lenStr == 1){
            count++;
        }

        attributeName();

        //此时指向最后一个AttributeList/attributeName
        //如果最后一个单词有） 则直接count++
        //如果最后一个单词没有） 则count++后还要count++
        count++;

    }

    public void attributeName(){
        String attriNames = commandWord.get(count);
        //判断开头是不是(
        //截取字符串看中间是否有, 有,继续调用attributeName()
        //判断最后一个指向的是否带有) 不能在这里count++

    }
}

package com.company;

import java.util.List;

public class sqlToken {

    String command;
    List<String> commandValue;

    public sqlToken(String command, List<String> commandValue) {
        this.command = command;
        this.commandValue = commandValue;

    }

    public String splitSpace() throws sqlException {
        StringBuilder handledCmd = new StringBuilder(command);
        for (int i = 0; i < handledCmd.length(); i++) {
            char tmp = handledCmd.charAt(i);
            String tmpStr = "  ";
            if ((i+2)<handledCmd.length()){
                tmpStr = handledCmd.substring(i,i+2);
            }
            //跳过一对单引号
            if (tmp=='\''){
                StringBuilder cutCommand = new StringBuilder(handledCmd);
                cutCommand.substring(i+1);
                int indexQuot = cutCommand.indexOf("\'");
                if (indexQuot==-1){   //没有配对的单引号报错
                    throw new sqlException("Expecting a \'");
                }
                i = indexQuot+i+1;
            }
            //one char special word (except =,>,<)
            if (tmp == '(' || tmp == ')' || tmp == ',' || tmp == ';') {
                handledCmd.insert(i + 1, " ");
                handledCmd.insert(i, " ");
                i = i + 2;
            }
            //two chars special word: !=
            if (tmpStr.equals("!=")){
                handledCmd.insert(i + 2, " ");
                handledCmd.insert(i, " ");
                i = i + 3;
            }
            //special word: =, >, <, ==, >=, <=
            if (tmp == '=' || tmp == '>' ||tmp == '<') {
                if (tmpStr.equals("==") || tmpStr.equals(">=") || tmpStr.equals("<=")){
                    handledCmd.insert(i + 2, " ");
                    handledCmd.insert(i, " ");
                    i = i + 3;
                }else{
                    handledCmd.insert(i + 1, " ");
                    handledCmd.insert(i, " ");
                    i = i + 2;
                }
            }
        }
        String cmd = handledCmd.toString();
        return cmd;
    }

    public void addCmdList(String handledCmd) throws sqlException{
        String cutCmd = handledCmd;
        int indexQuot = handledCmd.indexOf("\'");
        if (indexQuot==-1){
            String tmpStr[] = cutCmd.split(" ");  //用空格将一行字符串分开
            for (int i=0; i<tmpStr.length;i++){    //将数组中的值传入动态数组
                commandValue.add(i,tmpStr[i]);
            }
        }else{
            String leftQuot = handledCmd.substring(0,indexQuot);  //单引号左边的字符串（不包括单引号）
            String tmpStr[] = leftQuot.split(" ");  //将单引号左边字符串放入list
            for (int i=0; i<tmpStr.length;i++){
                commandValue.add(i,tmpStr[i]);
            }
            String rightQuot = handledCmd.substring(indexQuot+1); //先将最近的匹配单引号找出来
            int nextQuot = rightQuot.indexOf("\'");
            nextQuot = indexQuot+nextQuot+1;
            String textQuot;
            if (nextQuot+2<handledCmd.length()){
                textQuot = handledCmd.substring(indexQuot,nextQuot+2);  //单引号字符串（带单引号）
                addCmdList(rightQuot);
                rightQuot = handledCmd.substring(nextQuot+2); //单引号右边字符串（不带单引号）
            }else{
                textQuot = handledCmd.substring(indexQuot);
                commandValue.add(textQuot);  //将单引号内容放入字符串
            }

        }

    }

}

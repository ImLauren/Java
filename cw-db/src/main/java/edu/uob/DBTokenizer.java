package edu.uob;

import java.util.List;

public class DBTokenizer {

    String command;
    List<String> commandValue;

    public DBTokenizer(String command, List<String> commandValue) {
        this.command = command;
        this.commandValue = commandValue;

    }

    public String splitSpace() throws DBException {
        StringBuilder handledCmd = new StringBuilder(command);
        int len = command.length();
        //Check whether there is the final seal number
        checkSemi(len);
        for (int i = 0; i < handledCmd.length(); i++) {
            char tmp = handledCmd.charAt(i);
            String tmpStr = "  ";
            if ((i+2)<handledCmd.length()){
                tmpStr = handledCmd.substring(i,i+2);
            }
            //Skip a pair of single quotes
            i = skipQuot(tmp,handledCmd,i);
            //one char special word (except =,>,<)
            if (oneSpecChar(tmp)) {
                checkBracket(tmp,handledCmd,i);
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
            if (twoSpecCharFirst(tmp)) {
                if (twoSpecChar(tmpStr)){
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

    public void addCmdList(String handledCmd) throws DBException {
        String cutCmd = handledCmd;
        int indexQuot = handledCmd.indexOf("\'");
        if (indexQuot==-1){
            String tmpStr[] = cutCmd.split("\\s+");  //Separate a line of strings with spaces
            for (int i=0; i<tmpStr.length;i++){    //Pass the values in the array into the arraylist
                if (!tmpStr[i].equals("")){
                    commandValue.add(tmpStr[i]);
                }
            }
        }else{
            //String to the left of single quotation (excluding single quotation)
            String leftQuot = handledCmd.substring(0,indexQuot);
            //Put the string to the left of the single quotation into the list
            String tmpStr[] = leftQuot.split("\\s+");
            for (int i=0; i<tmpStr.length;i++){
                if (!tmpStr[i].equals("")){
                    commandValue.add(tmpStr[i]);
                }
            }
            String rightQuot = handledCmd.substring(indexQuot+1); //先将最近的匹配单引号找出来
            int nextQuot = rightQuot.indexOf("\'");
            nextQuot = indexQuot+nextQuot+1;
            String textQuot;
            if (nextQuot+2<handledCmd.length()){
                //Find the nearest matching single quotation
                textQuot = handledCmd.substring(indexQuot,nextQuot+1);
                commandValue.add(textQuot);   //Put the contents of single quotation marks into a string
                //String to the right of single quotation (without single quotation)
                rightQuot = handledCmd.substring(nextQuot+1);
                addCmdList(rightQuot);
            }else{
                textQuot = handledCmd.substring(indexQuot);
                commandValue.add(textQuot);  //Put the contents of single quotation marks into a string
            }

        }

    }

    public void checkSemi(int len) throws DBException{
        if (command.charAt(len-1) != ';'){
            throw new DBException("[ERROR] Expecting a ;");
        }
    }

    public int skipQuot(char tmp, StringBuilder handledCmd,int pos) throws DBException{
        if (tmp=='\''){
            String rightQuot = handledCmd.substring(pos+1);
            int indexQuot = rightQuot.indexOf("\'");
            if (indexQuot==-1){   //There is no paired single quotation --> error
                throw new DBException("[ERROR] Expecting a '");
            }
            pos = indexQuot+pos+1;
        }
        return pos;
    }

    public void checkBracket(char tmp, StringBuilder handledCmd, int pos) throws DBException{
        if (tmp == '('){
            String rightBracket = handledCmd.substring(pos+1);
            int nextBracket = rightBracket.indexOf(")");
            if (nextBracket==-1){
                throw new DBException("[ERROR] Expecting a )");
            }
        }
    }

    public boolean oneSpecChar(char tmp){
        return tmp == '(' || tmp == ')' || tmp == ',' || tmp == ';';
    }

    public boolean twoSpecCharFirst(char tmp){
        return tmp == '=' || tmp == '>' || tmp == '<';
    }

    public boolean twoSpecChar(String tmpStr){
        return tmpStr.equals("==") || tmpStr.equals(">=") || tmpStr.equals("<=");
    }

}

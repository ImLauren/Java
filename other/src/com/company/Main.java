package com.company;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, DBException {
        List<String> commandWord = new ArrayList<>(){{
            add("(");
            add("(");
            add("id");
            add("==");
            add("1");
            add(")");
            add("and");
            add("(");
            add("name");
            add("==");
            add("jack");
            add(")");
            add(")");
            add("or");
            add("(");
            add("gender");
            add("==");
            add("male");
            add(")");
            add(";");
        }};
        int pointer=0;
        sqlRePolish inputPolish = new sqlRePolish(commandWord,pointer);
        pointer = inputPolish.setRePolish(commandWord,pointer);
        System.out.println(commandWord);
        for (int i=0; i<inputPolish.conditions.size();i++){
            System.out.println(inputPolish.conditions.get(i).AttriName+" "+inputPolish.conditions.get(i).Operator+
                    " "+inputPolish.conditions.get(i).Value+" "+inputPolish.conditions.get(i).logicOp);
        }
        System.out.println(commandWord.get(pointer));
        System.out.println(commandWord.size());
        System.out.println(commandWord.get(19));
        int count = 0;
        while (count<commandWord.size() && !commandWord.get(count).equalsIgnoreCase("WHERE")){
            count++;
        }
        System.out.println("count= "+count);
    }


}

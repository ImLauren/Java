package edu.uob;

import java.util.LinkedList;

public class sqlStack {

    LinkedList<String> operaList;
    LinkedList<Condition> condList;

    public sqlStack() {
        operaList = new LinkedList<>();
        condList = new LinkedList<>();
    }

    public void push(String t) {
        operaList.addFirst(t);
    }

    public void pushCond(Condition t) {
        condList.addFirst(t);
    }

    public String pop() {
        return operaList.removeFirst();
    }

    public Condition popCond() {
        return condList.removeFirst();
    }

    //Stack top element
    public String peek() {
        String t = null;
        if (!operaList.isEmpty())
            t = operaList.getFirst();
        return t;
    }

    public Condition peekCond() {
        Condition cond = new Condition();
        if (!condList.isEmpty())
            cond = condList.getFirst();
        return cond;
    }

    public boolean isEmpty() {
        return operaList.isEmpty();
    }

    public boolean isEmptyCond() {
        return condList.isEmpty();
    }

}

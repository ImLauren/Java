package com.company;

import java.util.LinkedList;

public class sqlStack {

    LinkedList<String> operaList;

    public sqlStack() {
        operaList = new LinkedList<>();
    }

    //入栈
    public void push(String t) {
        operaList.addFirst(t);
    }

    //出栈
    public String pop() {
        return operaList.removeFirst();
    }

    //栈顶元素
    public String peek() {
        String t = null;
        //直接取元素会报异常，需要先判断是否为空
        if (!operaList.isEmpty())
            t = operaList.getFirst();
        return t;
    }

    //栈空
    public boolean isEmpty() {
        return operaList.isEmpty();
    }

}

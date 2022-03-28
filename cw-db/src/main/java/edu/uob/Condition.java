package edu.uob;

import java.util.List;

public class Condition {
    String AttriName;
    String Operator;
    String Value;
    String logicOp;   //AND or OR
    List<Integer> rowNumber;

    public Condition() {}

    public void setRowNumber(List<Integer> rowNumber) {
        this.rowNumber = rowNumber;
    }

    public List<Integer> getRowNumber() {
        return rowNumber;
    }

}

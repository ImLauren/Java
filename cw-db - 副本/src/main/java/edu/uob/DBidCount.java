package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class DBidCount {
    String DBName;
    List<String> tableNames;
    List<Integer> ids;

    public DBidCount() {
        tableNames = new ArrayList<>();
        ids = new ArrayList<>();
    }

}

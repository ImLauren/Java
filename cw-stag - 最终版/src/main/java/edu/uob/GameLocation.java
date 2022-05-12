package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;

public class GameLocation extends GameEntity{

    HashMap<String,GameEntity> allAttr;    //save all attributes in  a location
    ArrayList<String> paths;   //save all locations one location linked to

    public GameLocation(String name, String description) {
        super(name, description);
        allAttr = new HashMap<>();
        paths = new ArrayList<>();
    }

    public HashMap<String,GameEntity> getAllAttr() {
        return allAttr;
    }

    public ArrayList<String> getPaths() { return paths; }

}

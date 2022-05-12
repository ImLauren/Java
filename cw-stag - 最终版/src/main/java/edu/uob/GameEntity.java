package edu.uob;

import com.alexmerz.graphviz.Parser;

public abstract class GameEntity
{
    String name;
    String description;
    String type;  //Locations, Artefacts, Furniture, Characters, Players

    public GameEntity(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getType(){ return type; }

    public void setType(String type) {
        this.type = type;
    }

}

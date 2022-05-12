package edu.uob;

import java.util.ArrayList;

public class GamePlayers extends GameEntity{

    ArrayList<GameEntity> inventory;      //things carried by the player
    int healthLevel;
    int startLocationPos;    //start location in allLocations
    int currentLocPos;             //player position in allLocation
    int maxHealthLevel = 3;

    public GamePlayers(String name, String description, int startLocationPos) {
        super(name, description);
        inventory = new ArrayList<>();
        healthLevel = maxHealthLevel;    //max health level
        currentLocPos = startLocationPos;
    }

    public int getHealthLevel() { return healthLevel; }

    public void increaseHealth(){ healthLevel++; }

    public boolean isHealthMax(){ return healthLevel == maxHealthLevel; }

    public void loseHealth(){ healthLevel--; }

    public boolean isHealthZero(){ return healthLevel == 0; }

    public void reSetHealth(){ healthLevel = maxHealthLevel; }

    public ArrayList<GameEntity> getInventory() { return inventory; }

    public int getCurrentLocPos() { return currentLocPos; }

    public void setCurrentLocPos(int currentLocPos) { this.currentLocPos = currentLocPos; }

    public void resetCurrentLocPos (){ currentLocPos = startLocationPos; }

    public void clearAllInv(){ inventory.clear(); }

}

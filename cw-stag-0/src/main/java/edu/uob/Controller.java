package edu.uob;

import java.util.ArrayList;

public class Controller {

    ArrayList<GamePlayers> allPlayers;     //all player in the game
    String nowPlayer;
    int nowPlayerPos;
    ArrayList<GameLocation> allLocations;   //save all locations and their attributes

    public Controller(ArrayList<GamePlayers> allPlayers, String nowPlayer, int nowPlayerPos,
                      ArrayList<GameLocation> allLocations) {
        this.allPlayers = allPlayers;
        this.nowPlayer = nowPlayer;
        this.nowPlayerPos = nowPlayerPos;
        this.allLocations = allLocations;
    }

    public ArrayList<GamePlayers> existPlayer(int startLocationPos){
        boolean playerExist = false;      //whether existed
        for (GamePlayers Player : allPlayers) {
            if (nowPlayer.equals(Player.getName())) {
                playerExist = true;
                break;
            }
        }
        if (!playerExist){
            GamePlayers gamePlayer = new GamePlayers(nowPlayer,"player",startLocationPos);
            allPlayers.add(gamePlayer);
        }
        return allPlayers;
    }

    public int findPlayerPos(){
        for (int i=0; i<allPlayers.size();i++){
            if (nowPlayer.equals(allPlayers.get(i).getName())){
                nowPlayerPos = i;
            }
        }
        return nowPlayerPos;
    }

    public int findLocation(String location){
        int LocationPos = 0;
        for (int i=0; i<allLocations.size();i++){
            String locationName = allLocations.get(i).getName();
            if (locationName.equals(location)){
                LocationPos = i;
                break;
            }
        }
        return LocationPos;
    }

    public GameEntity findArtExist(String artefact){    //in current location
        int currentPosition = allPlayers.get(nowPlayerPos).getCurrentLocPos();
        return allLocations.get(currentPosition).getAllAttr().get(artefact);
    }




}

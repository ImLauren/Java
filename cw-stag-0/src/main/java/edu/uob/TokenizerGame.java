package edu.uob;

import java.util.ArrayList;
import java.util.Collections;

public class TokenizerGame {

    String command;          // initial command
    ArrayList<String> handleCmd;      //split command into words and save as array
    String playerName;      //player in one command

    public String getPlayerName() { return playerName; }

    public ArrayList<String> getHandleCmd() { return handleCmd; }

    public TokenizerGame(String command) throws GameException{
        this.command = command;
        handleCmd = new ArrayList<>();
        splitCmd();
    }

    public void splitCmd() throws GameException{
        int indexColon = command.indexOf(":");
        String tmpName = command.substring(0,indexColon);
        boolean nameValid = isNameValid(tmpName);
        if (!nameValid){
            throw new GameException("[ERROR] Invalid Player Name!");
        }
        playerName = tmpName;

        String pureCmd = command.substring(indexColon+1);
        pureCmd = pureCmd.trim();
        String[] tmpCmd = pureCmd.split("\\s+");
        Collections.addAll(handleCmd, tmpCmd);
    }

    public boolean isNameValid(String name){
        boolean isValid = name.matches("[a-zA-Z '-]+");
        return isValid;
    }



}

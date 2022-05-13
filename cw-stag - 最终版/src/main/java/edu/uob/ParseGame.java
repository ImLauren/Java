package edu.uob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.TreeMap;

public class ParseGame {

    ArrayList<GameLocation> allLocations;   //save all locations and their attributes
    TreeMap<String, HashSet<GameAction>> allActions;   //save all actions in the file
    ArrayList<String> handleCmd;      //split command into words and save as array
    ArrayList<GamePlayers> allPlayers;     //all player in the game
    String nowPlayer;
    int nowPlayerPos;         //player position in allPlayers
    String startLocation;
    int startLocationPos;    //start location in allLocations
    String result;
    int cmdPos;             //position of handleCmd  --> which word are read

    public ParseGame(ArrayList<GameLocation> allLocations, String startLocation,
                     TreeMap<String, HashSet<GameAction>> allActions, ArrayList<String> handleCmd,
                     String nowPlayer, ArrayList<GamePlayers> allPlayers) {
        this.allLocations = allLocations;
        this.startLocation = startLocation;
        this.allActions = allActions;
        this.handleCmd = handleCmd;
        this.nowPlayer = nowPlayer;
        this.allPlayers = allPlayers;
        Controller controller = new Controller(allPlayers,nowPlayer,nowPlayerPos,allLocations);
        startLocationPos = controller.findLocation(startLocation);
        cmdPos = 0;
    }

    public String text(GameServer gameServer){ return result; }

    public String getResult() { return result; }

    public ArrayList<GameLocation> getAllLocations() { return allLocations; }

    public ArrayList<GamePlayers> getAllPlayers() { return allPlayers; }

    public void startGame() throws GameException{
        runHandleCmd();
    }

    public void runHandleCmd() throws GameException{
        if (cmdPos == handleCmd.size()){
            return;
        }
        Controller controller = new Controller(allPlayers,nowPlayer,nowPlayerPos,allLocations);
        allPlayers = controller.existPlayer(startLocationPos);
        nowPlayerPos = controller.findPlayerPos();
        BasicCommand basicCommand = new BasicCommand(allLocations,allPlayers,nowPlayer,nowPlayerPos);
        ActionCommand actionCommand = new ActionCommand(allLocations,allActions,allPlayers,nowPlayerPos,handleCmd);

        if (basicCommand.isBasicCommand(handleCmd.get(cmdPos))){
            runBasicCmd(basicCommand);
        }else if(actionCommand.isActionCmd() != null){
            runActionCmd(actionCommand,actionCommand.isActionCmd());
        }else{
            throw new GameException("[ERROR] Need valid command!");
        }

        runHandleCmd();
    }

    public void runBasicCmd(BasicCommand basicCommand) throws GameException{
        String word = handleCmd.get(cmdPos);
        if (word.equalsIgnoreCase("inventory")||word.equalsIgnoreCase("inv")){
            runBasicInv(basicCommand);
        }else if(word.equalsIgnoreCase("get")){
            runBasicGet(basicCommand);
        }else if(word.equalsIgnoreCase("drop")){
            runBasicDrop(basicCommand);
        }else if(word.equalsIgnoreCase("goto")){
            runBasicGoto(basicCommand);
        }else if(word.equalsIgnoreCase("look")){
            runBasicLook(basicCommand);
        }else if(word.equalsIgnoreCase("health")){
            runBasicHealth(basicCommand);
        }
    }

    public void runBasicInv(BasicCommand basicCommand) throws GameException{
        cmdPos = handleCmd.size();
        result = basicCommand.runInvCmd();
    }

    public void runBasicGet(BasicCommand basicCommand) throws GameException {
        String getArtefact = isTwoWordsCmd();
        result = basicCommand.runGetCmd(getArtefact);
        allPlayers = basicCommand.getAllPlayers();
        allLocations = basicCommand.getAllLocations();
    }

    public void runBasicDrop(BasicCommand basicCommand) throws GameException{
        String artefact = isTwoWordsCmd();
        result = basicCommand.runDropCmd(artefact);
        allPlayers = basicCommand.getAllPlayers();
        allLocations = basicCommand.getAllLocations();
    }

    public String isTwoWordsCmd() throws GameException{
        cmdPos++;
        if (cmdPos == handleCmd.size()){
            throw new GameException("[ERROR] Command missing!");
        }
        String word = handleCmd.get(cmdPos);
        cmdPos = handleCmd.size();
        return word;
    }

    public void runBasicGoto(BasicCommand basicCommand) throws GameException {
        String toLocation = isTwoWordsCmd();
        result = basicCommand.runGotoCmd(toLocation);
        allPlayers = basicCommand.getAllPlayers();
    }

    public void runBasicLook(BasicCommand basicCommand) throws GameException{
        cmdPos = handleCmd.size();
        result = basicCommand.runLookCmd();
    }

    public void runBasicHealth(BasicCommand basicCommand) throws GameException{
        cmdPos=handleCmd.size();
        result = basicCommand.runHealthCmd();
    }

    public void runActionCmd(ActionCommand actionCommand, String actionName) throws GameException{
        checkCmd();
        cmdPos = handleCmd.size();
        int hashsetPos = actionCommand.getHashsetPos(actionName);
        int currentLocPos = allPlayers.get(nowPlayerPos).getCurrentLocPos();
        if (hashsetPos == -1){
            result = "Subjects cannot be triggered!";
            return;
        }

        boolean containAllSub = actionCommand.isContainAllSub(actionName,hashsetPos);
        if (!containAllSub){
            result = "Lost subject!";
            return;
        }

        boolean subAvailable = actionCommand.isSubAvailable(actionName,hashsetPos);
        if (!subAvailable){
            result = "Does not have subjects in current location <"+allLocations.get(currentLocPos).getName()+
                    "> or your inventory !";
            return;
        }
        result = actionCommand.runAction(actionName,hashsetPos);
        allLocations = actionCommand.getAllLocations();
        allPlayers = actionCommand.getAllPlayers();
    }

    public void checkCmd() throws GameException{
        if (handleCmd.size()<2){
            throw new GameException("[ERROR] Invalid command!");
        }
    }

}

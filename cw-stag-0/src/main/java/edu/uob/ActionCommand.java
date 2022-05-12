package edu.uob;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

public class ActionCommand {

    ArrayList<GameLocation> allLocations;   //save all locations and their attributes
    TreeMap<String, HashSet<GameAction>> allActions;   //save all actions in the file
    ArrayList<GamePlayers> allPlayers;     //all player in the game
    int nowPlayerPos;     //player position in allPlayers
    ArrayList<String> handleCmd;
    String diedText;
    boolean isDied;

    public ArrayList<GameLocation> getAllLocations() { return allLocations; }

    public ArrayList<GamePlayers> getAllPlayers() { return allPlayers; }

    public ActionCommand(ArrayList<GameLocation> allLocations, TreeMap<String, HashSet<GameAction>> allActions,
                         ArrayList<GamePlayers> allPlayers, int nowPlayerPos, ArrayList<String> handleCmd) {
        this.allLocations = allLocations;
        this.allActions = allActions;
        this.allPlayers = allPlayers;
        this.nowPlayerPos = nowPlayerPos;
        this.handleCmd = handleCmd;
        isDied = false;
    }

    public String isActionCmd(){
        for (String actionName : allActions.keySet()){
            for (String word : handleCmd){
                if (word.equalsIgnoreCase(actionName)){
                    return actionName;
                }
            }
        }
        return null;
    }

    public int getHashsetPos(String actionName){
        //Check whether the command is in the HashSet.
        // When returning the HashSet, return the position of the action corresponding to the HashSet
        HashSet<GameAction> actions = allActions.get(actionName);
        ArrayList<GameAction> actionsList = new ArrayList<>(actions);//B是set型的
        for (int i =0; i<actionsList.size();i++){
            for (String subName : actionsList.get(i).subjects){
                for (String word : handleCmd){
                    if (word.equalsIgnoreCase(subName)){
                        return i;
                    }
                }
            }
        }
        return -1;     //-1 -> not find
    }

    public boolean isContainAllSub(String actionName, int hashsetPos){
        HashSet<GameAction> actions = allActions.get(actionName);
        ArrayList<GameAction> actionsList = new ArrayList<>(actions);
        HashSet<String> subjects = actionsList.get(hashsetPos).getSubjects();
        ArrayList<String> markSub = new ArrayList<>(subjects);

        for (int i=0; i<markSub.size();i++){
            for (String word : handleCmd){
                if (word.equalsIgnoreCase(markSub.get(i))) {
                    markSub.set(i,"1");
                }
            }
        }
        for (String subject : markSub){
            if (!subject.equals("1")){
                return false;
            }
        }
        return true;
    }

    public boolean isSubAvailable(String actionName, int hashsetPos){
        HashSet<GameAction> actions = allActions.get(actionName);
        ArrayList<GameAction> actionsList = new ArrayList<>(actions);
        HashSet<String> subjects = actionsList.get(hashsetPos).getSubjects();
        for (String subject : subjects){
            boolean subInCurPos = isSubInCurPos(subject);
            boolean subInInv = isSubInInv(subject);
            if (!subInCurPos && !subInInv){
                return false;
            }
        }
        return true;
    }

    public boolean isSubInCurPos(String subject){
        int currentLocPos = allPlayers.get(nowPlayerPos).getCurrentLocPos();
        for (String attrName : allLocations.get(currentLocPos).getAllAttr().keySet()){
            if (subject.equalsIgnoreCase(attrName)){
                return true;
            }
        }
        return false;
    }

    public boolean isSubInInv(String subject){
        ArrayList<GameEntity> invSubjects = allPlayers.get(nowPlayerPos).getInventory();

        for (GameEntity invName : invSubjects){
            if (subject.equalsIgnoreCase(invName.getName())){
                return true;
            }
        }
        return false;
    }

    public String runAction(String actionName, int hashsetPos) throws GameException{
        String actionText;
        runConsumedCmd(actionName,hashsetPos);
        runProducedCmd(actionName,hashsetPos);
        HashSet<GameAction> actions = allActions.get(actionName);
        ArrayList<GameAction> actionsList = new ArrayList<>(actions);
        actionText = actionsList.get(hashsetPos).getNarration()+"\n";
        if (isDied){
            actionText = actionText + diedText;
        }
        return actionText;
    }

    public void runConsumedCmd(String actionName, int hashsetPos) throws GameException{
        HashSet<GameAction> actions = allActions.get(actionName);
        ArrayList<GameAction> actionsList = new ArrayList<>(actions);
        HashSet<String> consumed = actionsList.get(hashsetPos).getConsumed();
        for (String conName : consumed){
            if (conName.equalsIgnoreCase("health")){
                consumedHealth();
            }else{
                consumedEntity(conName);
            }
        }

    }

    public void consumedHealth(){
        allPlayers.get(nowPlayerPos).loseHealth();  //health--
        if (allPlayers.get(nowPlayerPos).isHealthZero()){
            //Put all items in the current location
            int currentLocPos = allPlayers.get(nowPlayerPos).getCurrentLocPos();
            for (GameEntity entity : allPlayers.get(nowPlayerPos).getInventory()){
                allLocations.get(currentLocPos).allAttr.put(entity.getName(),entity);
            }
            allPlayers.get(nowPlayerPos).clearAllInv();   //Empty inventory
            allPlayers.get(nowPlayerPos).reSetHealth();   //Reset health
            allPlayers.get(nowPlayerPos).resetCurrentLocPos();  //Reset place - start place
            isDied = true;
            diedText = "You died and lost all of your items, you must return to the start of the game\n";
        }
    }

    public void consumedEntity(String conName) throws GameException{    //put consumed Entity to Storeroom
        int conInvPos = isConsumedInInv(conName);
        int conLocPos = isConsumedInLoc(conName);
        if (conInvPos != -1){    //In the player's inv
            GameEntity conEntity = allPlayers.get(nowPlayerPos).getInventory().get(conInvPos);
            allPlayers.get(nowPlayerPos).getInventory().remove(conInvPos); //Remove from player's inv
            int storePos = findStoreroom();
            allLocations.get(storePos).getAllAttr().put(conEntity.getName(),conEntity);  //put in storeroom
        }else if(conLocPos != -1){    //inv location
            GameEntity conEntity = allLocations.get(conLocPos).getAllAttr().get(conName);
            allLocations.get(conLocPos).getAllAttr().remove(conName);  //delete from location
            int storePos = findStoreroom();
            allLocations.get(storePos).getAllAttr().put(conEntity.getName(),conEntity);  //put in storeroom
        }else{
            throw new GameException("[ERROR] Invalid subject!");
        }
    }

    public int isConsumedInInv(String conName){
        if (allPlayers.get(nowPlayerPos).getInventory().size()==0){
            return -1;
        }
        for (int i=0; i<allPlayers.get(nowPlayerPos).getInventory().size();i++){
            if (allPlayers.get(nowPlayerPos).getInventory().get(i).getName().equalsIgnoreCase(conName)){
                return i;
            }
        }
        return -1;
    }

    public int isConsumedInLoc(String conName){
        //in locations, it can be consumed
        for (int i=0; i<allLocations.size();i++){
            for (String attrName : allLocations.get(i).getAllAttr().keySet()){
                if (attrName.equalsIgnoreCase(conName)){
                    return i;
                }
            }
        }
        return -1;
    }

    public void runProducedCmd(String actionName, int hashsetPos){
        HashSet<GameAction> actions = allActions.get(actionName);
        ArrayList<GameAction> actionsList = new ArrayList<>(actions);
        HashSet<String> produced = actionsList.get(hashsetPos).getProduced();
        for (String produce : produced){
            if (isProLocation(produce)){
                producedPath(produce);
            }else if(produce.equalsIgnoreCase("health")){
                produceHealth();
            }else{
                produceEntity(produce);
            }
        }
    }

    public boolean isProLocation(String produce){
        //See if the produced is location
        for (GameLocation location : allLocations){
            if (location.getName().equalsIgnoreCase(produce)){
                return true;
            }
        }
        return false;
    }

    public void producedPath(String produce){
        int currentLocPos = allPlayers.get(nowPlayerPos).getCurrentLocPos();
        allLocations.get(currentLocPos).getPaths().add(produce);   //Add the location to the path
    }

    public void produceHealth(){
        if (!allPlayers.get(nowPlayerPos).isHealthMax()){
            allPlayers.get(nowPlayerPos).increaseHealth();  //less than max -> health++
        }
    }

    public void produceEntity(String produce){
        int currentLocPos = allPlayers.get(nowPlayerPos).getCurrentLocPos();
        int storePos = findStoreroom();
        GameEntity proEntity = allLocations.get(storePos).getAllAttr().get(produce);
        allLocations.get(storePos).getAllAttr().remove(produce);    //delete ftom storeroom
        allLocations.get(currentLocPos).getAllAttr().put(proEntity.getName(),proEntity);    //Join to current location
    }

    public int findStoreroom(){
        int pos=0;
        for (int i=0; i<allLocations.size();i++){
            if (allLocations.get(i).getName().equalsIgnoreCase("storeroom")){
                pos =  i;
            }
        }
        return pos;
    }



}

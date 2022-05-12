package edu.uob;

import java.util.ArrayList;

public class BasicCommand {

    ArrayList<GameLocation> allLocations;   //save all locations and their attributes
    //int currentLocPos;      //player Location
    ArrayList<GamePlayers> allPlayers;     //all player in the game
    String nowPlayer;
    int nowPlayerPos;     //player position in allPlayers

    public ArrayList<GamePlayers> getAllPlayers() { return allPlayers; }

    public ArrayList<GameLocation> getAllLocations() { return allLocations; }

    public BasicCommand(ArrayList<GameLocation> allLocations, ArrayList<GamePlayers> allPlayers,
                        String nowPlayer, int nowPlayerPos) {
        this.allLocations = allLocations;
        this.allPlayers = allPlayers;
        this.nowPlayer = nowPlayer;
        this.nowPlayerPos = nowPlayerPos;
    }

    public boolean isBasicCommand(String cmd){
        return cmd.equalsIgnoreCase("inventory") || cmd.equalsIgnoreCase("inv") ||
                cmd.equalsIgnoreCase("get") || cmd.equalsIgnoreCase("drop") ||
                cmd.equalsIgnoreCase("goto") || cmd.equalsIgnoreCase("look") ||
                cmd.equalsIgnoreCase("health");
    }

    public String runInvCmd(){
        //lists all of the artefacts currently being carried by the player
        String invText = "All artefacts you got as following:\n";
        boolean isEmpty = allPlayers.get(nowPlayerPos).inventory.size() == 0;
        if (isEmpty){
            invText = invText + "\tYour inventory is empty.\n";
        }else{
            for (GameEntity artefact : allPlayers.get(nowPlayerPos).getInventory()){
                invText = invText + "\t" + artefact.getName() + " : " + artefact.getDescription() + "\n";
            }
        }
        return invText;
    }

    public String runGetCmd(String artefact){
        String getText;
        int currentLocPos = allPlayers.get(nowPlayerPos).getCurrentLocPos();
        boolean attrExist = allLocations.get(currentLocPos).getAllAttr().containsKey(artefact);
        if (!attrExist){     //Judge whether there is this item in the current position
            getText = "Current location <"+ allLocations.get(currentLocPos).getName() +
                    "> doesn't has" +artefact+"!";
            return getText;
        }
        boolean canGet = canGetArtefact(allPlayers.get(nowPlayerPos),artefact);    //whether object can get
        if(canGet){
            Controller controller = new Controller(allPlayers,nowPlayer,nowPlayerPos,allLocations);
            GameEntity arteEntity = controller.findArtExist(artefact);
            allPlayers.get(nowPlayerPos).inventory.add(arteEntity);
            //delete from location
            allLocations.get(currentLocPos).getAllAttr().remove(artefact);
            getText = "You get "+artefact+" successfully.";
        }else{
            getText = "You cannot get "+artefact+".";
        }
        return getText;
    }

    public boolean canGetArtefact(GamePlayers player, String artefact){
        boolean canGet;
        int currentLocPos = player.getCurrentLocPos();
        String entityType = allLocations.get(currentLocPos).getAllAttr().get(artefact).getType();
        canGet = entityType.equalsIgnoreCase("Artefacts");
        return canGet;
    }

    public String runDropCmd(String artefact){
        String dropText;
        boolean artefactExist = false;
        for (GameEntity entity : allPlayers.get(nowPlayerPos).getInventory()){
            if (entity.getName().equalsIgnoreCase(artefact)) {
                artefactExist = true;
            }
        }
        if (!artefactExist){   //whether exist
            dropText = "You don't have "+artefact+" in your Inventory!";
            return dropText;
        }
        GameEntity arteEntity;
        int artePos=0;
        for (int i=0; i<allPlayers.get(nowPlayerPos).getInventory().size();i++){
             if (allPlayers.get(nowPlayerPos).getInventory().get(i).getName().equalsIgnoreCase(artefact)){
                 artePos = i;
             }
         }
        arteEntity = allPlayers.get(nowPlayerPos).getInventory().get(artePos);

        allPlayers.get(nowPlayerPos).getInventory().remove(arteEntity);   //delete from inv
        //put in current location
        int currentLocPos = allPlayers.get(nowPlayerPos).getCurrentLocPos();
        allLocations.get(currentLocPos).allAttr.put(artefact,arteEntity);
        dropText = "You successfully drop " +artefact+" in current location <" +
                allLocations.get(currentLocPos).getName() +">.";
        return dropText;
    }

    public String runGotoCmd(String toLocation){
        String gotoText;
        boolean existLoc = isLocExist(toLocation);
        if (!existLoc){   //whether location exist
            gotoText = "Location <"+toLocation+"> does not exist!";
            return gotoText;
        }
        int currentLocPos = allPlayers.get(nowPlayerPos).getCurrentLocPos();
        boolean existLocLink = allLocations.get(currentLocPos).getPaths().contains(toLocation);
        if (!existLocLink){    //whether can goto location
            gotoText = "You cannot goto <"+toLocation+"> from current location <"+
                    allLocations.get(currentLocPos).getName() +">!";
            return gotoText;
        }
        Controller controller = new Controller(allPlayers,nowPlayer,nowPlayerPos,allLocations);
        int toLocPos =controller.findLocation(toLocation);
        allPlayers.get(nowPlayerPos).setCurrentLocPos(toLocPos);
        gotoText = "You are now in location <" +toLocation+">.";
        return gotoText;
    }

    public boolean isLocExist(String toLocation){
        boolean existLoc = false;
        for (GameEntity entity : allLocations){
            if (toLocation.equals(entity.getName())){
                existLoc = true;
                break;
            }
        }
        return existLoc;
    }

    public String runLookCmd(){
        String lookText;
        int currentLocPos = allPlayers.get(nowPlayerPos).getCurrentLocPos();
        String playerText = "Location <" + allLocations.get(currentLocPos).getName()+"> "+
                allLocations.get(currentLocPos).getDescription() + "\n";
        playerText = playerText + "Entity list:\n";
        for (GameEntity entity : allLocations.get(currentLocPos).getAllAttr().values()){
            playerText = playerText + "\t" +entity.getName()+" : "+entity.getDescription() + "\n";
        }
        playerText = playerText + "Path list:\n";
        String currentLocName = allLocations.get(currentLocPos).getName();
        for (String path : allLocations.get(currentLocPos).getPaths()){
            playerText = playerText + "\t"+currentLocName+" ---> "+path+"\n";
        }

        String otherPlayer =getOtherPlayer(allPlayers.get(nowPlayerPos));
        if (otherPlayer==null){
            lookText = playerText;
        }else{
            lookText = playerText + "\n" + otherPlayer;
        }
        return lookText;
    }

    public String getOtherPlayer(GamePlayers player){
        boolean flag = false;
        int currentLocPos = player.getCurrentLocPos();
        String otherPlayer ="Other player in current location <"+ allLocations.get(currentLocPos).getName()+"> :\n";
        for (int i=0; i<allPlayers.size();i++){
            if (i != nowPlayerPos && allPlayers.get(i).getCurrentLocPos()==currentLocPos){
                flag = true;
                otherPlayer = otherPlayer + "\t" + allPlayers.get(i).getName()+"\n";
            }
        }
        if(!flag){ otherPlayer = null; }
        return otherPlayer;
    }

    public String runHealthCmd(){
        return "Your health Level is : " + allPlayers.get(nowPlayerPos).getHealthLevel() + "\n";
    }



}

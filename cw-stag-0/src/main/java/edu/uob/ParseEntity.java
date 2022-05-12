package edu.uob;

import com.alexmerz.graphviz.Parser;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.util.HashMap;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import com.alexmerz.graphviz.objects.Edge;

public class ParseEntity{

    final File entitiesFile;
    ArrayList<GameLocation> allLocations;  //save all locations and their attributes
    String startLocation;

    public ParseEntity(File entitiesFile) throws FileNotFoundException, ParseException {
        this.entitiesFile = entitiesFile;
        allLocations = new ArrayList<>();
        startEntityParse();
    }

    public void startEntityParse() throws FileNotFoundException, ParseException {
        Parser parser = new Parser();
        FileReader reader = new FileReader(entitiesFile);
        parser.parse(reader);
        Graph wholeDocument = parser.getGraphs().get(0);    //whole document
        ArrayList<Graph> sections = wholeDocument.getSubgraphs();    //locations, paths
        parseLocation(sections);
        parsePath(sections);
    }

    public void parseLocation(ArrayList<Graph> sections){
        ArrayList<Graph> locations = sections.get(0).getSubgraphs(); //location --> multiple locations
        startLocation = locations.get(0).getNodes(false).get(0).getId().getId();
        for (int i=0; i<locations.size();i++){
            //location: only one node
            Node locDetails = locations.get(i).getNodes(false).get(0);  //location Details
            String locName = locDetails.getId().getId();
            String locDes = locDetails.getAttribute("description");
            GameLocation newLocation = new GameLocation(locName,locDes);
            newLocation.setType("Locations");
            allLocations.add(newLocation);      //location node -> allLocations
            ArrayList<Graph> items = locations.get(i).getSubgraphs();
            parseItem(items,i);
        }
    }

    public void parseItem(ArrayList<Graph> items, int numLoc){
        for (Graph item : items) {
            ArrayList<Node> nodes = new ArrayList<>();  //save all details in one item
            nodes = item.getNodes(false);
            String attribute = item.getId().getId();  //location type（furniture, etc）
            //put all attributes in allLocal Hashmap
            for (Node node : nodes) {
                String name = node.getId().getId();
                String description = node.getAttribute("description");
                if (attribute.equalsIgnoreCase("artefacts")) {
                    GameArtefacts artefacts = new GameArtefacts(name, description);
                    artefacts.setType("Artefacts");
                    allLocations.get(numLoc).allAttr.put(name, artefacts);
                } else if (attribute.equalsIgnoreCase("furniture")) {
                    GameFurniture furniture = new GameFurniture(name, description);
                    furniture.setType("Furniture");
                    allLocations.get(numLoc).allAttr.put(name, furniture);
                } else if (attribute.equalsIgnoreCase("characters")) {
                    GameCharacters characters = new GameCharacters(name, description);
                    characters.setType("Characters");
                    allLocations.get(numLoc).allAttr.put(name, characters);
                }

            }
        }
    }

    public void parsePath(ArrayList<Graph> sections){
        ArrayList<Edge> path = sections.get(1).getEdges();
        for (Edge edg : path){
            Node fromLocation = edg.getSource().getNode();
            String fromName = fromLocation.getId().getId();
            Node toLocation = edg.getTarget().getNode();
            String toName = toLocation.getId().getId();
            for (GameLocation location : allLocations){
                if (location.getName().equalsIgnoreCase(fromName)){
                    location.paths.add(toName);
                }
            }
        }
    }

    public ArrayList<GameLocation> getAllLocations() {
        return allLocations;
    }

    public String getStartLocation() { return startLocation; }


}

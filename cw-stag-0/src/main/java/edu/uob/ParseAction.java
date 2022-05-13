package edu.uob;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ParseAction {

    final File actionsFile;
    TreeMap<String, HashSet<GameAction>> allActions;   //save all actions in the file

    public ParseAction(File actionsFile) throws ParserConfigurationException, IOException, SAXException {
        this.actionsFile = actionsFile;
        allActions = new TreeMap<String, HashSet<GameAction>>();
        startActionParse();
    }

    public void startActionParse() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(actionsFile);
        Element root = document.getDocumentElement();
        NodeList actions = root.getChildNodes();
        for (int i=1; i<actions.getLength();i=i+2){
            Element command = (Element)actions.item(i);    //single action
            parseCommand(command);
        }
    }

    public void parseCommand(Element command){
        Element subjects = (Element)command.getElementsByTagName("subjects").item(0);
        HashSet<String> subEntity = new HashSet<>();
        for (int i=0; i<subjects.getElementsByTagName("entity").getLength();i++){
            subEntity.add(subjects.getElementsByTagName("entity").item(i).getTextContent());
        }

        Element consumed = (Element)command.getElementsByTagName("consumed").item(0);
        HashSet<String>  conEntity =new HashSet<>();
        for (int i=0; i<consumed.getElementsByTagName("entity").getLength();i++){
            conEntity.add(consumed.getElementsByTagName("entity").item(i).getTextContent());
        }

        Element produced = (Element)command.getElementsByTagName("produced").item(0);
        HashSet<String>  proEntity = new HashSet<>();
        for (int i=0; i<produced.getElementsByTagName("entity").getLength();i++){
            proEntity.add(produced.getElementsByTagName("entity").item(i).getTextContent());
        }

        String narration = command.getElementsByTagName("narration").item(0).getTextContent();
        GameAction oneAction = new GameAction(subEntity,conEntity,proEntity,narration);

        Element triggers = (Element)command.getElementsByTagName("triggers").item(0);
        saveAction(oneAction,triggers);
    }

    public void saveAction(GameAction oneAction ,Element triggers){
        boolean isExist = false;
        String existName = null;    //Record the name (key value) that appears
        for (int i=0; i<triggers.getElementsByTagName("keyword").getLength();i++){
            String triPhrase = triggers.getElementsByTagName("keyword").item(i).getTextContent();
            for (String key : allActions.keySet()) {   //Check whether the instruction has appeared
                if (triPhrase.equals(key)) {
                    isExist = true;
                    existName = key;
                    break;
                }
            }
            if (isExist){   //exist -> save
                allActions.get(existName).add(oneAction);
            }else{         //not exist -> new hashset
                HashSet<GameAction> newAction = new HashSet<>();
                newAction.add(oneAction);
                allActions.put(triPhrase,newAction);
            }
        }
    }

    public TreeMap<String, HashSet<GameAction>> getAllActions() { return allActions; }



}

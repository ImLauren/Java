package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenTest {
    DBTokenizer testToken;
    List<String> commandWord;
    List<String> trueValue;
    String command;

    @BeforeEach
    void setup(){
        commandWord = new ArrayList<>();
    }

    @Test
    void testNormalCmd() throws DBException {
        command = "DELETE * FROM table_name;";
        testToken = new DBTokenizer(command,commandWord);
        command = testToken.splitSpace();
        testToken.addCmdList(command);
        trueValue = new ArrayList<>(){{add("DELETE");add("*");add("FROM");add("table_name");add(";");}};
        assertEquals(trueValue, commandWord);
    }

    @Test
    void testSpaces() throws DBException {
        command = "SELECT    *  FROM     people  WHERE   Name  ==  'Steve' ;";
        testToken = new DBTokenizer(command,commandWord);
        command = testToken.splitSpace();
        testToken.addCmdList(command);
        trueValue = new ArrayList<>(){{add("SELECT");add("*");add("FROM");add("people");add("WHERE");
                                       add("Name");add("==");;add("'Steve'");add(";");}};
        assertEquals(trueValue, commandWord);
    }

    @Test
    void testQuot() throws DBException {
        command = "SELECT * FROM Websites WHERE country='CN';";
        testToken = new DBTokenizer(command,commandWord);
        command = testToken.splitSpace();
        testToken.addCmdList(command);
        trueValue = new ArrayList<>(){{add("SELECT");add("*");add("FROM");add("Websites");add("WHERE");
                                       add("country");add("=");add("'CN'");add(";");}};
        assertEquals(trueValue, commandWord);
    }

    @Test
    void testQuot2() throws DBException {
        command = "UPDATE Websites SET alexa='5000' WHERE name='USA';";
        testToken = new DBTokenizer(command,commandWord);
        command = testToken.splitSpace();
        testToken.addCmdList(command);
        trueValue = new ArrayList<>(){{add("UPDATE");add("Websites");add("SET");add("alexa");add("=");
            add("'5000'");add("WHERE");add("name");add("=");add("'USA'");add(";");}};
        assertEquals(trueValue, commandWord);
    }

    @Test
    void testComma() throws DBException {
        command = "SELECT column_name,column_name FROM table_name;";
        testToken = new DBTokenizer(command,commandWord);
        command = testToken.splitSpace();
        testToken.addCmdList(command);
        trueValue = new ArrayList<>(){{add("SELECT");add("column_name");add(",");add("column_name");
                                       add("FROM");add("table_name");add(";");}};
        assertEquals(trueValue, commandWord);
    }

    @Test
    void testBrackets() throws DBException {
        command = "CREATE TABLE Persons (PersonID, LastName, FirstName);";
        testToken = new DBTokenizer(command,commandWord);
        command = testToken.splitSpace();
        testToken.addCmdList(command);
        trueValue = new ArrayList<>(){{add("CREATE");add("TABLE");add("Persons");add("(");add("PersonID");
                  add(",");add("LastName");add(",");add("FirstName");add(")");add(";");}};
        assertEquals(trueValue, commandWord);
    }

    @Test
    void testUnequalSign() throws DBException {
        command = "SELECT * FROM Websites WHERE id>=1;";
        testToken = new DBTokenizer(command,commandWord);
        command = testToken.splitSpace();
        testToken.addCmdList(command);
        trueValue = new ArrayList<>(){{add("SELECT");add("*");add("FROM");add("Websites");add("WHERE");
            add("id");add(">=");add("1");add(";");}};
        assertEquals(trueValue, commandWord);
    }

    @Test
    void testUnequalSign2() throws DBException {
        command = "SELECT * FROM people WHERE id!=12;";
        testToken = new DBTokenizer(command,commandWord);
        command = testToken.splitSpace();
        testToken.addCmdList(command);
        trueValue = new ArrayList<>(){{add("SELECT");add("*");add("FROM");add("people");add("WHERE");
            add("id");add("!=");add("12");add(";");}};
        assertEquals(trueValue, commandWord);
    }

    @Test
    void testNoSemi(){
        command = "SELECT * FROM people";
        testToken = new DBTokenizer(command,commandWord);
        try {
            command = testToken.splitSpace();
        }catch (DBException e){
            e.printStackTrace();
            assertEquals("[ERROR] Expecting a ;", e.getMessage());
        }
    }

    @Test
    void testNoBracket() {
        command = "CREATE TABLE Persons (PersonID ;";
        testToken = new DBTokenizer(command,commandWord);
        try {
            command = testToken.splitSpace();
        }catch (DBException e){
            e.printStackTrace();
            assertEquals("[ERROR] Expecting a )", e.getMessage());
        }
    }

    @Test
    void testNoQuot(){
        command = "SELECT * FROM people WHERE Name=='Steve;";
        testToken = new DBTokenizer(command,commandWord);
        try {
            command = testToken.splitSpace();
        }catch (DBException e){
            e.printStackTrace();
            assertEquals("[ERROR] Expecting a '", e.getMessage());
        }
    }

}

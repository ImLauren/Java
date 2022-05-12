package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// PLEASE READ:
// The tests in this file will fail by default for a template skeleton, your job is to pass them
// and maybe write some more, read up on how to write tests at
// https://junit.org/junit5/docs/current/user-guide/#writing-tests
final class BasicCommandTests {

  private GameServer server;

  // Make a new server for every @Test (i.e. this method runs before every @Test test case)
  @BeforeEach
  void setup() {
      File entitiesFile = Paths.get("config/basic-entities.dot").toAbsolutePath().toFile();
      File actionsFile = Paths.get("config/basic-actions.xml").toAbsolutePath().toFile();
      server = new GameServer(entitiesFile, actionsFile);
  }

  // Test to spawn a new server and send a simple "look" command
  @Test
  void testLookingAroundStartLocation() {
    String response = server.handleCommand("player : look").toLowerCase();
    assertTrue(response.contains("empty room"), "Did not see description of room in response to look");
    assertTrue(response.contains("magic potion"), "Did not see description of artifacts in response to look");
    assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
  }

  @Test
  void testGetInvAroundStartLocation() {
    server.handleCommand("player : get potion");
    String response = server.handleCommand("player : inv").toLowerCase();
    assertTrue(response.contains("magic potion"), "Did not see list of inv in response to inv");
    response = server.handleCommand("player : inventory").toLowerCase();
    assertTrue(response.contains("magic potion"), "Did not see list of inventory in response to inventory");
    response = server.handleCommand("player : look").toLowerCase();
    assertFalse(response.contains("magic potion"), "Did see description of potion in response to look");
  }

  @Test
  void testDropAroundStartLocation() {
    server.handleCommand("player : get potion");
    String response = server.handleCommand("player : inv").toLowerCase();
    assertTrue(response.contains("magic potion"), "Did not see list of inv in response to inv");
    server.handleCommand("player : drop potion");
    response = server.handleCommand("player : inv").toLowerCase();
    assertFalse(response.contains("magic potion"), "Did see description of potion in response to inv");
    response = server.handleCommand("player : look").toLowerCase();
    assertTrue(response.contains("magic potion"), "Did not see description of artifacts in response to look");
  }

  @Test
  void testGotoFromStartLocation() {
    server.handleCommand("player : goto forest");
    String response = server.handleCommand("player : look").toLowerCase();
    assertTrue(response.contains("a dark forest"), "Did not see description of forest in response to look");
    assertTrue(response.contains("brass key"), "Did not see description of key in response to look");
  }

  @Test
  void testHealthCommand() {
    String response = server.handleCommand("player : health").toLowerCase();
    assertTrue(response.contains("health"), "Did not see description of health in response to health");
  }

  @Test
  void testSimpleCompleteCmd(){
    String response = server.handleCommand("player : look").toLowerCase();
    assertTrue(response.contains("empty room"), "Did not see description of room in response to look");
    assertTrue(response.contains("magic potion"), "Did not see description of artifacts in response to look");
    assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
    server.handleCommand("player : goto forest");
    response = server.handleCommand("player : look").toLowerCase();
    assertTrue(response.contains("a dark forest"), "Did not see description of forest in response to look");
    assertTrue(response.contains("brass key"), "Did not see description of key in response to look");
    server.handleCommand("player : get key");
    response = server.handleCommand("player : inv").toLowerCase();
    assertTrue(response.contains("key"), "Did not see name of key in response to inv");
    assertTrue(response.contains("brass key"), "Did not see description of key in response to inv");
    response = server.handleCommand("player : look").toLowerCase();
    assertFalse(response.contains("brass key"),"Did see key of description in response to inv");
    server.handleCommand("player : goto cabin");
    server.handleCommand("player : drop key");
    response = server.handleCommand("player : inv").toLowerCase();
    assertFalse(response.contains("brass key"), "Did see description of key in response to inv");
    response = server.handleCommand("player : look").toLowerCase();
    assertTrue(response.contains("brass key"), "Did not see description of key in response to look");
  }

  @Test
  void testMultiplePlayers(){
    server.handleCommand("Peter : goto forest");
    server.handleCommand("Helen Alex: look");
    String response = server.handleCommand("Peter : look").toLowerCase();
    assertFalse(response.contains("helen"), "Did see name of other player in response to look");
    server.handleCommand("Helen Alex: goto forest");
    response = server.handleCommand("Peter : look").toLowerCase();
    assertTrue(response.contains("helen alex"), "Did not see name of other player in response to look");
  }

  @Test
  void testErrorCommand(){
    assertTrue(server.handleCommand("p@lay1 : look").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("player : goto").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("player : get").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("player : drop").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("player : oopen trapdoor").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("player : unlock").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("player : open").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("player : drink").startsWith("[ERROR]"));
  }

  @Test
  void testOpenCmd(){
    String response = server.handleCommand("Peter : look").toLowerCase();
    assertFalse(response.contains("cellar"), "Did see path of cellar in response to look");
    server.handleCommand("Peter : goto forest");
    server.handleCommand("Peter : get key");
    server.handleCommand("Peter : goto cabin");
    response = server.handleCommand("Peter : open trapdoor with key");
    assertTrue(response.contains("You unlock the trapdoor and see steps leading down into a cellar"),
            "Did not see narration in response to open door");
    response = server.handleCommand("Peter : look").toLowerCase();
    assertTrue(response.contains("cellar"), "Did not see path of cellar in response to look");
  }

  @Test
  void testUnlockCmd(){
    String response = server.handleCommand("Peter : look").toLowerCase();
    assertFalse(response.contains("cellar"), "Did see path of cellar in response to look");
    server.handleCommand("Peter : goto forest");
    server.handleCommand("Peter : get key");
    server.handleCommand("Peter : goto cabin");
    response = server.handleCommand("Peter : use key to unlock trapdoor");
    assertTrue(response.contains("You unlock the trapdoor and see steps leading down into a cellar"),
            "Did not see narration in response to open door");
    response = server.handleCommand("Peter : look").toLowerCase();
    assertTrue(response.contains("cellar"), "Did not see path of cellar in response to look");
  }

  @Test
  void testDrinkCmd(){
    String response = server.handleCommand("Peter : look").toLowerCase();
    assertTrue(response.contains("potion"), "Did not see name of potion in response to look");
    response = server.handleCommand("Peter : health").toLowerCase();
    assertTrue(response.contains("3"),"Did not see healthlevel of player in response to health");
    response = server.handleCommand("Peter : drink potion");
    assertTrue(response.contains("You drink the potion and your health improves"),
            "Did not see narration in response to drink potion");
    response = server.handleCommand("Peter : health").toLowerCase();
    assertTrue(response.contains("3"),"Did not see healthlevel of player in response to health");
    response = server.handleCommand("Peter : look").toLowerCase();
    assertFalse(response.contains("potion"), "Did see name of potion in response to look");
  }

  @Test
  void testFightCmd(){
    server.handleCommand("Peter : goto forest");
    server.handleCommand("Peter : get key");
    server.handleCommand("Peter : goto cabin");
    server.handleCommand("Peter : open trapdoor with key");
    server.handleCommand("Peter : goto cellar");
    String response = server.handleCommand("Peter : look").toLowerCase();
    assertTrue(response.contains("a dusty cellar"), "Did not see description of cellar in response to look");
    assertTrue(response.contains("angry elf"), "Did not see description of elf in response to look");
    response = server.handleCommand("Peter : health").toLowerCase();
    assertTrue(response.contains("3"),"Did not see healthlevel of player in response to health");
    response = server.handleCommand("Peter : fight with elf");
    assertTrue(response.contains("You attack the elf, but he fights back and you lose some health"),
            "Did not see narration in response to fight with elf");
    response = server.handleCommand("Peter : health").toLowerCase();
    assertTrue(response.contains("2"),"Did not see healthlevel of player in response to health");
    server.handleCommand("Peter : hit elf");
    response = server.handleCommand("Peter : attack elf");
    assertTrue(response.contains("You died and lost all of your items, you must return to the start of the game"),
            "Did not see narration in response to fight with elf and died");
    response = server.handleCommand("Peter : look").toLowerCase();
    assertTrue(response.contains("empty room"), "Did not see description of room in response to look");
    assertTrue(response.contains("magic potion"), "Did not see description of artifacts in response to look");
    assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
  }

  @Test
  void testCompleteCmd(){
    String response = server.handleCommand("Alice : look").toLowerCase();
    assertTrue(response.contains("magic potion"));
    server.handleCommand("Alice : get potion");
    response = server.handleCommand("Alice : look").toLowerCase();
    assertFalse(response.contains("magic potion"));
    response = server.handleCommand("Alice : goto cellar").toLowerCase();
    assertTrue(response.contains("you cannot goto <cellar> "));
    response = server.handleCommand("Alice : inv").toLowerCase();
    assertTrue(response.contains("potion"));
    server.handleCommand("Alice : goto forest");
    server.handleCommand("Alice : get key");
    server.handleCommand("Alice : goto cabin");
    response = server.handleCommand("Alice : using key to open trapdoor").toLowerCase();
    assertTrue(response.contains("you unlock the trapdoor and see steps leading down into a cellar"));
    server.handleCommand("Alice : goto cellar");
    response = server.handleCommand("Alice : look").toLowerCase();
    assertTrue(response.contains("angry elf"));
    server.handleCommand("Alice : hit elf");
    response = server.handleCommand("Alice : attack elf").toLowerCase();
    assertTrue(response.contains("you attack the elf, but he fights back and you lose some health"));
    server.handleCommand("Alice : drop potion");
    response = server.handleCommand("Alice : look").toLowerCase();
    assertTrue(response.contains("magic potion"));
    response = server.handleCommand("Alice : inv").toLowerCase();
    assertFalse(response.contains("magic potion"));
    server.handleCommand("Alice : get potion");
    response = server.handleCommand("Alice : look").toLowerCase();
    assertFalse(response.contains("magic potion"));
    response = server.handleCommand("Alice : inv").toLowerCase();
    assertTrue(response.contains("magic potion"));
    response = server.handleCommand("Alice : drink potion").toLowerCase();
    assertTrue(response.contains("you drink the potion and your health improves"));
    server.handleCommand("Alice : fight with elf");
    response = server.handleCommand("Alice : fight elf").toLowerCase();
    assertTrue(response.contains("you died and lost all of your items, you must return to the start of the game"));
    response = server.handleCommand("Alice : look").toLowerCase();
    assertTrue(response.contains("empty room"), "Did not see description of room in response to look");
  }



}

package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// PLEASE READ:
// The tests in this file will fail by default for a template skeleton, your job is to pass them
// and maybe write some more, read up on how to write tests at
// https://junit.org/junit5/docs/current/user-guide/#writing-tests
final class ExtendedCmdTests {

    private GameServer server;

    // Make a new server for every @Test (i.e. this method runs before every @Test test case)
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config/extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config/extended-actions.xml").toAbsolutePath().toFile();
        server = new GameServer(entitiesFile, actionsFile);
    }

    @Test
    void testLookAtStartLocation() {
        String response = server.handleCommand("Harry : look").toLowerCase();
        assertTrue(response.contains("a log cabin in the woods"));
        assertTrue(response.contains("a bottle of magic potion"));
        assertTrue(response.contains("a razor sharp axe"));
        assertTrue(response.contains("a silver coin"));
        assertTrue(response.contains("a locked wooden trapdoor in the floor"));
    }

    @Test
    void testCompleteCmd() {
        server.handleCommand("Harry : get potion");
        server.handleCommand("Harry : get axe");
        server.handleCommand("Harry : get coin");
        String response = server.handleCommand("Harry : inv").toLowerCase();
        assertTrue(response.contains("a bottle of magic potion"));
        assertTrue(response.contains("a razor sharp axe"));
        assertTrue(response.contains("a silver coin"));
        response = server.handleCommand("Harry : look").toLowerCase();
        assertFalse(response.contains("a bottle of magic potion"));
        assertFalse(response.contains("a razor sharp axe"));
        assertFalse(response.contains("a silver coin"));
        server.handleCommand("Harry : goto forest");
        response = server.handleCommand("Harry : look").toLowerCase();
        assertTrue(response.contains("a tall pine tree"));
        response = server.handleCommand("Harry : cut tree with axe").toLowerCase();
        assertTrue(response.contains("you cut down the tree with the axe"));
        response = server.handleCommand("Harry : look").toLowerCase();
        assertTrue(response.contains("a heavy wooden log"));
        server.handleCommand("Harry : get log");
        response = server.handleCommand("Harry : look").toLowerCase();
        assertFalse(response.contains("a heavy wooden log"));
        assertFalse(response.contains("a tall pine tree"));
        response = server.handleCommand("Harry : inventory").toLowerCase();
        assertTrue(response.contains("a heavy wooden log"));
        server.handleCommand("Harry : get key");
        response = server.handleCommand("Harry : drink potion").toLowerCase();
        assertTrue(response.contains("you drink the potion and your health improves"));
        response = server.handleCommand("Harry : health").toLowerCase();
        assertTrue(response.contains("3"));
        server.handleCommand("Harry : goto riverbank");
        response = server.handleCommand("Harry : look").toLowerCase();
        assertTrue(response.contains("a grassy riverbank"));
        assertTrue(response.contains("an old brass horn"));
        assertTrue(response.contains("a fast flowing river"));
        response = server.handleCommand("Harry : blow horn").toLowerCase();
        assertTrue(response.contains("you blow the horn and as if by magic, a lumberjack appears"));
        response = server.handleCommand("Harry : look").toLowerCase();
        assertTrue(response.contains("lumberjack"));
        assertTrue(response.contains("a burly wood cutter"));
        response = server.handleCommand("Harry : bridge the river with the log").toLowerCase();
        assertTrue(response.contains("you bridge the river with the log and can now reach the other side"));
        response = server.handleCommand("Harry : look").toLowerCase();
        assertTrue(response.contains("clearing"));
        server.handleCommand("Harry : goto clearing");
        response = server.handleCommand("Harry : look").toLowerCase();
        assertTrue(response.contains("a clearing in the woods"));
        assertTrue(response.contains("ground"));
        assertTrue(response.contains("it looks like the soil has been recently disturbed"));
        server.handleCommand("Harry : goto riverbank");
        server.handleCommand("Harry : goto forest");
        server.handleCommand("Harry : goto cabin");
        response = server.handleCommand("Harry : unlock trapdoor with key").toLowerCase();
        assertTrue(response.contains("you unlock the door and see steps leading down into a cellar"));
        server.handleCommand("Harry : goto cellar");
        response = server.handleCommand("Harry : hit elf").toLowerCase();
        assertTrue(response.contains("you attack the elf, but he fights back and you lose some health"));
        response = server.handleCommand("Harry : pay elf coin").toLowerCase();
        assertTrue(response.contains("you pay the elf your silver coin and he produces a shovel"));
        response = server.handleCommand("Harry : look").toLowerCase();
        assertTrue(response.contains("a sturdy shovel"));
        server.handleCommand("Harry : get shovel");
        response = server.handleCommand("Harry : inv").toLowerCase();
        assertTrue(response.contains("a sturdy shovel"));
        server.handleCommand("Harry : goto cabin");
        server.handleCommand("Harry : goto forest");
        server.handleCommand("Harry : goto riverbank");
        server.handleCommand("Harry : goto clearing");
        response = server.handleCommand("Harry : dig into ground using shovel").toLowerCase();
        assertTrue(response.contains("you dig into the soft ground and unearth a pot of gold !!!"));
        response = server.handleCommand("Harry : look").toLowerCase();
        assertTrue(response.contains("a deep hole in the ground"));
        assertTrue(response.contains("a big pot of gold"));
        response = server.handleCommand("Harry : drop log").toLowerCase();
        assertTrue(response.contains("you don't have log in your inventory!"));
        server.handleCommand("Harry : get gold");
        response = server.handleCommand("Harry : inv").toLowerCase();
        assertTrue(response.contains("a big pot of gold"));
    }




}
package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// PLEASE READ:
// The tests in this file will fail by default for a template skeleton, your job is to pass them
// and maybe write some more, read up on how to write tests at
// https://junit.org/junit5/docs/current/user-guide/#writing-tests
final class ControllerTests {
  OXOModel model;
  OXOController controller;

  // create your standard 3*3 OXO board (where three of the same symbol in a line wins) with the X
  // and O player
  private static OXOModel createStandardModel() {
    OXOModel model = new OXOModel(3, 3, 3);
    model.addPlayer(new OXOPlayer('X'));
    model.addPlayer(new OXOPlayer('O'));
    return model;
  }

  // we make a new board for every @Test (i.e. this method runs before every @Test test case)
  @BeforeEach
  void setup() {
    model = createStandardModel();
    controller = new OXOController(model);
  }

  // here's a basic test for the `controller.handleIncomingCommand` method
  @Test
  void testHandleIncomingCommand() throws OXOMoveException {
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a1");

    // A move has been made for A1 (i.e. the [0,0] cell on the board), let's see if that cell is
    // indeed owned by the player
    assertEquals(firstMovingPlayer, controller.gameModel.getCellOwner(0, 0));
  }

  // here's a complete game where we find out if someone won
  @Test
  void testBasicWinWithA1A2A3() throws OXOMoveException {
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("a3");

    // OK, so A1, A2, A3 is a win and that last A3 move is made by the first player (players
    // alternative between moves) let's make an assertion to see whether the first moving player is
    // the winner here
    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testBasicWinWithA2B2C2() throws OXOMoveException {  //second column vertically
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("c3");
    controller.handleIncomingCommand("c2");
    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testBasicWinWithA1B2C3() throws OXOMoveException {  //diagonally -- down-right
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("c2");
    controller.handleIncomingCommand("c3");
    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testBasicWinWithA3B2C1() throws OXOMoveException {  //diagonally -- down-left (second player win)
    controller.handleIncomingCommand("a1");
    // take note of whose gonna made the second move
    OXOPlayer secondMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a3");
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("c3");
    controller.handleIncomingCommand("c1");
    assertEquals(
            secondMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(secondMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testBasicDrawn() throws OXOMoveException {  //drawn
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("a3");
    controller.handleIncomingCommand("c1");
    controller.handleIncomingCommand("c2");
    controller.handleIncomingCommand("b3");
    controller.handleIncomingCommand("c3");
    assertTrue(model.isGameDrawn());
  }

  @Test
  void testUppercase() throws OXOMoveException {   //uppercase input
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("A1");
    assertEquals(firstMovingPlayer, controller.gameModel.getCellOwner(0, 0));
    controller.handleIncomingCommand("B1");
    controller.handleIncomingCommand("C2");
    assertEquals(firstMovingPlayer, controller.gameModel.getCellOwner(2, 1));
  }

  @Test
  void testErrorLength() {  //input wrong length
    assertThrows(OXOMoveException.InvalidIdentifierLengthException.class, ()-> controller.handleIncomingCommand("a"));
    assertThrows(OXOMoveException.InvalidIdentifierLengthException.class, ()-> controller.handleIncomingCommand("aa1"));
    assertThrows(OXOMoveException.InvalidIdentifierLengthException.class, ()-> controller.handleIncomingCommand("a12"));
    assertThrows(OXOMoveException.InvalidIdentifierLengthException.class, ()-> controller.handleIncomingCommand("ab3"));
    assertThrows(OXOMoveException.InvalidIdentifierLengthException.class, ()-> controller.handleIncomingCommand("b1c"));
  }

  @Test
  void testErrorRange() {  //error row range
    assertThrows(OXOMoveException.OutsideCellRangeException.class, ()-> controller.handleIncomingCommand("a4"));
    assertThrows(OXOMoveException.OutsideCellRangeException.class, ()-> controller.handleIncomingCommand("c0"));
    assertThrows(OXOMoveException.OutsideCellRangeException.class, ()-> controller.handleIncomingCommand("b4"));
    assertThrows(OXOMoveException.OutsideCellRangeException.class, ()-> controller.handleIncomingCommand("d1"));
    assertThrows(OXOMoveException.OutsideCellRangeException.class, ()-> controller.handleIncomingCommand("d2"));
    controller.addRow();
    controller.addColumn();
    assertThrows(OXOMoveException.OutsideCellRangeException.class, ()-> controller.handleIncomingCommand("c9"));
    assertThrows(OXOMoveException.OutsideCellRangeException.class, ()-> controller.handleIncomingCommand("i4"));
  }

  @Test
  void testErrorCharacter() {  //error Character
    assertThrows(OXOMoveException.InvalidIdentifierCharacterException.class, ()-> controller.handleIncomingCommand("~1"));
    assertThrows(OXOMoveException.InvalidIdentifierCharacterException.class, ()-> controller.handleIncomingCommand("!1"));
    assertThrows(OXOMoveException.InvalidIdentifierCharacterException.class, ()-> controller.handleIncomingCommand("a!"));
    assertThrows(OXOMoveException.InvalidIdentifierCharacterException.class, ()-> controller.handleIncomingCommand("aa"));
  }

  @Test
  void testErrorCellTaken() throws OXOMoveException {  //CellAlreadyTaken
    controller.handleIncomingCommand("a1");
    assertThrows(OXOMoveException.CellAlreadyTakenException.class, ()-> controller.handleIncomingCommand("a1"));
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("c2");
    assertThrows(OXOMoveException.CellAlreadyTakenException.class, ()-> controller.handleIncomingCommand("b2"));
  }

  @Test
  void testAddNewPlayer1() throws OXOMoveException {   //add a new player  && uppercase letter
    // take note of whose gonna made the first move
    model.addPlayer(new OXOPlayer('P'));
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("A1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("a3");
    controller.handleIncomingCommand("B1");
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("b3");
    controller.handleIncomingCommand("c1");
    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));

  }

  @Test
  void testAddNewPlayer2() throws OXOMoveException {   //add a new player when playing -- new player win
    // take note of whose gonna made the first move
    controller.addRow();
    controller.addColumn();
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("a3");
    model.addPlayer(new OXOPlayer('P'));
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("b3");
    OXOPlayer thirdMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("c3");
    controller.handleIncomingCommand("c2");
    controller.handleIncomingCommand("d3");
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("c1");
    controller.handleIncomingCommand("c4");
    controller.handleIncomingCommand("a1");

    assertEquals(
            thirdMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(thirdMovingPlayer.getPlayingLetter()));

  }

  @Test
  void testAddNewPlayer3() throws OXOMoveException {   //add a new player when playing -- new player win
    // take note of whose gonna made the first move
    controller.addRow();
    controller.addColumn();
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("c1");
    controller.handleIncomingCommand("b3");
    model.addPlayer(new OXOPlayer('P'));
    controller.handleIncomingCommand("a4");
    OXOPlayer thirdMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("b4");
    controller.handleIncomingCommand("c3");
    controller.handleIncomingCommand("c2");
    controller.handleIncomingCommand("d4");
    controller.handleIncomingCommand("c4");
    controller.handleIncomingCommand("d2");

    assertEquals(
            thirdMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(thirdMovingPlayer.getPlayingLetter()));

  }

  @Test
  void testDecreaseWin() throws OXOMoveException {   //decrease WinThreshold after input and X win
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("a2");
    controller.decreaseWinThreshold();

    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));

  }

  @Test
  void testDecreaseDrawn() throws OXOMoveException {   //decrease WinThreshold after input and the game is drawn
    // take note of whose gonna made the first move
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("b2");
    controller.decreaseWinThreshold();

    assertTrue(model.isGameDrawn());
  }

  @Test
  void testIncreaseWin() throws OXOMoveException {   //increase WinThreshold after input and X win
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("b2");
    controller.increaseWinThreshold();
    controller.addRow();
    controller.addColumn();
    controller.handleIncomingCommand("a3");
    controller.handleIncomingCommand("b3");
    controller.handleIncomingCommand("a4");

    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));

  }

}

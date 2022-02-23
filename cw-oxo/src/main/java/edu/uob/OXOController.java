package edu.uob;

import edu.uob.OXOMoveException.*;

class OXOController {
  OXOModel gameModel;

  public OXOController(OXOModel model) {
    gameModel = model;
  }

  int num_player = 0;
  public void handleIncomingCommand(String command) throws OXOMoveException {
    int len = command.length();  //InvalidIdentifierLength
    if (len != 2) {
      throw new OXOMoveException.InvalidIdentifierLengthException(len);
    }

    char ch1 = command.charAt(0);
    char ch2 = command.charAt(1);

    if (ch1 >= 'A' && ch1 <= 'Z') {   //uppercase -> lowercase
      int tmp_char = ch1 + 32;
      ch1 = (char) tmp_char;
    }
    if (ch1 < 'a' || ch1 > 'z') {  //row - InvalidIdentifierCharacter
      throw new OXOMoveException.InvalidIdentifierCharacterException(RowOrColumn.ROW, ch1);
    }
    if (ch2 < '0' || ch2 > '9') {   //column - InvalidIdentifierCharacter
      throw new OXOMoveException.InvalidIdentifierCharacterException(RowOrColumn.COLUMN, ch2);
    }

    int n_row = ch1 - 'a';  //number of row
    int n_col = ch2 - '1';  //number of column
    int row = gameModel.getNumberOfRows();
    int col = gameModel.getNumberOfColumns();
    boolean win = false;

    if (n_row >= row || n_row < 0) {   //OutsideCellRange row
      throw new OXOMoveException.OutsideCellRangeException(RowOrColumn.ROW, n_row + 1);
    }
    if (n_col >= col || n_col < 0) {   //OutsideCellRange column
      throw new OXOMoveException.OutsideCellRangeException(RowOrColumn.COLUMN, n_col + 1);
    }
    if (gameModel.cells.get(n_row).get(n_col) != null) {    //CellAlreadyTaken
      throw new OXOMoveException.CellAlreadyTakenException(n_row , n_col);
    }

    if (gameModel.getWinner() == null) {
      OXOPlayer now_player;  //current player
      now_player = gameModel.getPlayerByNumber(num_player);
      gameModel.setCellOwner(n_row, n_col, now_player);
      num_player++;
      int n_players = gameModel.getNumberOfPlayers();   //the number of all players
      if (num_player == n_players) {
        num_player = 0;
      }
      gameModel.setCurrentPlayerNumber(num_player);   //next player

      win = gameModel.winRow(row, col, now_player);  //win - row
      if (!win) {
        win = gameModel.winCol(row, col, now_player);  //win - column
      }
      if (!win) {
        win = gameModel.winDiag(row, col, now_player);  //win - diagonally
      }
      if (win) {
        gameModel.setWinner(now_player);  //winner
      }
    }

    //whether all cells are written or not
    boolean flag = gameModel.winFull(row, col);
    if (flag && !win) {
      gameModel.setGameDrawn();
    }

  }

  public void addRow() {
    gameModel.addRow();
  }

  public void removeRow() {
    gameModel.removeRow();
  }

  public void addColumn() {
    gameModel.addColumn();
  }

  public void removeColumn() {
    gameModel.removeColumn();
  }

  public void increaseWinThreshold() {
    int win_num = gameModel.getWinThreshold();
    win_num++;
    gameModel.setWinThreshold(win_num);
  }

  public void decreaseWinThreshold() {
    int win_num = gameModel.getWinThreshold();
    int row = gameModel.getNumberOfRows();
    int col = gameModel.getNumberOfColumns();
    win_num--;
    gameModel.setWinThreshold(win_num);

    boolean[] win = new boolean[gameModel.getNumberOfPlayers()];
    for (int i = 0; i < gameModel.getNumberOfPlayers(); i++) {
      OXOPlayer now_player = gameModel.getPlayerByNumber(i);
      win[i] = gameModel.winRow(row, col, now_player);  //win - row
      if (!win[i]) {
        win[i] = gameModel.winCol(row, col, now_player);  //win - column
      }
      if (!win[i]) {
        win[i] = gameModel.winCol(row, col, now_player);  //win - column
      }
      if (!win[i]) {
        win[i] = gameModel.winDiag(row, col, now_player);  //win - diagonally
      }
    }

    for (int i = 0; i < gameModel.getNumberOfPlayers(); i++) {
      for (int j = 0; j < gameModel.getNumberOfPlayers(); j++) {
        if (win[i] && win[j] && i != j) {
          gameModel.setGameDrawn();
          return;
        }
      }
    }

    for (int i = 0; i < gameModel.getNumberOfPlayers(); i++) {
      OXOPlayer now_player = gameModel.getPlayerByNumber(i);
      if (win[i]){
        gameModel.setWinner(now_player);  //winner
        return;
      }
    }
  }

}

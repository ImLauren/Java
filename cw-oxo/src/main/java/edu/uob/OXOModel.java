package edu.uob;

import java.util.*;

class OXOModel {
  List<List<OXOPlayer>> cells = new ArrayList<>();  //cells -> ArrayLists
  List<OXOPlayer> players = new ArrayList<>();  //players -> ArrayLists
  private int currentPlayerNumber;
  private OXOPlayer winner;
  private boolean gameDrawn;
  private int winThreshold;

  public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
    winThreshold = winThresh;
    //initialize arraylists
    for (int i=0; i<numberOfRows;i++){
      List<OXOPlayer> cols = new ArrayList<>();
      for (int j=0; j<numberOfColumns;j++){
        cols.add(j,null);
      }
      cells.add(i,cols);
    }
  }

  public int getNumberOfPlayers() {return players.size();}

  public void addPlayer(OXOPlayer player) {
    int plays_num = getNumberOfPlayers();
    players.add(plays_num,player);
  }

  public OXOPlayer getPlayerByNumber(int number) {return players.get(number);}

  public OXOPlayer getWinner() {return winner;}

  public void setWinner(OXOPlayer player) {winner = player;}

  public int getCurrentPlayerNumber() {return currentPlayerNumber;}

  public void setCurrentPlayerNumber(int playerNumber) {currentPlayerNumber = playerNumber;}

  public int getNumberOfRows() {return cells.size();}

  public int getNumberOfColumns() {return cells.get(0).size();}

  public OXOPlayer getCellOwner(int rowNumber, int colNumber) {return (cells.get(rowNumber)).get(colNumber);}

  public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
    (cells.get(rowNumber)).set(colNumber,player);
  }

  public void setWinThreshold(int winThresh) {winThreshold = winThresh;}

  public int getWinThreshold() {return winThreshold;}

  public void setGameDrawn() {gameDrawn = true;}

  public boolean isGameDrawn() {return gameDrawn;}

  public void addColumn(){
    int row = getNumberOfRows();
    int col = getNumberOfColumns();
    for (int i=0; i<row && col<9;i++){
      (cells.get(i)).add(col,null);
    }
  }

  public void addRow() {
    int row = getNumberOfRows();
    int col = getNumberOfColumns();
    if (row<9){
      ArrayList <OXOPlayer> cols = new ArrayList<>();
      for (int j=0; j<col && row<9;j++){
        cols.add(j,null);
      }
      cells.add(row, cols);
    }
  }

  public void removeColumn() {
    int row = getNumberOfRows();
    int col = getNumberOfColumns();
    for (int i=0; i<row && col>1; i++){
      (cells.get(i)).remove(col-1);
    }
  }

  public void removeRow() {
    int row = getNumberOfRows();
    int col = getNumberOfColumns();
    if(row>1){
      if (col >0) {
        (cells.get(row - 1)).subList(0, col).clear();
      }
      cells.remove(row-1);
    }
  }

  public boolean winRow(int row, int col, OXOPlayer now_player) {
    OXOPlayer tmp;
    int win_num = getWinThreshold();
    for (int i=0; i<row; i++){
      for (int j=0; j<col-win_num+1; j++){
        tmp = cells.get(i).get(j);
        int k=0;
        while (tmp == now_player && k<win_num){
          k++;
          if (j+k<col){
            tmp = cells.get(i).get(j+k);
          }
        }
        if (k==win_num){
          return true;
        }
      }
    }
    return false;
  }

  public boolean winCol(int row, int col, OXOPlayer now_player) {
    OXOPlayer tmp;
    int win_num = getWinThreshold();
    for (int i = 0; i < row-win_num+1; i++) {
      for (int j = 0; j < col; j++) {
        tmp = cells.get(i).get(j);
        int k = 0;
        while (tmp == now_player && k < win_num) {
          k++;
          if (i+k<row){
            tmp = cells.get(i + k).get(j);
          }
        }
        if (k == win_num) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean winDiag(int row, int col, OXOPlayer now_player) {
    OXOPlayer tmp;
    int win_num = getWinThreshold();
    //right-down
    for (int i=0; i<row-win_num+1;i++){
      for (int j=0; j<col-win_num+1;j++){
        tmp = cells.get(i).get(j);
        int k=0;
        while (tmp == now_player && k <win_num){
          k++;
          if (i+k<row && j+k<col){
            tmp = cells.get(i+k).get(j+k);
          }
        }
        if (k == win_num) {
          return true;
        }
      }
    }
    //left-down
    OXOPlayer tmp2;
    for (int i=0; i<row-win_num+1;i++){
      for (int j=win_num-1; j<col;j++){
        tmp2 = cells.get(i).get(j);
        int k=0;
        while (tmp2 == now_player && k <win_num){
          k++;
          if (i+k<row && j-k>=0){
            tmp2 = cells.get(i+k).get(j-k);
          }
        }
        if (k == win_num) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean winFull(int row, int col){
    for (int i=0; i<row; i++){
      for (int j=0; j<col; j++){
        if(cells.get(i).get(j) == null){
          return false;
        }
      }
    }
    return true;
  }

}

package chess.model;

import java.util.Arrays;

public class GameModel {
  private static GameModel INSTANCE = new GameModel();
  public int files=8, ranks=8;
  public int whiteKingIndex, blackKingIndex;
  public int enPassantSquare = -1;

  private char[] chessBoard = new char[files*ranks];
  
  //Method to get static Instance of class
  public static GameModel getInstance(){
    return INSTANCE;
  }
  
  //#region Board Methods
  private void clearBoard(){
    Arrays.fill(chessBoard, ' ');
  }

  public void printBoard(){
    System.out.println();
    for(int i=0; i<chessBoard.length; i+=8){
      System.out.println(((i/ranks))+Arrays.toString(Arrays.copyOfRange(chessBoard, i, i+files)));
    }
    String str = "  ";
    for(int i=0; i<files; i++){
      str=str+i+"  ";
    }
    System.out.println(str+"\n");
    //System.out.println("  a, b, c, d, e, f, g, h");
  }

  public char[] getBoard(){
    return chessBoard;
  }

  public int getIndex(int file, int rank){
    return rank*ranks + file;
  }

  public int[] getCoordinates(int index){
    int[] coords = new int[2];
    coords[0] = index % files;
    coords[1] = index / ranks;
    return coords;
  }

  public void setBoard(int file, int rank, char piece){
    chessBoard[getIndex(file,rank)] = piece;
  }

  public void newStandardChessBoard(){
    clearBoard();
    for(int c=0; c < 2; c++){
      // Place pawns
      for(int i=0;i<files;i++){
        setBoard(i, c%2==0 ? 6:1, c%2==0 ? 'P':'p');
      }
      // Place rooks
      setBoard(0, c%2==0 ? 7:0, c%2==0 ? 'R':'r');
      setBoard(7, c%2==0 ? 7:0, c%2==0 ? 'R':'r');
      // Place knights
      setBoard(1, c%2==0 ? 7:0, c%2==0 ? 'N':'n');
      setBoard(6, c%2==0 ? 7:0, c%2==0 ? 'N':'n');
      // Place bishops
      setBoard(2, c%2==0 ? 7:0, c%2==0 ? 'B':'b');
      setBoard(5, c%2==0 ? 7:0, c%2==0 ? 'B':'b');
      // Place queens
      setBoard(3, c%2==0 ? 7:0, c%2==0 ? 'Q':'q');
      // Place kings
      setBoard(4, c%2==0 ? 7:0, c%2==0 ? 'K':'K');
      if(c%2==0){
        whiteKingIndex = getIndex(4,7);
      } else {
        blackKingIndex = getIndex(4,0);
      }
    }
    printBoard();
  }
  //#endregion

  //
}

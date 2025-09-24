package chess.move;

import chess.Main;

public class Move {
  public int[] coords = new int[4];
  public Character piece, capture;
  //public Piece capture;
  public boolean isWhite;
  public String special="";

  public Move(int col, int row, int newCol, int newRow) {
    coords[0]=col;
    coords[1]=row;
    coords[2]=newCol;
    coords[3]=newRow;
    piece = Main.GM.getBoard()[Main.GM.getIndex(col, row)];
    capture = Main.GM.getBoard()[Main.GM.getIndex(newCol, newRow)];
    isWhite = Character.isUpperCase(piece);
    //piece = Main.GM.getPiece(col, row);
    //capture = Main.GM.getPiece(newCol, newRow);
  }

  public boolean isValidMove(){
    return true;
  }

  public boolean pieceCollision(){
    return false;
  }

  @Override
  public String toString() {
    String str = String.format("%d%d%d%d%c", coords[0], coords[1], coords[2], coords[3], capture);
    return str;
  }

  public boolean isVerticalCollision() {
    if (coords[0]==coords[2]) {
      int start = Math.min(coords[1], coords[3]) + 1;
      int end = Math.max(coords[1], coords[3]);
      for (int r=start; r<end; r++) {
        if (!Main.GM.getBoard()[Main.GM.getIndex(coords[0], r)].equals(' ')) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isHorizontalCollision() {
    if (coords[1]==coords[3]) {
      int start = Math.min(coords[0], coords[2]) + 1;
      int end = Math.max(coords[0], coords[2]);
      for (int c=start; c<end; c++) {
        if (!Main.GM.getBoard()[Main.GM.getIndex(c, coords[1])].equals(' ')) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isDiagonalCollision() {
    if(Math.abs(coords[0]-coords[2]) == Math.abs(coords[1]-coords[3])) {
      int colStep = (coords[2]-coords[0]) / Math.abs(coords[2]-coords[0]);
      int rowStep = (coords[3]-coords[1]) / Math.abs(coords[3]-coords[1]);
      int steps = Math.abs(coords[0]-coords[2]);
      for (int i = 1; i < steps; i++) {
        if (!Main.GM.getBoard()[Main.GM.getIndex(coords[0] + i * colStep, coords[1] + i * rowStep)].equals(' ')) {
          return true;
        }
      }
    }
    return false;
  }
}


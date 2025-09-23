package chess.pieces;

import chess.Main;

public class Piece {
  public int[] coord = new int[2];
  public int[] pos = new int[2];
  public boolean isWhite;
  public Character type;

  public Piece(int[] coordinates, Character piece){
    this.coord = coordinates;
    this.pos[0] = coord[0] * Main.gridSize;
    this.pos[1] = coord[1] * Main.gridSize;
    this.type = piece;
    this.isWhite = Character.isUpperCase(type);
  }

  @Override
  public String toString(){
    return String.format("{%c at (col: %d, row: %d) | isWhite->%b}", type,coord[0],coord[1],isWhite);
  }
}

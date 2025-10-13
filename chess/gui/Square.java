package chess.gui;

import chess.pieces.Piece;

public class Square {
  public int row, col;
  public Piece piece=null;

  public Square(int col, int row){
    this.row = row;
    this.col = col;
  }

  public Square(int col, int row, Piece piece){
    this.row = row;
    this.col = col;
    this.piece = piece;
  }

  public boolean isEmpty() {
    return piece == null;
  }

  @Override
  public String toString() {
    return String.format("Square: {col: %d, row: %d, piece: %s}", col, row, piece==null ? "null":piece);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) { return true; }
    if (!(o instanceof Square)) { return false; }

    Square s = (Square) o;
    return Integer.compare(row, s.row)==0 && Integer.compare(col, s.col)==0;
  }
}

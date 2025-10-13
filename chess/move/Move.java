package chess.move;

import chess.gui.Square;

public class Move {
  public Square initial, target;
  public boolean isWhite;
  public String special="";
  public int score;
  public boolean isCheck = false;

  public Move(Square initial, Square target) {
    this.initial = initial;
    this.target = target;
    if (!initial.isEmpty())
      this.isWhite = initial.piece.isWhite;
  }

  @Override
  public String toString() {
    return String.format("%d%d%d%d%c",initial.col,initial.row,target.col,target.row,getChar());
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) { return true; }
    if (!(o instanceof Move)) { return false; }

    Move m = (Move) o;
    return this.initial==m.initial && this.target==m.target;
  }

  private char getChar(){
    if (target.piece == null) { return ' '; }
    switch (target.piece.name) {
      case "pawn" : return 'p';
      case "knight" : return 'n';
      case "bishop" : return 'b';
      case "rook" : return 'r';
      case "queen" : return 'q';
      case "king" : return 'k';
      default : return ' ';
    }
  }
}

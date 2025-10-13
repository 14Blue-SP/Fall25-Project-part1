package chess.pieces;

import chess.move.Move;

public class Knight extends Piece {
  public Knight(boolean isWhite) {
    super("knight", isWhite, 300);
    spriteIndex = 3;
  }

  @Override
  public boolean isValidMove(Move move) {
    return Math.abs(move.initial.col-move.target.col) * Math.abs(move.initial.row-move.target.row)==2;
  }
}
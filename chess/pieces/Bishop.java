package chess.pieces;

import chess.move.Move;

public class Bishop extends Piece {
  public Bishop(boolean isWhite) {
    super("bishop", isWhite, 350);
  }

  @Override
  public boolean isValidMove(Move move) {
    return Math.abs(move.initial.col-move.target.col)==Math.abs(move.initial.row-move.target.row);
  }

  @Override
  public boolean pieceCollision(Move move) {
    return isDiagonalCollision(move);
  }
}
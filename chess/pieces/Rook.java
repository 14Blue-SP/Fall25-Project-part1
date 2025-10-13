package chess.pieces;

import chess.move.Move;

public class Rook extends Piece {
  public Rook(boolean isWhite) {
    super("rook", isWhite, 500);
  }

  @Override
  public boolean isValidMove(Move move) {
    return move.initial.col==move.target.col || move.initial.row==move.target.row;
  }

  @Override
  public boolean pieceCollision(Move move) {
    return isVerticalCollision(move) || isHorizontalCollision(move);
  }
}
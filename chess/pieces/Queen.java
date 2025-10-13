package chess.pieces;

import chess.move.Move;

public class Queen extends Piece {
  public Queen(boolean isWhite) {
    super("queen", isWhite, 900);
  }

  @Override
  public boolean isValidMove(Move move) {
    return move.initial.col==move.target.col || move.initial.row==move.target.row || Math.abs(move.initial.col-move.target.col)==Math.abs(move.initial.row-move.target.row);
  }

  @Override
  public boolean pieceCollision(Move move) {
    return isVerticalCollision(move) || isHorizontalCollision(move) || isDiagonalCollision(move);
  }
}
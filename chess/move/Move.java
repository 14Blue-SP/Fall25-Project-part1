package chess.move;

import chess.pieces.Piece;

public interface Move {
  void move(Piece piece, int newCol, int newRow);
}
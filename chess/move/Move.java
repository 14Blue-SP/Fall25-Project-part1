package chess.move;

import chess.pieces.Piece;
import chess.gui.BoardView;

public interface Move {
  void move(BoardView boardView, Piece piece, int newCol, int newRow);
}
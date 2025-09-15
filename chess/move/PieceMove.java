package chess.move;

import chess.gui.BoardView;
import chess.model.GameModel;
import chess.pieces.Piece;

public class PieceMove implements Move {
  GameModel gameModel;

  public int col;
  public int row;

  public Piece piece;
  public Piece capturedPiece;

  public PieceMove(GameModel gameModel){
    this.gameModel = gameModel;
  }

  @Override
  public void move(BoardView boardView, Piece piece, int col, int row) {
    // Implementation of the move method
    this.piece = piece;
    this.col = col;
    this.row = row;
    this.capturedPiece = boardView.getPieceAt(col, row);
  }
}

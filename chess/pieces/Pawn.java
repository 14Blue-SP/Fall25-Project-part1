package chess.pieces;

import chess.gui.BoardView;
import chess.model.GameModel;

public class Pawn extends Piece {
  public Pawn(BoardView board, int col, int row, boolean isWhite){
    super(col, row, isWhite);
    this.board = board;
    this.xPos = col * board.tileSize;
    this.yPos = row * board.tileSize;
    this.name = "Pawn";
    this.value = 1;
    this.spriteIndex = 5;
  }

  @Override
  public boolean isValidMove(int col, int row){
    int direction = this.isWhite ? -1 : 1;

    // standard move
    if(this.col == col && this.row + direction == row && GameModel.getInstance().getPieceAt(col, row) == null){
      return true;
    }
    // first double move
    if(this.isFirstMove && this.col == col && this.row + 2 * direction == row && GameModel.getInstance().getPieceAt(col, row) == null){
      return true;
    }
    // capture move
    if(Math.abs(this.col - col) == 1 && this.row + direction == row && GameModel.getInstance().getPieceAt(col, row) != null){
      return true;
    }
    // en passant
    if(GameModel.getInstance().enPassantSquare == GameModel.getInstance().getIndex(col, row)
    && Math.abs(this.col - col) == 1 && this.row + direction == row){
      return true;
    }
    return false;
  }

  @Override
  public boolean pieceCollision(int col, int row){
    if(this.col == col){
      int start = Math.min(this.row, row)+1;
      int end = Math.max(this.row, row);
      for(int r = start; r < end; r++){
        if(GameModel.getInstance().getPieceAt(col, r) != null){
          return true;
        }
      }
    }
    return false;
  }
}
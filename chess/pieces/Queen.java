package chess.pieces;

import chess.gui.BoardView;
import chess.model.GameModel;

public class Queen extends Piece {
  public Queen(BoardView board, int col, int row, boolean isWhite){
    super(col, row, isWhite);
    this.board = board;
    this.xPos = col * board.tileSize;
    this.yPos = row * board.tileSize;
    this.name = "Queen";
    this.value = 9;
    this.spriteIndex = 1;
  }

  @Override
  public boolean isValidMove(int col, int row){
    return this.col == col || this.row == row || Math.abs(this.col - col) == Math.abs(this.row - row);
  }

  @Override
  public boolean pieceCollision(int col, int row){
    // check horizontal and vertical paths for pieces
    if(this.col == col){
      int start = Math.min(this.row, row)+1;
      int end = Math.max(this.row, row);
      for(int r = start; r < end; r++){
        if(GameModel.getInstance().getPieceAt(col, r) != null){
          return true; 
        }
      }
    }
    if(this.row == row){
      int start = Math.min(this.col, col)+1;
      int end = Math.max(this.col, col);
      for(int c = start; c < end; c++){
        if(GameModel.getInstance().getPieceAt(c, row) != null){
          return true;
        }
      }
    }
    // check diagonal paths for pieces
    if(Math.abs(this.col - col) == Math.abs(this.row - row)){
      int colStep = (col - this.col) / Math.abs(col - this.col);
      int rowStep = (row - this.row) / Math.abs(row - this.row);
      int steps = Math.abs(col - this.col);
      for(int i = 1; i < steps; i++){
        if(GameModel.getInstance().getPieceAt(this.col + i * colStep, this.row + i * rowStep) != null){
          return true;
        }
      }
    }
    return false;
  }
}

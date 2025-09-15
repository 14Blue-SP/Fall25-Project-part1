package chess.pieces;

import chess.gui.BoardView;
import chess.model.GameModel;

public class Bishop extends Piece {
  public Bishop(BoardView board, int col, int row, boolean isWhite){
    super(col, row, isWhite);
    this.board = board;
    this.xPos = col * board.tileSize;
    this.yPos = row * board.tileSize;
    this.name = "Bishop";
    this.value = 3;
    this.spriteIndex = 2;
  }

  @Override
  public boolean isValidMove(int col, int row){
    return Math.abs(this.col - col) == Math.abs(this.row - row);
  }

  @Override
  public boolean pieceCollision(int col, int row){
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
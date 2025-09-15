package chess.pieces;

import chess.gui.BoardView;

public class Knight extends Piece {
  public Knight(BoardView board, int col, int row, boolean isWhite){
    super(col, row, isWhite);
    this.xPos = col * board.tileSize;
    this.yPos = row * board.tileSize;
    this.name = "Knight";
    this.value = 3;
    this.spriteIndex = 3;
  }

  @Override
  public boolean isValidMove(int col, int row){
    return Math.abs(this.col - col) * Math.abs(this.row - row) == 2;
  }
}

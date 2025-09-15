package chess.pieces;

import chess.gui.BoardView;

public class King extends Piece {
  public King(BoardView board, int col, int row, boolean isWhite){
    super(col, row, isWhite);
    this.xPos = col * board.tileSize;
    this.yPos = row * board.tileSize;
    this.name = "King";
    this.value = Integer.MAX_VALUE;
    this.spriteIndex = 0;
  }

  @Override
  public boolean isValidMove(int col, int row){
    return Math.abs(this.col - col) <= 1 && Math.abs(this.row - row) <= 1;
  }
}

package chess.pieces;

import chess.gui.BoardView;

public class Bishop extends Piece {
  public Bishop(BoardView board, int col, int row, boolean isWhite){
    super(col, row, isWhite);
    this.xPos = col * board.tileSize;
    this.yPos = row * board.tileSize;
    this.name = "Bishop";
    this.value = 3;
    this.spriteIndex = 2;
  }
}

package chess.pieces;

import chess.gui.BoardView;

public class Queen extends Piece {
  public Queen(BoardView board, int col, int row, boolean isWhite){
    super(col, row, isWhite);
    this.xPos = col * board.tileSize;
    this.yPos = row * board.tileSize;
    this.name = "Queen";
    this.value = 9;
    this.spriteIndex = 1;
  }
}

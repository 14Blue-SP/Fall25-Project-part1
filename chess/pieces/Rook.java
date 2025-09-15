package chess.pieces;

import chess.gui.BoardView;

public class Rook extends Piece {
  public Rook(BoardView board, int col, int row, boolean isWhite){
    super(col, row, isWhite);
    this.xPos = col * board.tileSize;
    this.yPos = row * board.tileSize;
    this.name = "Rook";
    this.value = 3;
    this.spriteIndex = 4;
  }
}

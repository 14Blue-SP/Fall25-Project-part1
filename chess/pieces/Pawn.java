package chess.pieces;

import chess.gui.BoardView;

public class Pawn extends Piece {
  public Pawn(BoardView board, int col, int row, boolean isWhite){
    super(col, row, isWhite);
    this.xPos = col * board.tileSize;
    this.yPos = row * board.tileSize;
    this.name = "Pawn";
    this.value = 1;
    this.spriteIndex = 5;
  }
}

package chess.pieces;

import java.awt.Graphics;

import chess.gui.BoardView;
import chess.model.GameModel;

public class Piece {
  public BoardView board;
  public int col, row;
  public int xPos, yPos;

  public boolean isWhite;
  public String name;
  public int value;

  public boolean isFirstMove = true;

  int spriteIndex;

  public Piece(int col, int row, boolean isWhite){
    this.col = col;
    this.row = row;
    this.isWhite = isWhite;
  }

  public boolean isValidMove(int col, int row){
    return true;
  }

  public boolean pieceCollision(int col, int row){
    return false;
  }

  @Override
  public String toString(){
    return (isWhite ? "White" : "Black") + " " + name;
  }

  public void draw(Graphics gfx, int tileSize){
    int scale = GameModel.getInstance().imageScale;
    gfx.drawImage(GameModel.getInstance().pieceImage, xPos, yPos, xPos+tileSize, yPos+tileSize, 
    spriteIndex*scale, (isWhite ? 0:1)*scale,
    (spriteIndex+1)*scale, ((isWhite ? 0:1)+1)*scale,
    null);
  }
}
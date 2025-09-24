package chess.move;

import chess.Main;

public class PawnMove extends Move {

  public PawnMove(int col, int row, int newCol, int newRow) {
    super(col, row, newCol, newRow);
  }
  
  @Override
   public boolean isValidMove(){
    int direction = isWhite ? -1 : 1;

    //Promotion
    if(coords[3] == (isWhite?0:7)){
      special = "=";
    }

    //Standard Move
    if(coords[0]==coords[2] && coords[1]+direction == coords[3] && Main.GM.getBoard()[Main.GM.getIndex(coords[2], coords[3])]==' '){
      return true;
    }

    //First Double Move
    if(coords[1]==(isWhite?6:1) && coords[0]==coords[2] && coords[1]+2*direction == coords[3] && Main.GM.getBoard()[Main.GM.getIndex(coords[2], coords[3])]==' ' && Main.GM.getBoard()[Main.GM.getIndex(coords[2], coords[1]+direction)]==' '){
      return true;
    }

    //Capture
    if(Math.abs(coords[0]-coords[2])==1 && coords[1]+direction==coords[3] && Main.GM.getBoard()[Main.GM.getIndex(coords[2], coords[3])]!=' '){
      return true;
    }
    //enPassant
    if(Main.GM.enPassantSquare==Main.GM.getIndex(coords[2], coords[3]) && Math.abs(coords[0]-coords[2])==1 && coords[1]+direction==coords[3]){
      special = "e.p.";
      return true;
    }
    return false;
   }
}

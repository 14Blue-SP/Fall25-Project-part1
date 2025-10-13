package chess.pieces;

import chess.model.GameModel;
import chess.move.Move;

public class Pawn extends Piece {
  public int dir;
  public Pawn(boolean isWhite) {
    dir = isWhite ? -1:1;
    super("pawn", isWhite, 100);
  }

  @Override
  public boolean isValidMove(Move move) {
    //Promotion
    if(move.target.row==((isWhite==GameModel.getInstance().playerIsWhite) ? 0:7)){
      move.special = "=";
    }

    // Standard Move
    if(move.initial.col==move.target.col && move.target.row==move.initial.row+dir && board.getElement(move.target.col, move.target.row).isEmpty()){
      return true;
    }

    // First Double Move
    if (move.initial.row==((isWhite==GameModel.getInstance().playerIsWhite) ? 6:1) && move.initial.col==move.target.col && move.target.row==move.initial.row+2*dir && board.getElement(move.target.col, move.target.row).isEmpty() && board.getElement(move.target.col, move.target.row-dir).isEmpty()) {
      return true;
    }

    // Capture
    if (Math.abs(move.initial.col-move.target.col)==1 && move.target.row==move.initial.row+dir && !board.getElement(move.target.col, move.target.row).isEmpty()) {
      return true;
    }

    //enPassant
    if(board.enPassant==move.target && move.initial.row!=((isWhite==GameModel.getInstance().playerIsWhite)?6:1) && Math.abs(move.initial.col-move.target.col)==1 && move.target.row==move.initial.row+dir){
      move.special="e.p.";
      return true;
    }
    return false;
  }
}
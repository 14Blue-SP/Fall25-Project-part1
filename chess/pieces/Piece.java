package chess.pieces;

import java.util.ArrayList;

import chess.model.BoardModel;
import chess.model.GameModel;
import chess.move.Move;

public class Piece {
  public BoardModel board = GameModel.getInstance().getBoardModel();
  public String name;
  public boolean isWhite;
  public int value, spriteIndex;
  public int[] pos = new int[2];
  public ArrayList<Move> moves = new ArrayList<>();

  public Piece(String name, boolean isWhite, int value) {
    this.name = name;
    this.isWhite = isWhite;
    this.value = (isWhite ? 1:-1)*value;
    this.spriteIndex = "KQBNRP".indexOf(Character.toUpperCase(name.charAt(0)));
  }

  public boolean isValidMove(Move move) {
    return true;
  }

  public boolean pieceCollision(Move move) {
    return false;
  }

  public boolean isVerticalCollision(Move move) {
    if (move.initial.col==move.target.col) {
      int start = Math.min(move.initial.row, move.target.row) +1;
      int end = Math.max(move.initial.row, move.target.row);
      for (int r=start; r<end; r++) {
        if (!board.getElement(move.initial.col, r).isEmpty()) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isHorizontalCollision(Move move) {
    if (move.initial.row==move.target.row) {
      int start = Math.min(move.initial.col, move.target.col) +1;
      int end = Math.max(move.initial.col, move.target.col);
      for (int c=start; c<end; c++) {
        if (!board.getElement(c, move.initial.row).isEmpty()) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isDiagonalCollision(Move move) {
    if (move.initial.col==move.target.col && move.initial.row==move.target.row) { return true; }
    if (Math.abs(move.initial.col-move.target.col)==Math.abs(move.initial.row-move.target.row)) {
      int colStep = (move.target.col-move.initial.col) / Math.abs(move.target.col-move.initial.col);
      int rowStep = (move.target.row-move.initial.row) / Math.abs(move.target.row-move.initial.row);
      int steps = Math.abs(move.target.col-move.initial.col);
      for (int i=1; i<steps; i++) {
        if (!board.getElement(move.initial.col+i*colStep, move.initial.row+i*rowStep).isEmpty()) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public String toString() {
    return String.format("%s %s|%d points",(isWhite ? "White":"Black"), name, value);
  }
}

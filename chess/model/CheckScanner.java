package chess.model;

import chess.gui.Square;
import chess.move.Move;
import chess.pieces.*;

public class CheckScanner {
  private boolean Alliance(Square s1, Square s2) {
    if (s1.isEmpty() || s2.isEmpty()) {
      return false;
    }
    return s1.piece.isWhite == s2.piece.isWhite;
  }

  public boolean isLegalMove(Move move) {
    if (Alliance(move.initial, move.target)){
      return false;
    }
    if (!move.initial.piece.isValidMove(move)) {
      return false;
    }
    if (move.initial.piece.pieceCollision(move)) {
      return false;
    }
    return true;
  }

  public boolean isSafeMove(Move move) {
    Square king;
    BoardModel board = GameModel.getInstance().getBoardModel();
    board.setElement(new Square(move.initial.col, move.initial.row));
    board.setElement(new Square(move.target.col, move.target.row, move.initial.piece));
    if (move.isWhite) {
      king = board.whiteKing;
    } else {
      king = board.blackKing;
    }
    if (move.initial.piece.name.equals("king")) {
      if (move.isWhite) {
        board.whiteKing = board.getElement(move.target.col, move.target.row);
      } else {
        board.blackKing = board.getElement(move.target.col, move.target.row);
      }
    }
    boolean isSafe = !isCheck(move.isWhite);
    board.setElement(move.initial);
    board.setElement(move.target);
    if (move.isWhite) {
      board.whiteKing = king;
    } else {
      board.blackKing = king;
    }
    return isSafe;
  }

  public boolean isCheck(boolean isWhite) {
    BoardModel board = GameModel.getInstance().getBoardModel();
    Square king = isWhite ? board.whiteKing:board.blackKing;
    
    // Check Diagonals
    for (int i=0; i<board.getBoard().length; i++) {
      Square square = board.getBoard()[i];
      if (!square.isEmpty()) {
        if (square.piece instanceof Bishop || square.piece instanceof Queen) {
          Move move = new Move(square, king);
          if (isLegalMove(move)) { return true; }
        }
      }
    }

    // Check Horizontals and Verticals
    for (int i=0; i<board.getBoard().length; i++) {
      Square square = board.getBoard()[i];
      if (!square.isEmpty()) {
        if (square.piece instanceof Rook || square.piece instanceof Queen) {
          Move move = new Move(square, king);
          if (isLegalMove(move)) { return true; }
        }
      }
    }

    // Check Knight
    for (int i=0; i<board.getBoard().length; i++) {
      Square square = board.getBoard()[i];
      if (!square.isEmpty()) {
        if (square.piece instanceof Knight) {
          Move move = new Move(square, king);
          if (isLegalMove(move)) { return true; }
        }
      }
    }

    // Check Pawn
    for (int i=0; i<board.getBoard().length; i++) {
      Square square = board.getBoard()[i];
      if (!square.isEmpty()) {
        if (square.piece instanceof Pawn) {
          Move move = new Move(square, king);
          if (isLegalMove(move)) { return true; }
        }
      }
    }

    // Check King
    for (int i=0; i<board.getBoard().length; i++) {
      Square square = board.getBoard()[i];
      if (!square.isEmpty()) {
        if (square.piece instanceof King) {
          if (king.equals(square)) {continue;}
          Move move = new Move(square, king);
          if (isLegalMove(move)) { return true; }
        }
      }
    }
    return false;
  }
}

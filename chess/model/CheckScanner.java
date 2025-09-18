package chess.model;

import chess.move.PieceMove;
import chess.pieces.Piece;

public class CheckScanner implements GameModelListener {
  private static CheckScanner INSTANCE = new CheckScanner();
  private boolean whiteInCheck = false;
  private boolean blackInCheck = false;

  public CheckScanner() {
    GameModel.getInstance().addListener(this);
  }

  public static CheckScanner getInstance() {
    return INSTANCE;
  }

  @Override
  public void gameStateChanged(){
    System.out.print(this.getClass().getSimpleName()+"--");
    System.out.println(new Throwable().getStackTrace()[0].getMethodName());
    scanForChecks();
    System.out.println("Is Check? " + whiteInCheck + " " + blackInCheck);
    System.out.println("Is Checkmate? " + isCheckmate(true) + " " + isCheckmate(false));
    
  }

  public void scanForChecks(){
    whiteInCheck = false;
    blackInCheck = false;

    // Check if white king is in check
    Piece king = GameModel.getInstance().getKing(true);
    assert king != null;
    for(Piece opponent : GameModel.getInstance().getPieces()){
      if(opponent.isWhite != king.isWhite){
        PieceMove move = new PieceMove(GameModel.getInstance());
        move.move(opponent, king.col, king.row);
        if(GameModel.getInstance().isLegalMove(move)){
          whiteInCheck = true;
        }
      }
    }
    // Check if black king is in check
    king = GameModel.getInstance().getKing(false);
    assert king != null;
    for(Piece opponent : GameModel.getInstance().getPieces()){
      if(opponent.isWhite != king.isWhite){
        PieceMove move = new PieceMove(GameModel.getInstance());
        move.move(opponent, king.col, king.row);
        if(GameModel.getInstance().isLegalMove(move)){
          blackInCheck = true;
        }
      }
    }
  }

  public boolean isCheckmate(boolean isWhite){
    if(isWhite && !whiteInCheck){
      return false;
    }
    if(!isWhite && !blackInCheck){
      return false;
    }

    // Check if the player has any legal moves to get out of check
    for(Piece piece : GameModel.getInstance().getPieces()){
      if(piece.isWhite == isWhite){
        for(int col = 0; col < 8; col++){
          for(int row = 0; row < 8; row++){
            PieceMove move = new PieceMove(GameModel.getInstance());
            move.move(piece, col, row);
            if(GameModel.getInstance().isLegalMove(move)){
              // Simulate the move
              int originalCol = piece.col;
              int originalRow = piece.row;
              Piece capturedPiece = move.capture;

              piece.col = col;
              piece.row = row;
              if(capturedPiece != null){
                GameModel.getInstance().getPieces().remove(capturedPiece);
              }

              // Check if still in check
              scanForChecks();
              boolean stillInCheck = isWhite ? whiteInCheck : blackInCheck;

              // Undo the move
              piece.col = originalCol;
              piece.row = originalRow;
              if(capturedPiece != null){
                GameModel.getInstance().getPieces().add(capturedPiece);
              }

              if(!stillInCheck){
                return false; // Found a legal move to get out of check
              }
            }
          }
        }
      }
    }
    return true; // No legal moves found, it's checkmate
  }

  public boolean willCheck(PieceMove move){
    Piece king = GameModel.getInstance().getKing(move.piece.isWhite);
    return false;
  }

  /*
  public boolean isInCheckAfterMove(PieceMove move){
    // Simulate the move
    int originalCol = move.piece.col;
    int originalRow = move.piece.row;
    Piece capturedPiece = move.capture;

    move.piece.col = move.col;
    move.piece.row = move.row;
    if(capturedPiece != null){
      GameModel.getInstance().getPieces().remove(capturedPiece);
    }

    // Check if in check
    scanForChecks();
    boolean inCheck = move.piece.isWhite ? whiteInCheck : blackInCheck;

    // Undo the move
    move.piece.col = originalCol;
    move.piece.row = originalRow;
    if(capturedPiece != null){
      GameModel.getInstance().getPieces().add(capturedPiece);
    }

    return inCheck;
  }

  /*
  public boolean willCheck(Piece piece, int col, int row){
    // Simulate the move
    int originalCol = piece.col;
    int originalRow = piece.row;
    Piece capturedPiece = GameModel.getInstance().getPieceAt(col, row);

    piece.col = col;
    piece.row = row;
    if(capturedPiece != null){
      GameModel.getInstance().getPieces().remove(capturedPiece);
    }

    // Check if in check
    scanForChecks();
    boolean inCheck = piece.isWhite ? whiteInCheck : blackInCheck;

    // Undo the move
    piece.col = originalCol;
    piece.row = originalRow;
    if(capturedPiece != null){
      GameModel.getInstance().getPieces().add(capturedPiece);
    }

    return inCheck;
  }
  */

  public boolean isCheck(boolean isWhite){
    return isWhite ? whiteInCheck : blackInCheck;
  }

  public boolean isWhiteInCheck(){
    return whiteInCheck;
  }

  public boolean isBlackInCheck(){
    return blackInCheck;
  }
}

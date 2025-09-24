package chess.model;

import java.util.Arrays;
import java.util.ListIterator;

import chess.move.*;
import chess.pieces.Piece;

public class CheckScanner {
  private GameModel GM;

  public CheckScanner(GameModel gameModel) {
    GM = gameModel;
  }
  
  private boolean Alliance(Character p1, Character p2) {
    if (p1 == ' ' || p2 == ' ') {
      return false;
    }
    return Character.isUpperCase(p1) == Character.isUpperCase(p2);
  }

  public boolean isLegalMove(Move move) {
    if (move.isWhite != GM.isWhiteTurn) {
      return false;
    }
    if (Alliance(move.piece, move.capture)) {
      return false;
    }
    if (!move.isValidMove()) {
      return false;
    }
    if (move.pieceCollision()) {
      return false;
    }
    return true;
  }

  public boolean isSafeMove(Move move) {
    Character piece = GM.getBoard()[GM.getIndex(move.coords[0], move.coords[1])];
    Character capture = GM.getBoard()[GM.getIndex(move.coords[2], move.coords[3])];
    GM.setBoard(move.coords[0], move.coords[1], ' ');
    GM.setBoard(move.coords[2], move.coords[3], piece);
    int tempPosition = GM.getIndex(move.coords[0], move.coords[1]);
    if(Character.toLowerCase(piece) == 'k'){
      if(move.isWhite){
        GM.whiteKingIndex = GM.getIndex(move.coords[2], move.coords[3]);
      } else {
        GM.blackKingIndex = GM.getIndex(move.coords[2], move.coords[3]);
      }
    }
    boolean isSafe = !isCheck(move.isWhite);
    GM.setBoard(move.coords[0], move.coords[1], piece);
    GM.setBoard(move.coords[2], move.coords[3], capture);
    if(Character.toLowerCase(piece) == 'k'){
      if(move.isWhite){
        GM.whiteKingIndex = tempPosition;
      } else {
        GM.blackKingIndex = tempPosition;
      }
    }
    return isSafe;
  }

  public boolean isCheck(boolean isWhite){
    int[] king;
    if(isWhite){
      king = GM.getCoordinates(GM.whiteKingIndex);
    } else {
      king = GM.getCoordinates(GM.blackKingIndex);
    }

    // check Diagonals
    for(int i=0; i<GM.getBoard().length; i++){
      int[] square = GM.getCoordinates(i);
      BishopMove move = new BishopMove(king[0],king[1],square[0],square[1]);
      if(isLegalMove(move)){
        Character capture = Character.toLowerCase(GM.getBoard()[GM.getIndex(move.coords[2], move.coords[3])]);
        if(capture=='b' || capture=='q'){
          return true;
        }
      }
    }

    // check Vertical and Horizontal
    for(int i=0; i<GM.getBoard().length; i++){
      int[] square = GM.getCoordinates(i);
      RookMove move = new RookMove(king[0],king[1],square[0],square[1]);
      if(isLegalMove(move)){
        Character capture = Character.toLowerCase(GM.getBoard()[GM.getIndex(move.coords[2], move.coords[3])]);
        if(capture=='r' || capture=='q'){
          return true;
        }
      }
    }

    // check Knight
    for(int i=0; i<GM.getBoard().length; i++){
      int[] square = GM.getCoordinates(i);
      KnightMove move = new KnightMove(king[0],king[1],square[0],square[1]);
      if(isLegalMove(move)){
        Character capture = Character.toLowerCase(GM.getBoard()[GM.getIndex(move.coords[2], move.coords[3])]);
        if(capture=='n'){
          return true;
        }
      }
    }

    // check Pawn
    for(int i=0; i<GM.getBoard().length; i++){
      int[] square = GM.getCoordinates(i);
      PawnMove move = new PawnMove(king[0],king[1],square[0],square[1]);
      if(isLegalMove(move)){
        Character capture = Character.toLowerCase(GM.getBoard()[GM.getIndex(move.coords[2], move.coords[3])]);
        if(capture=='p'){
          return true;
        }
      }
    }

    // check King
    for(int i=0; i<GM.getBoard().length; i++){
      int[] square = GM.getCoordinates(i);
      KingMove move = new KingMove(king[0],king[1],square[0],square[1]);
      if(isLegalMove(move)){
        Character capture = Character.toLowerCase(GM.getBoard()[GM.getIndex(move.coords[2], move.coords[3])]);
        if(capture=='k'){
          return true;
        }
      }
    }
    return false;
  }

  public void scanForChecks(){
    GM.isWhiteCheck=false;
    GM.isBlackCheck=false;
    int[] king;

    // Check if white King is in Check
    king = GM.getCoordinates(GM.whiteKingIndex);
    for(Piece opponent : GM.getPieces()){
      Move move = new Move(opponent.coord[0], opponent.coord[1], king[0], king[1]);

      ListIterator<Move> listIterator = GM.PossibleMoves.listIterator();
      while (listIterator.hasNext()) {
        Move m = listIterator.next();
        if (Arrays.equals(m.coords, move.coords)){
          GM.isWhiteCheck = true; return;
        }
      }
    }
    // Check if white King is in Check
    king = GM.getCoordinates(GM.blackKingIndex);
    for(Piece opponent : GM.getPieces()){
      Move move = new Move(opponent.coord[0], opponent.coord[1], king[0], king[1]);

      ListIterator<Move> listIterator = GM.PossibleMoves.listIterator();
      while (listIterator.hasNext()) {
        Move m = listIterator.next();
        if (Arrays.equals(m.coords, move.coords)){
          GM.isBlackCheck = true; return;
        }
      }
    }
  }
}

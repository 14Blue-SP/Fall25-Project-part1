package chess.model;

import java.awt.Image;
import java.util.List;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;

import chess.move.PieceMove;
import chess.pieces.Piece;
import chess.pieces.Queen;

public class GameModel {
  private static GameModel INSTANCE = new GameModel();
  private String[] board = new String[64];
  private ArrayList<Piece> pieces = new ArrayList<>();
  private List<GameModelListener> listeners = new ArrayList<>();
  private Stack<String> moveHistory = new Stack<>();

  public Image pieceImage = new ImageIcon("chess/pieces.png").getImage();
  public int imageScale = pieceImage.getWidth(null) / 6;

  public int enPassantSquare = -1;

  public static GameModel getInstance(){
    return INSTANCE;
  }

  public void addListener(GameModelListener listener){
    listeners.add(listener);
  }

  public void notifyListeners(){
    for(GameModelListener listener : listeners){
      listener.gameStateChanged();
    }
  }

  public void newGame(){
    // Initialize board and pieces for a new game
    clearBoard();
    for(int c=0; c < 2; c++){
      // Place pawns
      for(int i=0;i<8;i++){
        setPiece(getIndex(i, c%2==0 ? 6:1), c%2==0 ? "P":"p");
      }
      // Place rooks
      setPiece(getIndex(0, c%2==0 ? 7:0), c%2==0 ? "R":"r");
      setPiece(getIndex(7, c%2==0 ? 7:0), c%2==0 ? "R":"r");
      // Place knights
      setPiece(getIndex(1, c%2==0 ? 7:0), c%2==0 ? "N":"n");
      setPiece(getIndex(6, c%2==0 ? 7:0), c%2==0 ? "N":"n");
      // Place bishops
      setPiece(getIndex(2, c%2==0 ? 7:0), c%2==0 ? "B":"b");
      setPiece(getIndex(5, c%2==0 ? 7:0), c%2==0 ? "B":"b");
      // Place queens
      setPiece(getIndex(3, c%2==0 ? 7:0), c%2==0 ? "Q":"q");
      // Place kings
      setPiece(getIndex(4, c%2==0 ? 7:0), c%2==0 ? "K":"k");
    }
  }

  public void clearBoard(){
    Arrays.fill(board, " ");
    moveHistory.clear();
  }

  public String[] getBoard(){
    return board;
  }

  public void setPiece(int index, String piece){
    board[index] = piece;
  }

  public int getIndex(int col, int row){
    return row*8 + col;
  }

  public ArrayList<Piece> getPieces(){
    return pieces;
  }

  public Piece getKing(boolean isWhite){
    for(Piece piece : pieces){
      if(piece.name.equals("King") && piece.isWhite == isWhite){
        return piece;
      }
    }
    return null;
  }

  public void printBoard(){
    System.out.println();
    for(int i = 0; i < board.length; i+=8){
      System.out.println((8-(i/8))+Arrays.toString(Arrays.copyOfRange(board, i, i+8)));
    } 
    System.out.println("  a, b, c, d, e, f, g, h");
    System.out.println();
  }

  public boolean isLegalMove(PieceMove move){
    if(Alliance(move.piece, move.capture)){
      return false;
    }
    if(!move.piece.isValidMove(move.col, move.row)){
      return false;
    }
    if(move.piece.pieceCollision(move.col, move.row)){
      return false;
    }
    if(CheckScanner.getInstance().willCheck(move)){
      //return false;
    }
    return true;
  }

  public void makeMove(PieceMove move){
    String piece = getAbbrivation(move.piece.name);
    
    if(move.piece.name.equals("Pawn")){
      // en passant
      int direction = move.piece.isWhite ? 1:-1;
      if(getIndex(move.col, move.row)== enPassantSquare){
        move.capture = getPieceAt(move.col, move.row + direction);
        setPiece(getIndex(move.capture.col, move.capture.row), " ");
        move.specialMove = "ep";
      }
      if(Math.abs(move.piece.row - move.row) == 2){
        enPassantSquare = getIndex(move.col, move.row + direction);
      } else {
        enPassantSquare = -1;
      }

      // Promotion -- always promote to queen (need to implement choice later)
      int promotionRank = move.piece.isWhite ? 0:7;
      if(move.row == promotionRank){
        pieces.add(new Queen(move.piece.board, move.col, move.row, move.piece.isWhite));
        capture(move.piece);
        piece = "Q";
        move.specialMove = "prom";
      }
    }
    setPiece(getIndex(move.piece.col, move.piece.row), " ");
    setPiece(getIndex(move.col, move.row), move.piece.isWhite ? piece.toUpperCase():piece.toLowerCase());
    recordMove(move);
    move.piece.col = move.col;
    move.piece.row = move.row;
    if(move.piece.isFirstMove){
      move.piece.isFirstMove = false;
    }
    capture(move.capture);
    notifyListeners();
    System.out.println(moveHistory.peek());
    printBoard();
  }

  private void capture(Piece piece){
    pieces.remove(piece);
  }

  private boolean Alliance(Piece p1, Piece p2){
    if(p1==null || p2 == null){
      return false;
    }
    return p1.isWhite == p2.isWhite;
  }

  private String getAbbrivation(String name){
    switch(name.toLowerCase()){
      case "pawn": return "P";
      case "rook": return "R";
      case "knight": return "N";
      case "bishop": return "B";
      case "queen": return "Q";
      case "king": return "K";
      default: return " ";
    }
  }

  public Piece getPieceAt(int file, int rank){
    for(Piece piece : pieces){
      if(piece.col == file && piece.row == rank){
        return piece;
      }
    }
    return null;
  }

  public String getSquare(int col, int row){
    return ""+(char)('a'+col)+(8-row);
  }

  private void recordMove(PieceMove move){
    String moveNotation = "";
    // future: break into separate stacks for white and black
    moveNotation += move.piece.isWhite ? "White ": "Black ";

    if(!move.piece.name.equals("Pawn")){
      moveNotation += getAbbrivation(move.piece.name);
    }
    if(move.capture != null){
      if(move.piece.name.equals("Pawn")){
        moveNotation += (char)('a'+move.piece.col);
      }
      moveNotation += "x";
    }
    moveNotation += getSquare(move.col, move.row);

    // en passant
    if(move.specialMove.equals("ep")){
      moveNotation += " e.p.";
    }

    // promotion -- =R, =B, =N (not implemented)
    if(move.specialMove.equals("prom")){
      moveNotation += "=Q"; // always promote to queen for simplicity
    }

    // 0-0 for kingside castle
    // 0-0-0 for queenside castle (not implemented)
    // + for check, # for checkmate (not implemented)
    moveHistory.push(moveNotation);
  }
}
package chess.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chess.move.Move;
import chess.pieces.Piece;

public class GameModel {
  private static GameModel INSTANCE = new GameModel();
  private static MoveGenerator MG;
  public int files=8, ranks=8;
  public int whiteKingIndex, blackKingIndex;
  public int enPassantSquare = -1;

  public Piece selectedPiece=null;
  public Move latestMove = null;

  public boolean isWhiteTurn=true;
  public boolean isWhiteCheck=false, isBlackCheck=false;
  public int GameOver = -1; 

  private Character[] chessBoard = new Character[files*ranks];
  private ArrayList<Piece> pieces = new ArrayList<>();
  public List<Move> PossibleMoves = new ArrayList<>();
  
  //Method to get static Instance of class
  public static GameModel getInstance(){
    MG = new MoveGenerator(INSTANCE);
    return INSTANCE;
  }

  public void getState(){
    MG.CS.scanForChecks();
    isWhiteTurn = !isWhiteTurn;
    System.out.printf("White in Check: %b -  Black in Check: %b%n", isWhiteCheck, isBlackCheck);
    System.out.println("White turn? "+isWhiteTurn);
    //System.out.println(PossibleMoves);
    if (PossibleMoves.size() == 0){
      System.out.println("O moves avalible - " + isWhiteTurn);
      System.out.println("Game Over");
      if (isWhiteCheck) {
        System.out.println("Black Wins");
      } else if (isBlackCheck) {
        System.out.println("White Wins");
      } else {
        System.out.println("Draw");
      }
      System.exit(0);
    }
  }
  
  //#region Board Methods
  private void clearBoard(){
    Arrays.fill(chessBoard, ' ');
  }

  public void printBoard(){
    System.out.println();
    for(int i=0; i<chessBoard.length; i+=8){
      System.out.println(((i/ranks))+Arrays.toString(Arrays.copyOfRange(chessBoard, i, i+files)));
    }
    String str = "  ";
    for(int i=0; i<files; i++){
      str=str+i+"  ";
    }
    System.out.println(str+"\n");
    //System.out.println("  a, b, c, d, e, f, g, h");
    makePieces();
    //getState();
  }

  public Character[] getBoard(){
    return chessBoard;
  }

  public int getIndex(int file, int rank){
    return rank*ranks + file;
  }

  public int[] getCoordinates(int index){
    return new int[] {index % files, index / ranks};
  }

  public void setBoard(int file, int rank, Character piece){
    chessBoard[getIndex(file,rank)] = piece;
  }

  public void newStandardChessBoard(){
    clearBoard();
    for(int c=0; c < 2; c++){
      // Place pawns
      for(int i=0;i<files;i++){
        setBoard(i, c%2==0 ? 6:1, c%2==0 ? 'P':'p');
      }
      // Place rooks
      setBoard(0, c%2==0 ? 7:0, c%2==0 ? 'R':'r');
      setBoard(7, c%2==0 ? 7:0, c%2==0 ? 'R':'r');
      // Place knights
      setBoard(1, c%2==0 ? 7:0, c%2==0 ? 'N':'n');
      setBoard(6, c%2==0 ? 7:0, c%2==0 ? 'N':'n');
      // Place bishops
      setBoard(2, c%2==0 ? 7:0, c%2==0 ? 'B':'b');
      setBoard(5, c%2==0 ? 7:0, c%2==0 ? 'B':'b');
      // Place queens
      setBoard(3, c%2==0 ? 7:0, c%2==0 ? 'Q':'q');
      // Place kings
      setBoard(4, c%2==0 ? 7:0, c%2==0 ? 'K':'k');
      if(c%2==0){
        whiteKingIndex = getIndex(4,7);
      } else {
        blackKingIndex = getIndex(4,0);
      }
    }
    getPossibleMoves();
    printBoard();
    makePieces();
  }
  //#endregion

  //#region Piece Methods
  public void makePieces(){
    pieces.clear();
    for(int i=0; i<chessBoard.length; i++){
      if(!chessBoard[i].equals(' ')){
        pieces.add(new Piece(getCoordinates(i), chessBoard[i]));
      }
    }
  }

  public ArrayList<Piece> getPieces(){
    return pieces;
  }

  public Piece getPiece(int file, int rank){
    for(Piece p : pieces){
      if(p.coord[0]==file && p.coord[1]==rank){
        return p;
      }
    }
    return null;
  }
  //#endregion

  //#region Move Methods
  public void makeMove(Move move){
    setBoard(move.coords[2], move.coords[3], move.piece);
    setBoard(move.coords[0], move.coords[1], ' ');
    if(Character.toLowerCase(move.piece)=='k'){
      if(move.isWhite){
        whiteKingIndex = getIndex(move.coords[2], move.coords[3]);
      } else {
        blackKingIndex = getIndex(move.coords[2], move.coords[3]);
      }
    }
  }

  public void undoMove(Move move){
    setBoard(move.coords[2], move.coords[3], move.capture);
    setBoard(move.coords[0], move.coords[1], move.piece);
    if(Character.toLowerCase(move.piece)=='k'){
      if(move.isWhite){
        whiteKingIndex = getIndex(move.coords[0], move.coords[1]);
      } else {
        blackKingIndex = getIndex(move.coords[0], move.coords[1]);
      }
    }
  }

  public void MakeBoardMove(Move move){
    if(Character.toLowerCase(move.piece) == 'p'){
      // en Passant
      int direction = move.isWhite ? 1:-1;
      if(getIndex(move.coords[2], move.coords[3]) == enPassantSquare){
        move.capture = getBoard()[getIndex(move.coords[2], move.coords[3]+direction)];
        setBoard(move.coords[2], move.coords[3]+direction, ' ');
      }
      if(Math.abs(move.coords[1]-move.coords[3]) == 2){
        enPassantSquare = getIndex(move.coords[2], move.coords[3]+direction);
      } else {
        enPassantSquare = -1;
      }

      if(move.coords[3] == (move.isWhite?0:7)){
        move.special = "=";
      }

      // Promotion
      if(move.special=="="){
        System.out.println("Promotion Choice?"); // methond to pick promotion piece
        move.special = move.special+"Q";
        Character promPiece = move.special.charAt(move.special.length()-1);
        if(move.isWhite){
          promPiece=Character.toUpperCase(promPiece);
        } else {
          promPiece=Character.toLowerCase(promPiece);
        }
        move.piece = promPiece;
      }
    }
    makeMove(move);
    //record Move
    printBoard();
    getPossibleMoves();
    getState();
  }

  public void UndoBoardMove(Move move){
    if(Character.toLowerCase(move.piece) == 'p'){
      // en Passant
      if(move.special.equals("e.p.")){
        int direction = move.isWhite ? 1:-1;
        setBoard(move.coords[2], move.coords[3], ' ');
        enPassantSquare = getIndex(move.coords[2], move.coords[1]-direction);
        move.coords[3]+=direction;
      }
    }
    //Promotion
    if (move.special != "" && move.special.charAt(0) == '=') {
      move.piece=move.isWhite ? 'P':'p';
    }
    undoMove(move);
    //record Move
    printBoard();
    getPossibleMoves();
    getState();
  }

  public void getPossibleMoves(){
    List<Move> list = new ArrayList<>();
    for (int i=0; i<chessBoard.length; i++){
      switch (Character.toLowerCase(chessBoard[i])) {
        case 'p' : list.addAll(MG.possiblePawn(i)); break;
        case 'n' : list.addAll(MG.possibleKnight(i)); break;
        case 'r' : list.addAll(MG.possibleRook(i)); break;
        case 'b' : list.addAll(MG.possibleBishop(i)); break;
        case 'q' : list.addAll(MG.possibleQueen(i)); break;
        case 'k' : list.addAll(MG.possibleKing(i)); break;
      }
    }
    PossibleMoves = list;
  }
  //#endregion
  //
}

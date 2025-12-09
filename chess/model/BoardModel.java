package chess.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import chess.Const;
import chess.gui.Square;
import chess.move.Move;
import chess.pieces.*;

public class BoardModel {
  public CheckScanner CS = new CheckScanner();
  private Square[] board = new Square[Const.ROWS*Const.COLS];

  public Square whiteKing, blackKing, enPassant=null;
  public boolean[] castle = {true, true, true, true};

  //#region Board Methods
  public Square[] getBoard() {
    return board;
  }

  public int getIndex(int col, int row){
    return row*Const.ROWS + col;
  }
  public int[] getCoordinates(int index){
    return new int[] {index%Const.COLS, index/Const.ROWS};
  }

  public Square getElement(int col, int row){
    return board[getIndex(col, row)];
  }
  public void setElement(Square square){
    board[getIndex(square.col, square.row)] = square;
  }

  public void clearBoard() {
    for (int i=0; i<board.length; i++) {
      int[] coords = getCoordinates(i);
      board[i] = new Square(coords[0], coords[1]);
    }
  }

  public void newStandardChessBoard() {
    clearBoard();
    for(int c=0; c < 2; c++){
      boolean isWhite = c%2==0;
      // Place pawns
      for(int i=0;i<Const.COLS;i++){
        setElement(new Square(i, isWhite? 6:1, new Pawn(isWhite)));
      }
      // Place rooks
      setElement(new Square(0, isWhite? 7:0, new Rook(isWhite)));
      setElement(new Square(7, isWhite? 7:0, new Rook(isWhite)));
      // Place knights
      setElement(new Square(1, isWhite? 7:0, new Knight(isWhite)));
      setElement(new Square(6, isWhite? 7:0, new Knight(isWhite)));
      // Place bishops
      setElement(new Square(2, isWhite? 7:0, new Bishop(isWhite)));
      setElement(new Square(5, isWhite? 7:0, new Bishop(isWhite)));
      // Place queens
      setElement(new Square(3, isWhite? 7:0, new Queen(isWhite)));
      // Place kings
      setElement(new Square(4, isWhite? 7:0, new King(isWhite)));
    }
    whiteKing = getElement(4, 7);
    blackKing = getElement(4, 0);

    castle = new boolean[] {true, true, true, true};
    //printBoard();
  }

  public void printBoard() {
    System.out.println("");
    for (int i=0; i<board.length; i+=Const.COLS) {
      char[] arr = new char[Const.COLS];
      for (int j=0; j<arr.length; j++) {
        Square s = board[i+j];
        if (s.isEmpty()) {arr[j]=' '; continue;}
        if(s.piece instanceof Pawn) {arr[j]= 'p'; }
        if(s.piece instanceof Knight) {arr[j]= 'n'; }
        if(s.piece instanceof Bishop) {arr[j]= 'b'; }
        if(s.piece instanceof Rook) {arr[j]= 'r'; }
        if(s.piece instanceof Queen) {arr[j]= 'q'; }
        if(s.piece instanceof King) {arr[j]= 'k'; }
        if (s.piece.isWhite) {arr[j]=Character.toUpperCase(arr[j]);}
      }
      System.out.println((i/Const.ROWS) + Arrays.toString(arr));
    }
  }

  public void flipBoard() {
    int start = 0;
    int end = board.length-1;
    
    while (start < end) {
      Square temp = board[start];
      board[start] = board[end];
      int[] coords = getCoordinates(start);
      board[start].col = coords[0];
      board[start].row = coords[1];
      board[end] = temp;
      coords = getCoordinates(end);
      board[end].col = coords[0];
      board[end].row = coords[1];
      start++;
      end--;
    }
    for (Square square : board) {
      if (!square.isEmpty()) {
        if (square.piece instanceof Pawn) {
          Pawn p = (Pawn)square.piece;
          p.dir *= -1;
        }
      }
    }
    whiteKing = getElement(3, 0);
    blackKing = getElement(3, 7);
  }

  //#endregion

  //#region Move Methods
  public void findMoves(Square square) {
    for (int i=0; i<board.length; i++) {
      Square target = board[i];
      Move move = new Move(square, target);
      if(CS.isLegalMove(move) && CS.isSafeMove(move)) {
        if (square.piece instanceof Pawn) {
          if (move.special.startsWith("=")) {
            String prom = "QRNB";
            for (int j=0; j<prom.length(); j++){
              move.special = "="+prom.charAt(j);
              square.piece.moves.add(move);
            }
            continue;
          }
        }
        square.piece.moves.add(move);
      }
    }
  }

  public ArrayList<Move> getPossibleMoves(boolean isWhite) {
    ArrayList<Move> list = new ArrayList<>();
    for (int i=0; i<getBoard().length; i++) {
      Square square = getBoard()[i];
      if (!square.isEmpty() && square.piece.isWhite==isWhite) {
        findMoves(square);
        list.addAll(square.piece.moves);
        square.piece.moves.clear();
      }
    }
    return list;
  }

  public void makeMove(Move move) {
    // Pawn special move cases
    if (move.initial.piece instanceof Pawn) {
      if (!(move.initial.piece instanceof Pawn)) {return;}
      Pawn p = (Pawn)(move.initial.piece);
      int direction = p.dir;
      // enPassant
      if(move.special.equals("e.p.")){
        move.target.piece = getElement(move.target.col, move.target.row-direction).piece;
        getElement(move.target.col, move.target.row-direction).piece=null;
      }
      // Set enPassant square
      if(Math.abs(move.target.row-move.initial.row)==2){
        enPassant = getElement(move.target.col, move.target.row-direction);
      } else {
        enPassant = null;
      }
      if (move.special.startsWith("=")) {
        Piece promPiece=null;
        char promChar = move.special.charAt(move.special.length()-1);
        switch (promChar) {
          case 'Q': promPiece=new Queen(move.isWhite); break;
          case 'N': promPiece=new Knight(move.isWhite); break;
          case 'B': promPiece=new Bishop(move.isWhite); break;
          case 'R': promPiece=new Rook(move.isWhite); break;
        }
        promPiece.moves = move.initial.piece.moves;
        move.initial.piece = promPiece;
      }
    }

    // update Board
    setElement(new Square(move.initial.col, move.initial.row));
    setElement(new Square(move.target.col, move.target.row, move.initial.piece));
    move.initial.piece.moves.clear();

    // Update king index
    if (move.initial.piece instanceof King) {
      if (move.isWhite) {
        whiteKing = getElement(move.target.col, move.target.row);
        castle[0]=false;
        castle[1]=false;
      } else {
        blackKing = getElement(move.target.col, move.target.row);
        castle[2]=false;
        castle[3]=false;
      }
    }

    // Castling
    boolean playerIsWhite = GameModel.getInstance().playerIsWhite;
    if(move.special.equals("0-0")){
      Square rook = getElement(playerIsWhite ? 7:0, move.initial.row);
      getElement(move.target.col-(playerIsWhite ? 1:-1), move.target.row).piece=rook.piece;
      rook.piece=null;
    } else if(move.special.equals("0-0-0")){
      Square rook = getElement(playerIsWhite ? 0:7, move.initial.row);
      getElement(move.target.col+(playerIsWhite ? 1:-1), move.target.row).piece=rook.piece;
      rook.piece=null;
    }

    // Update Rook castle rights
    if (move.initial.piece instanceof Rook) {
      if (move.isWhite) {
        if (move.initial.col==(playerIsWhite ? 0:7) && move.initial.row==(playerIsWhite ? 7:0)) {
          if (playerIsWhite) {castle[0]=false;}
          else {castle[1]=false;}
        }
        if (move.initial.col==(playerIsWhite ? 7:0) && move.initial.row==(playerIsWhite ? 7:0)) {
          if (playerIsWhite) {castle[1]=false;}
          else {castle[0]=false;}
        }
      } else {
        if (move.initial.col==(playerIsWhite ? 0:7) && move.initial.row==(playerIsWhite ? 0:7)) {
          if (playerIsWhite) {castle[2]=false;}
          else {castle[3]=false;}
        }
        if (move.initial.col==(playerIsWhite ? 7:0) && move.initial.row==(playerIsWhite ? 0:7)) {
          if (playerIsWhite) {castle[3]=false;}
          else {castle[2]=false;}
        }
      }
    }
    if (move.target.piece instanceof Rook) {
      if (!move.isWhite) {
        if (move.target.col==(playerIsWhite ? 0:7) && move.target.row==(playerIsWhite ? 7:0)) {
          if (playerIsWhite) {castle[0]=false;}
          else {castle[1]=false;}
        }
        if (move.target.col==(playerIsWhite ? 7:0) && move.target.row==(playerIsWhite ? 7:0)) {
          if (playerIsWhite) {castle[1]=false;}
          else {castle[0]=false;}
        }
      } else {
        if (move.target.col==(playerIsWhite ? 0:7) && move.target.row==(playerIsWhite ? 0:7)) {
          if (playerIsWhite) {castle[2]=false;}
          else {castle[3]=false;}
        }
        if (move.target.col==(playerIsWhite ? 7:0) && move.target.row==(playerIsWhite ? 0:7)) {
          if (playerIsWhite) {castle[3]=false;}
          else {castle[2]=false;}
        }
      }
    }
  }

  public void undoMove(Move move){
    // Promotion
    if(move.special.startsWith("=")){
      move.initial.piece=new Pawn(move.isWhite);
      if (!GameModel.getInstance().playerIsWhite) {
        Pawn p = (Pawn)(move.initial.piece);
        p.dir*=-1;
      }
    }

    // update Board
    setElement(move.initial);
    setElement(move.target);

    // Update king index
    if (move.initial.piece instanceof King) {
      if (move.isWhite) {
        whiteKing = getElement(move.initial.col, move.initial.row);
      } else {
        blackKing = getElement(move.initial.col, move.initial.row);
      }
    }

    // Undo Pawn Move special cases
    if (move.initial.piece instanceof Pawn) {
      // enPassant
      if(move.special.equals("e.p.")){
        Pawn p = (Pawn)(move.initial.piece);
        int direction = p.dir;
        getElement(move.target.col, move.target.row-direction).piece=move.target.piece;
        getElement(move.target.col, move.target.row).piece=null;
      }

      // Update enPassant square
      Move prevMove = GameModel.getInstance().getMoves().isEmpty() ? null : GameModel.getInstance().getMoves().getFirst();
      if(prevMove!=null && prevMove.initial.piece instanceof Pawn) {
        Pawn p = (Pawn)(prevMove.initial.piece);
        int direction = p.dir;
        if (Math.abs(prevMove.target.row-prevMove.initial.row)==2) {
          enPassant = getElement(prevMove.target.col, prevMove.target.row-direction);
        } else {
          enPassant = null;
        }
      }
    }

    // Castling
    boolean playerIsWhite = GameModel.getInstance().playerIsWhite;
    if(move.special.equals("0-0")){
      Square rook = getElement(move.target.col-(playerIsWhite ? 1:-1), move.target.row);
      getElement(playerIsWhite ? 7:0, move.initial.row).piece=rook.piece;
      rook.piece=null;
    } else if(move.special.equals("0-0-0")){
      Square rook = getElement(move.target.col+(playerIsWhite ? 1:-1), move.target.row);
      getElement(playerIsWhite ? 0:7, move.initial.row).piece=rook.piece;
      rook.piece=null;
    }

    boolean KingMoved = true;
    List<Move> moves = new ArrayList<>(GameModel.getInstance().getMoves());
    moves.removeIf(m -> m.isWhite != move.isWhite);
    moves.removeIf(m -> !(m.initial.piece instanceof King));
    if (moves.isEmpty()){
      KingMoved = false;
    }
    // update castle rights
    if (move.initial.piece instanceof King || move.initial.piece instanceof Rook){
      moves = new ArrayList<>(GameModel.getInstance().getMoves());
      moves.removeIf(m -> m.isWhite != move.isWhite);
      moves.removeIf(m -> !(m.initial.piece instanceof Rook));
      List<Move> moves2 = moves.stream().filter(m -> m.initial.col==(playerIsWhite ? 7:0)).toList();
      moves.removeIf(m -> m.initial.col==(playerIsWhite ? 0:7));
      if (move.isWhite && !KingMoved) {
        if (moves.isEmpty()) {if (playerIsWhite) {castle[0]=true;} else {castle[1]=true;}}
        if (moves2.isEmpty()) {if (playerIsWhite) {castle[1]=true;} else {castle[0]=true;}}
      }
      if (!move.isWhite && !KingMoved){
        if (moves.isEmpty()) {if (playerIsWhite) {castle[2]=true;} else {castle[3]=true;}}
        if (moves2.isEmpty()) {if (playerIsWhite) {castle[3]=true;} else {castle[2]=true;}}
      }
    }

    if (move.target.piece instanceof Rook){
      moves = new ArrayList<>(GameModel.getInstance().getMoves());
      moves.removeIf(m -> m.isWhite != move.isWhite);
      moves.removeIf(m -> !(m.initial.piece instanceof Rook));
      List<Move> moves2 = moves.stream().filter(m -> m.initial.col==(playerIsWhite ? 7:0)).toList();
      moves.removeIf(m -> m.initial.col==(playerIsWhite ? 0:7));
      if (!move.isWhite) {
        if (moves.isEmpty()) {if (playerIsWhite) {castle[0]=true;} else {castle[1]=true;}}
        if (moves2.isEmpty()) {if (playerIsWhite) {castle[1]=true;} else {castle[0]=true;}}
      } else {
        if (moves.isEmpty()) {if (playerIsWhite) {castle[2]=true;} else {castle[3]=true;}}
        if (moves2.isEmpty()) {if (playerIsWhite) {castle[3]=true;} else {castle[2]=true;}}
      }
    }
  }
  //#endregion

  //#region Evaluaton Methods
  public int getState(){
    boolean[] checks = GameModel.getInstance().checks;
    checks[0]=CS.isCheck(true);
    checks[1]=CS.isCheck(false);
    if(GameModel.getInstance().getMoves().isEmpty()) {return 0;}
    if (checks[0] && !GameModel.getInstance().getMoves().getFirst().isWhite){
      GameModel.getInstance().getMoves().getFirst().isCheck=true;
    }
    if (checks[1] && GameModel.getInstance().getMoves().getFirst().isWhite){
      GameModel.getInstance().getMoves().getFirst().isCheck=true;
    }
    int numMoves = getPossibleMoves(!GameModel.getInstance().whiteToMove).size();

    // no possible moves
    if (numMoves==0){
      if (checks[0]) { return 2; }
      else if (checks[1]) { return 1; }
      else { return 3; }
    }

    // 50 moves
    int moveCount = (int)Math.floor(GameModel.getInstance().getMoves().size()/2)+1;
    if (moveCount >= 50) { return 3; }

    // 3 move repetition
    if (checkRepetition(GameModel.getInstance().whiteToMove) && checkRepetition(!GameModel.getInstance().whiteToMove)) { return 3; }

    // not enough material
    if (countMaterial()){return 3;}
    return 0;
  }

  public boolean checkRepetition(boolean isWhite){
    if (GameModel.getInstance().getMoves().size()<2) { return false; }
    ListIterator<Move> moveIterator = GameModel.getInstance().getMoves().listIterator();
    Square target;
    if (isWhite == GameModel.getInstance().whiteToMove) {
      target = GameModel.getInstance().getMoves().get(0).target;
    } else {
      target = GameModel.getInstance().getMoves().get(1).target;
    }

    int count=0, rep=0;
    while (moveIterator.hasNext() && count<=5) {
      Move move = moveIterator.next();
      if(isWhite == move.isWhite) {
        if (move.target.equals(target)){
          rep++;
          if (rep>=3){
            return true;
          }
        }
        count++;
      }
    }
    return false;
  }

  public boolean countMaterial() {
    // [pawns, knights, bishops, rooks, queen, sum]
    int[] whiteMaterial = {0, 0, 0, 0, 0, 0};
    int[] blackMaterial = {0, 0, 0, 0, 0, 0};

    for (Square square : board){
      if (!square.isEmpty()) {
        switch (square.piece.name) {
          case "pawn" : 
            if (square.piece.isWhite){ whiteMaterial[0]++; }
            else { blackMaterial[0]++; } break;
          case "knight" : 
            if (square.piece.isWhite){ whiteMaterial[1]++; }
            else { blackMaterial[1]++; } break;
          case "bishop" : 
            if (square.piece.isWhite){ whiteMaterial[2]++; }
            else { blackMaterial[2]++; } break;
          case "rook" : 
            if (square.piece.isWhite){ whiteMaterial[3]++; }
            else { blackMaterial[3]++; } break;
          case "queen" : 
            if (square.piece.isWhite){ whiteMaterial[4]++; }
            else { blackMaterial[4]++; } break;
        }
      }
    }
    for(int i=0; i<whiteMaterial.length-1; i++) {
      whiteMaterial[5]+=whiteMaterial[i];
      blackMaterial[5]+=blackMaterial[i];
    }

    if (whiteMaterial[4]+whiteMaterial[3]+whiteMaterial[0]+
        blackMaterial[4]+blackMaterial[3]+blackMaterial[0]>0) {return false;}
    if (whiteMaterial[1]+blackMaterial[1]>1) {return false;}
    if ((whiteMaterial[2]>0 || blackMaterial[2]>0)
        && whiteMaterial[1]+blackMaterial[1]>0) {return false;}
    if (whiteMaterial[2]>1 || blackMaterial[2]>1) {return false;}
    if (whiteMaterial[2]==1 && blackMaterial[2]==1) {
      Square whiteBishop=null, blackBishop=null;
      for (Square square : board) {
        if (!square.isEmpty()) {
          if (square.piece instanceof Bishop) {
            if (square.piece.isWhite) {whiteBishop = square;}
            else {blackBishop = square;}
          }
        }
      }
      if (whiteBishop==null || blackBishop==null) {return true;}
      boolean s1 = (whiteBishop.col+whiteBishop.row)%2 == 0;
      boolean s2 = (blackBishop.col+blackBishop.row)%2 == 0;
      if (s1 != s2) {return false;}
    }
    return true;
  }
  //#endregion
}

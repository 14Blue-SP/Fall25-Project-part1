package chess.move;

import chess.gui.Square;
import chess.model.BoardModel;
import chess.model.GameModel;

public class Scorer {
  //#region Piece Square Tables
  private static  int[] pawnTable = {
    00, 00, 00, 00, 00, 00, 00, 00,
    50, 50, 50, 50, 50, 50, 50, 50,
    10, 10, 20, 30, 30, 20, 10, 10,
    05, 05, 10, 25, 25, 10, 05, 05,
    00, 00, 00, 20, 20, 00, 00, 00,
    05,-05,-10, 00, 00,-10,-05, 05,
    05, 10, 10,-20,-20, 10, 10, 05,
    00, 00, 00, 00, 00, 00, 00, 00
  };

  private static  int[] knightTable = {
    -50,-40,-30,-30,-30,-30,-40,-50,
    -40,-20, 00, 00, 00, 00,-20,-40,
    -30, 00, 10, 15, 15, 10, 00,-30,
    -30, 05, 15, 20, 20, 15, 05,-30,
    -30, 00, 15, 20, 20, 15, 00,-30,
    -30, 05, 10, 15, 15, 10, 05,-30,
    -40,-20, 00, 05, 05, 00,-20,-40,
    -50,-40,-30,-30,-30,-30,-40,-50
  };

  private static  int[] bishopTable = {
    -20,-10,-10,-10,-10,-10,-10,-20,
    -10, 00, 00, 00, 00, 00, 00,-10,
    -10, 00, 05, 10, 10, 05, 00,-10,
    -10, 05, 05, 10, 10, 05, 05,-10,
    -10, 00, 10, 10, 10, 10, 00,-10,
    -10, 10, 10, 10, 10, 10, 10,-10,
    -10, 05, 00, 00, 00, 00, 05,-10,
    -20,-10,-10,-10,-30,-10,-10,-20
  };
  
  private static  int[] rookTable = {
     00, 00, 00, 00, 00, 00, 00, 00,
     05, 10, 10, 10, 10, 10, 10, 05,
    -05, 00, 00, 00, 00, 00, 00,-05,
    -05, 00, 00, 00, 00, 00, 00,-05,
    -05, 00, 00, 00, 00, 00, 00,-05,
    -05, 00, 00, 00, 00, 00, 00,-05,
    -05, 00, 00, 00, 00, 00, 00,-05,
     00, 00, 00, 05, 05, 00, 00, 00
  };

  private static  int[] queenTable = {
    -20,-10,-10,-05,-05,-10,-10,-20,
    -10, 00, 00, 00, 00, 00, 00,-10,
    -10, 00, 05, 05, 05, 05, 00,-10,
    -05, 00, 05, 05, 05, 05, 00,-05,
     00, 00, 05, 05, 05, 05, 00,-05,
    -10, 05, 05, 05, 05, 05, 00,-10,
    -10, 00, 05, 00, 00, 00, 00,-10,
    -20,-10,-10,-05,-05,-10,-10,-20
  };

  private static  int[] kingTable = {
    -30,-40,-40,-50,-50,-40,-40,-30,
    -30,-40,-40,-50,-50,-40,-40,-30,
    -30,-40,-40,-50,-50,-40,-40,-30,
    -30,-40,-40,-50,-50,-40,-40,-30,
    -20,-30,-30,-40,-40,-30,-30,-20,
    -10,-20,-20,-20,-20,-20,-20,-10,
     20, 20, 00, 00, 00, 00, 20, 20,
     20, 30, 10, 00, 00, 10, 30, 20
  };

  public static void FlipTables() {
    flipTable(pawnTable);
    flipTable(knightTable);
    flipTable(rookTable);
    flipTable(bishopTable);
    flipTable(queenTable);
    flipTable(kingTable);
  }

  private static void flipTable(int[] table){
    int start = 0;
    int end = table.length-1;

    while (start  < end) {
      int temp = table[start];
      table[start] = table[end];
      table[end] = temp;
      start++;
      end--;
    }
  }
  //#endregion

  public static int score(int numMoves, int depth) {
    int score=0, material=calculateMaterial(true);
    score += calculateAttack(true);
    score += material;
    score += calculateMoveability(numMoves, depth, material, true);
    score += calculatePosition(true);
    material=calculateMaterial(false);
    score -= calculateAttack(false);
    score += material;
    score -= calculateMoveability(numMoves, depth, material, false);
    score -= calculatePosition(false);
    return -(score + depth*50);
  }

  public static int calculateAttack(boolean isWhite) {
    BoardModel board = GameModel.getInstance().getBoardModel();
    int score=0;
    Square temp = board.whiteKing;
    for (int i = 0; i < board.getBoard().length; i++) {
      Square square = board.getBoard()[i]; 
      if (square.isEmpty()) { continue; }
      if (square.piece.isWhite == isWhite) {
        switch (square.piece.name) {
          case "pawn": {board.whiteKing=square; if(board.CS.isCheck(true)){score -= 65;}} break;
          case "knight": {board.whiteKing=square; if(board.CS.isCheck(true)){score -= 300;}} break;
          case "bishop": {board.whiteKing=square; if(board.CS.isCheck(true)){score -= 300;}} break;
          case "rook": {board.whiteKing=square; if(board.CS.isCheck(true)){score -= 500;}} break;
          case "queen": {board.whiteKing=square; if(board.CS.isCheck(true)){score -= 900;}} break;
        }
      }
    }
    board.whiteKing = temp;
    if (board.CS.isCheck(isWhite)){score -= 200;}
    return score/2;
  }

  public static int calculateMaterial(boolean isWhite) {
    BoardModel board = GameModel.getInstance().getBoardModel();
    int score=0;
    for (int i=0; i<board.getBoard().length; i++) {
      Square square = board.getBoard()[i];
      if (!square.isEmpty()) {
        if (square.piece.isWhite == isWhite) {
          score += square.piece.value;
        }
      }
    }
    return score;
  }

  public static int calculatePosition(boolean isWhite) {
    BoardModel board = GameModel.getInstance().getBoardModel();
    int score=0;
    for (int i = 0; i < board.getBoard().length; i++) {
      Square square = board.getBoard()[i];
      if (square.isEmpty()) { continue; }
      if (square.piece.isWhite == isWhite) {
        switch(square.piece.name) {
          case "pawn": score += pawnTable[i]; break;
          case "knight": score += knightTable[i]; break;
          case "bishop": score += bishopTable[i]; break;
          case "rook": score += rookTable[i]; break;
          case "queen": score += queenTable[i]; break;
          case "king": score += kingTable[i]; 
                       board.findMoves(square);
                       score += square.piece.moves.size()*30;
                       square.piece.moves.clear(); break;
        }
      }
    }
    return score;
  }

  public static int calculateMoveability(int numMoves, int depth, int material, boolean isWhite) {
    int state = GameModel.getInstance().getBoardModel().getState();
    int score = 0;
    score+=numMoves*5;
    if (numMoves==0) {
      if ((state==1 && !isWhite) || (state==2 && isWhite)) {
        score -= 200000*depth;

      } else if (state==3) {
        score -= 150000*depth;
      }
    }
    return score;
  }
}

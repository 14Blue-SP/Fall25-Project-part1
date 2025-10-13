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
  //#endregion

  public static int score(int numMoves, int depth) {
    int score=0, material=calculateMaterial(GameModel.getInstance().getBoardModel(), true);
    //score += calculateAttack();
    score += material;
    score += calculatePosition(GameModel.getInstance().getBoardModel(), true);
    material=calculateMaterial(GameModel.getInstance().getBoardModel(), false);
    score += material;
    score -= calculatePosition(GameModel.getInstance().getBoardModel(), false);
    return -(score + depth*50);
  }

  public static int calculateMaterial(BoardModel board, boolean isWhite) {
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

  public static int calculatePosition(BoardModel board, boolean isWhite) {
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
}

package chess.pieces;

import chess.gui.Square;
import chess.model.GameModel;
import chess.move.Move;

public class King extends Piece {
  public King(boolean isWhite) {
    super("king", isWhite, 10000);
  }

  @Override
  public boolean isValidMove(Move move) {
    GameModel GM = GameModel.getInstance();
    boolean[] checks = GM.checks;
    boolean[] castle;
    boolean isCheck;
    int[] king;

    if (isWhite) {
      king = new int[] {4,7};
      isCheck = checks[0];
      castle = new boolean[] {board.castle[0], board.castle[1]};
    } else {
      king = new int[] {4,0};
      isCheck = checks[1];
      castle = new boolean[] {board.castle[2], board.castle[3]};
    }

    if (!GM.playerIsWhite) {
      if (isWhite) {
        king = new int[] {3,0};
        isCheck = checks[0];
        castle = new boolean[] {board.castle[1], board.castle[0]};
      } else {
        king = new int[] {3,7};
        isCheck = checks[1];
        castle = new boolean[] {board.castle[3], board.castle[2]};
      }
    }

    // Castling King side
    if (move.initial.col==king[0] && move.initial.row==king[1]
        && move.target.col==move.initial.col+(GM.playerIsWhite ? 2:-2) && move.target.row==move.initial.row
        && board.getElement(king[0]+(GM.playerIsWhite ? 1:-1), king[1]).isEmpty()
        && board.getElement(king[0]+(GM.playerIsWhite ? 2:-2), king[1]).isEmpty()
        && !isCheck && castle[1])
    {
      Square s1 = board.getElement(king[0], king[1]);
      Square s2 = board.getElement(king[0]+(GM.playerIsWhite ? 1:-1), king[1]);
      Move temp = new Move(s1, s2);
      if (!board.CS.isSafeMove(temp)){
        return false;
      }
      move.special="0-0";
      return true;
    }

    // Castling Queen side
    if (move.initial.col==king[0] && move.initial.row==king[1]
        && move.target.col==move.initial.col-(GM.playerIsWhite ? 2:-2) && move.target.row==move.initial.row
        && board.getElement(king[0]-(GM.playerIsWhite ? 1:-1), king[1]).isEmpty()
        && board.getElement(king[0]-(GM.playerIsWhite ? 2:-2), king[1]).isEmpty()
        && board.getElement(king[0]-(GM.playerIsWhite ? 3:-3), king[1]).isEmpty()
        && !isCheck && castle[0])
    {
      Square s1 = board.getElement(king[0], king[1]);
      Square s2 = board.getElement(king[0]-(GM.playerIsWhite ? 1:-1), king[1]);
      Move temp = new Move(s1, s2);
      if (!board.CS.isSafeMove(temp)){
        return false;
      }
      move.special="0-0-0";
      return true;
    }

    return Math.abs(move.initial.col-move.target.col)<=1 && Math.abs(move.initial.row-move.target.row)<=1;
  }
}
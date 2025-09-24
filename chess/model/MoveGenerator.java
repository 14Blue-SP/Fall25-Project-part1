package chess.model;

import java.util.ArrayList;
import java.util.List;

import chess.move.*;

public class MoveGenerator {
  private GameModel GM;
  public CheckScanner CS;

  public MoveGenerator(GameModel gameModel){
    GM = gameModel;
    CS = new CheckScanner(gameModel);
  }

  public List<KingMove> possibleKing(int index) {
    int[] piece = GM.getCoordinates(index);
    List<KingMove> list = new ArrayList<>();
    for (int i = 0; i < GM.getBoard().length; i++) {
      int[] target = GM.getCoordinates(i);
      KingMove move = new KingMove(piece[0], piece[1], target[0], target[1]);
      if (CS.isLegalMove(move) && CS.isSafeMove(move)) {
        list.add(move);
      }
    }
    return list;
  }

  public List<QueenMove> possibleQueen(int index) {
    int[] piece = GM.getCoordinates(index);
    List<QueenMove> list = new ArrayList<>();
    for (int i = 0; i < GM.getBoard().length; i++) {
      int[] target = GM.getCoordinates(i);
      QueenMove move = new QueenMove(piece[0], piece[1], target[0], target[1]);
      if (CS.isLegalMove(move) && CS.isSafeMove(move)) {
        list.add(move);
      }
    }
    return list;
  }

  public List<BishopMove> possibleBishop(int index) {
    int[] piece = GM.getCoordinates(index);
    List<BishopMove> list = new ArrayList<>();
    for (int i = 0; i < GM.getBoard().length; i++) {
      int[] target = GM.getCoordinates(i);
      BishopMove move = new BishopMove(piece[0], piece[1], target[0], target[1]);
      if (CS.isLegalMove(move) && CS.isSafeMove(move)) {
        list.add(move);
      }
    }
    return list;
  }

  public List<RookMove> possibleRook(int index) {
    int[] piece = GM.getCoordinates(index);
    List<RookMove> list = new ArrayList<>();
    for (int i = 0; i < GM.getBoard().length; i++) {
      int[] target = GM.getCoordinates(i);
      RookMove move = new RookMove(piece[0], piece[1], target[0], target[1]);
      if (CS.isLegalMove(move) && CS.isSafeMove(move)) {
        list.add(move);
      }
    }
    return list;
  }

  public List<KnightMove> possibleKnight(int index) {
    int[] piece = GM.getCoordinates(index);
    List<KnightMove> list = new ArrayList<>();
    for (int i = 0; i < GM.getBoard().length; i++) {
      int[] target = GM.getCoordinates(i);
      KnightMove move = new KnightMove(piece[0], piece[1], target[0], target[1]);
      if (CS.isLegalMove(move) && CS.isSafeMove(move)) {
        list.add(move);
      }
    }
    return list;
  }

  public List<PawnMove> possiblePawn(int index) {
    int[] piece = GM.getCoordinates(index);
    List<PawnMove> list = new ArrayList<>();
    for (int i = 0; i < GM.getBoard().length; i++) {
      int[] target = GM.getCoordinates(i);
      PawnMove move = new PawnMove(piece[0], piece[1], target[0], target[1]);
      if (CS.isLegalMove(move) && CS.isSafeMove(move)) {
        list.add(move);
      }
    }
    return list;
  }
}

package chess.model;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javax.swing.JOptionPane;

import chess.Const;
import chess.move.*;

public class GameModel {
  private static final GameModel INSTANCE = new GameModel();
  private BoardModel boardModel = new BoardModel();
  private List<Move> moves = new Stack<>();
  
  public boolean whiteToMove, playerIsWhite=true, computerGame, isPlaying=true;
  public boolean[] checks = {false, false};

  public String[] gameStates = {"playing", "White", "Black", "Draw"};

  public static GameModel getInstance() {
    return INSTANCE;
  }

  public BoardModel getBoardModel() {
    return boardModel;
  }

  public void newGame() {
    whiteToMove = true;
    moves.clear();
    checks = new boolean[] {false, false};

    Object[] mode={"Multiplayer", "Computer"};
    computerGame = (JOptionPane.showOptionDialog(Const.frame, "What kind of game do you want to play?", "Choose Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, mode, mode[0])==1);
    System.out.printf("You picked a %s game.%n%s%n", (!computerGame ? "Multiplayer":"Computer"), (!computerGame ? "Have fun!":"Your opponent will be the Computer."));

    if (computerGame) {
      Object[] team={"Black", "White"};
      playerIsWhite = (JOptionPane.showOptionDialog(Const.frame, "Play as White or Black?", "Choose Your Side", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, team, team[1])==1);
      System.out.printf("You are playing as %s.%n%s%n", (playerIsWhite ? "White":"Black"), (playerIsWhite ? "You go first!":"Computer goes first!"));
    }
    boardModel.newStandardChessBoard();
    if (!playerIsWhite) {boardModel.flipBoard();}
  }

  public void nextTurn() {
    whiteToMove = !whiteToMove;
  }

  public void promotionSelection(Move move) {
    Object[] option = {"Queen", "Rook", "Bishop", "Knight"};
    int promPiece = (JOptionPane.showOptionDialog(Const.frame, "Selcet a Promotion", "Pawn Promotion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]));
    switch (promPiece) {
      case 0: move.special = "=Q"; break;
      case 1: move.special = "=R"; break;
      case 2: move.special = "=B"; break;
      case 3: move.special = "=N"; break;
      default: move.special = "=Q"; break;
    }
  }

  public void makeMove(Move move) {
    boardModel.makeMove(move);
    //System.out.println(move);
    //boardModel.printBoard();
    addMove(move);
    getState();
  }

  public void undoMove() {
    if (!moves.isEmpty()) {
      Move lastMove = moves.remove(0);
      //System.out.println(lastMove);
      boardModel.undoMove(lastMove);
      //boardModel.printBoard();
      getState();
    }
  }

  public void addMove(Move move){
    moves.addFirst(move);
  }

  public List<Move> getMoves() {
    return moves;
  }

  public List<Move> sortMoves(List<Move> list) {
    for (Move move : list) {
      boardModel.makeMove(move);
      move.score = Scorer.score(-1, 0);
      boardModel.undoMove(move);
    }
    Collections.sort(list, (m1,m2) -> Integer.compare(m1.score, m2.score));
    //if (!list.getFirst().isWhite) { return list.reversed(); }
    return list;
  }

  public MinMaxMove MinMax(int depth, int min, int max, Move move, boolean isMaximizingPlayer) {
    List<Move> list = boardModel.getPossibleMoves(isMaximizingPlayer);
    if (depth == 0 || list.isEmpty()) {return new MinMaxMove(move, Scorer.score(list.size(), depth)*(isMaximizingPlayer ? 1:-1));}

    // sort moves by score value
    list = sortMoves(list);
    //if(depth==Const.MAX_DEPTH)System.out.println("depth: "+depth +"|"+list);
    for (Move iteration : list) {
      boardModel.makeMove(iteration);
      int bestScore;
      if (isMaximizingPlayer) 
      {bestScore = MinMax(depth-1, min, max, iteration, false).score;}
      else {bestScore = MinMax(depth-1, min, max, iteration, true).score;}
      boardModel.undoMove(iteration);

      if (!isMaximizingPlayer) {
        min = Math.min(min, bestScore);
        if (depth == Const.MAX_DEPTH) { move = iteration; }

        if (min<=max) {break;}
      } else {
        max = Math.max(max, bestScore);
        if (depth == Const.MAX_DEPTH) { move = iteration; }

        if (min>=max) {break;}
      }
    }
    if (isMaximizingPlayer) {return new MinMaxMove(move, max); }
    else { return new MinMaxMove(move, min); }
  }

  public void getState(){
    int state = boardModel.getState();
    //System.out.printf("Checks: {White:%b, Black:%b}%n", GameModel.getInstance().checks[0], checks[1]);
    //System.out.printf("Game: %s%n", gameStates[state]);

    if (state != 0) {
      isPlaying = false;
      String message;
      switch (state) {
        case 1: message = "White Has Won the Game."; break;
        case 2: message = "Black Has Won the Game."; break;
        case 3: message = "The Game Has ended in a Draw."; break;
        default: message = "This is an informational message."; break;
      }
      JOptionPane.showMessageDialog(Const.frame, message, "Game Over!", JOptionPane.INFORMATION_MESSAGE);
    }
  }
}

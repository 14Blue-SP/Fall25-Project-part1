package chess.move;

import chess.model.GameModel;

public class PieceMove implements Move {
  GameModel gameModel;

  public PieceMove(GameModel gameModel){
    this.gameModel = gameModel;
  }

  @Override
  public void move() {
    // Implementation of the move method
  }
}

package chess.move;

public class MinMaxMove extends Move {
  
  public MinMaxMove(Move move, int score) {
    super(move.initial, move.target);
    this.score = score;
    this.special = move.special;
    this.isCheck = move.isCheck;
  }

  @Override
  public String toString() {
    return String.format("%s, Score: %d", super.toString(), score);
  }
}

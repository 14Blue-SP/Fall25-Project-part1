package chess.move;

public class KnightMove extends Move {

  public KnightMove(int col, int row, int newCol, int newRow) {
    super(col, row, newCol, newRow);
  }
  
  @Override
   public boolean isValidMove(){
    return Math.abs(coords[0]-coords[2]) * Math.abs(coords[1]-coords[3]) == 2;
   }
}

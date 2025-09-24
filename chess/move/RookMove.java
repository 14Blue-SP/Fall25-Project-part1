package chess.move;

public class RookMove extends Move {

  public RookMove(int col, int row, int newCol, int newRow) {
    super(col, row, newCol, newRow);
  }
  
  @Override
   public boolean isValidMove(){
    return coords[0]==coords[2] || coords[1]==coords[3];
   }

   @Override
   public boolean pieceCollision() {
    return isVerticalCollision() || isHorizontalCollision();
  }
}

package chess.gui;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.JPanel;

import chess.model.GameModel;
import chess.model.GameModelListener;
import chess.pieces.*;

public class BoardView extends JPanel implements GameModelListener {
  public int tileSize;
  protected int files = 8, ranks = 8;

  private ArrayList<Piece> pieces = new ArrayList<>();

  public Piece selectedPiece = null;

  private Input input = new Input(this);
  
  public BoardView(int width){
    GameModel.getInstance().newGame();
    this.tileSize = (int)(width*0.9)/ files;
    this.setPreferredSize(new Dimension(files*tileSize, ranks*tileSize));
    placePieces();

    this.addMouseListener(input);
    this.addMouseMotionListener(input);

    GameModel.getInstance().printBoard();
    GameModel.getInstance().addListener(this);
  }

  public void placePieces(){
    pieces.clear();
    for(int i=0;i<64;i++){
      int file = i%files;
      int rank = i/ranks;
      String space = GameModel.getInstance().getBoard()[i];

      if(space.equalsIgnoreCase("P")){
        pieces.add(new Pawn(this, file, rank, Character.isUpperCase(space.charAt(0))));
      }
      if(space.equalsIgnoreCase("R")){
        pieces.add(new Rook(this, file, rank, Character.isUpperCase(space.charAt(0))));
      }
      if(space.equalsIgnoreCase("N")){
        pieces.add(new Knight(this, file, rank, Character.isUpperCase(space.charAt(0))));
      }
      if(space.equalsIgnoreCase("B")){
        pieces.add(new Bishop(this, file, rank, Character.isUpperCase(space.charAt(0))));
      }
      if(space.equalsIgnoreCase("Q")){
        pieces.add(new Queen(this, file, rank, Character.isUpperCase(space.charAt(0))));
      }
      if(space.equalsIgnoreCase("K")){
        pieces.add(new King(this, file, rank, Character.isUpperCase(space.charAt(0))));
      }
    }
  }

  public Piece getPieceAt(int file, int rank){
    for(Piece piece : pieces){
      if(piece.col == file && piece.row == rank){
        return piece;
      }
    }
    return null;
  }

  public void paintComponent(Graphics gfx){
    for(int rank = 0; rank < ranks; rank++){
      for(int file = 0; file < files; file++){
        gfx.setColor((rank + file) % 2 == 0 ? Color.white : Color.gray);
        gfx.fillRect(file*tileSize, rank*tileSize, tileSize, tileSize);
      }
    }

    for(Piece piece : pieces){
      piece.draw(gfx,tileSize);
    }
  }

  @Override
  public void gameStateChanged() {
    //System.out.print(this.getClass().getSimpleName()+"--");
    //System.out.println(new Throwable().getStackTrace()[0].getMethodName());
    placePieces();
    repaint();
  }
}
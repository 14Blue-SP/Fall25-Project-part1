package chess.gui;

import java.awt.*;
import javax.swing.JPanel;

import chess.model.GameModel;
import chess.model.GameModelListener;
import chess.move.PieceMove;
import chess.pieces.*;

public class BoardView extends JPanel implements GameModelListener {
  public int tileSize;
  protected int files = 8, ranks = 8;

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
    GameModel.getInstance().getPieces().clear();
    for(int i=0;i<64;i++){
      int file = i%files;
      int rank = i/ranks;
      String space = GameModel.getInstance().getBoard()[i];

      if(space.equalsIgnoreCase("P")){
        GameModel.getInstance().getPieces().add(new Pawn(this, file, rank, Character.isUpperCase(space.charAt(0))));
      }
      if(space.equalsIgnoreCase("R")){
        GameModel.getInstance().getPieces().add(new Rook(this, file, rank, Character.isUpperCase(space.charAt(0))));
      }
      if(space.equalsIgnoreCase("N")){
        GameModel.getInstance().getPieces().add(new Knight(this, file, rank, Character.isUpperCase(space.charAt(0))));
      }
      if(space.equalsIgnoreCase("B")){
        GameModel.getInstance().getPieces().add(new Bishop(this, file, rank, Character.isUpperCase(space.charAt(0))));
      }
      if(space.equalsIgnoreCase("Q")){
        GameModel.getInstance().getPieces().add(new Queen(this, file, rank, Character.isUpperCase(space.charAt(0))));
      }
      if(space.equalsIgnoreCase("K")){
        GameModel.getInstance().getPieces().add(new King(this, file, rank, Character.isUpperCase(space.charAt(0))));
      }
    }
  }

  public void paintComponent(Graphics gfx){
    // draw board
    for(int rank = 0; rank < ranks; rank++){
      for(int file = 0; file < files; file++){
        gfx.setColor((rank + file) % 2 == 0 ? Color.white : Color.gray);
        gfx.fillRect(file*tileSize, rank*tileSize, tileSize, tileSize);
        gfx.setColor(Color.black);
        gfx.drawRect(file*tileSize, rank*tileSize, tileSize, tileSize);
      }
    }

    // draw coordinates
    for(int rank = 0; rank < ranks; rank++){
      gfx.setColor(Color.black);
      gfx.drawString(""+(8-rank), 2, rank*tileSize + 10);
    }
    for(int file = 0; file < files; file++){
      gfx.setColor(Color.black);
      gfx.drawString(""+(char)('a'+file), file*tileSize + (int)(tileSize*0.85), ranks*tileSize - 2);
    }

    // highlight legal moves for selected piece
    for(int rank = 0; rank < ranks; rank++){
      for(int file = 0; file < files; file++){
        if(selectedPiece != null){
          PieceMove move = new PieceMove(GameModel.getInstance());
          move.move(selectedPiece, file, rank);
          if(GameModel.getInstance().isLegalMove(move)){
            gfx.setColor(new Color(150,255,0,75));
            gfx.fillRect(file*tileSize, rank*tileSize, tileSize, tileSize);
          }
        }
      }
    }

    for(Piece piece : GameModel.getInstance().getPieces()){
      piece.draw(gfx,tileSize);
    }
  }

  @Override
  public void gameStateChanged() {
    //System.out.print(this.getClass().getSimpleName()+"--");
    //System.out.println(new Throwable().getStackTrace()[0].getMethodName());
    repaint();
  }
}
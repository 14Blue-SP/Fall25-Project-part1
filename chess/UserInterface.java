package chess;

import javax.swing.*;
import javax.swing.border.Border;

import chess.model.GameModel;
import chess.pieces.Piece;

import java.awt.*;
import java.awt.event.*;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
  private GameModel GM = Main.GM;
  int mouse[] = {-1,-1};

  public UserInterface(){
    Main.gridSize = (int)(Main.gridSize*0.9)/GM.files;
    setPreferredSize(new Dimension(GM.files * Main.gridSize, GM.ranks * Main.gridSize));
    Border lineBorder = BorderFactory.createLineBorder(Color.decode(Main.colors[5]), 5);
    setBorder(lineBorder);
    addMouseListener(this);
    addMouseMotionListener(this);
    addKeyListener(this);
    setFocusable(true);
  }

  @Override
  public void paintComponent(Graphics gfx){
    super.paintComponent(gfx);

    drawBoard(gfx);
    drawHoveredSquare(gfx);
    drawPieces(gfx);
  }

  private void drawBoard(Graphics gfx){
    for (int rank=0; rank<GM.ranks; rank++){
      for (int file=0; file<GM.files; file++){
        gfx.setColor((file+rank)%2 == 0 ? Color.decode(Main.colors[2]) : Color.decode(Main.colors[1]));
        gfx.fillRect(file*Main.gridSize, rank*Main.gridSize, Main.gridSize, Main.gridSize);
        gfx.setColor(Color.decode(Main.colors[0]));
        gfx.drawRect(file*Main.gridSize, rank*Main.gridSize, Main.gridSize, Main.gridSize);
      }
    }
  }

  private void drawHoveredSquare(Graphics gfx){
    if(mouse[0]>=0 && mouse[1]>=0){
      gfx.setColor(Color.decode(Main.colors[3]));
      gfx.fillRect(mouse[0]*Main.gridSize, mouse[1]*Main.gridSize, Main.gridSize, Main.gridSize);
    }
  }

  private void drawPieces(Graphics gfx){
    for(Piece p : GM.getPieces()){
      int spriteIndex=-1;
      switch (Character.toLowerCase(p.type)) {
        case 'k': spriteIndex=0; break;
        case 'q': spriteIndex=1; break;
        case 'b': spriteIndex=2; break;
        case 'n': spriteIndex=3; break;
        case 'r': spriteIndex=4; break;
        case 'p': spriteIndex=5; break;
      }
      gfx.drawImage(Main.pieceSheet, p.pos[0], p.pos[1], p.pos[0]+Main.gridSize, p.pos[1]+Main.gridSize, spriteIndex*Main.imageScale, (p.isWhite? 0:1)*Main.imageScale, (spriteIndex+1)*Main.imageScale, ((p.isWhite? 0:1)+1)*Main.imageScale, getFocusCycleRootAncestor());
    }
  }

  //#region Event Listener Moves
  @Override
  public void mousePressed(MouseEvent e) {
    int file = e.getX() / Main.gridSize;
    int rank = e.getY() / Main.gridSize;

    Piece piece = GM.getPiece(file, rank);
    if(piece!=null && piece.isWhite == GM.isWhiteTurn){
      GM.selectedPiece=piece;
    }
    //System.out.printf("Mouse Pressed - {file: %d, rank: %d}%n",file,rank);
  }
  @Override
  public void mouseReleased(MouseEvent e) {
    int file = e.getX() / Main.gridSize;
    int rank = e.getY() / Main.gridSize;

     if(GM.selectedPiece!=null){
      boolean isValidMove = false;
      if(isValidMove){
      } else {
        GM.selectedPiece.pos[0] = GM.selectedPiece.coord[0] * Main.gridSize;
        GM.selectedPiece.pos[1] = GM.selectedPiece.coord[1] * Main.gridSize;
      }
    }
    repaint();
  }
  @Override
  public void mouseDragged(MouseEvent e) {
    mouse[0]=-1; mouse[1]=-1;
    if(GM.selectedPiece != null){
      GM.selectedPiece.pos[0] = e.getX() - Main.gridSize/2;
      GM.selectedPiece.pos[1] = e.getY() - Main.gridSize/2;
    }
    repaint();
  }
  @Override
  public void mouseMoved(MouseEvent e) {
    mouse[0] = e.getX() / Main.gridSize;
    mouse[1] = e.getY() / Main.gridSize;
    repaint();
  }
  @Override
  public void mouseExited(MouseEvent e) {
    mouse[0]=-1; mouse[1]=-1;
    repaint();
  }
  @Override
  public void keyReleased(KeyEvent e) {
    int keyCode = e.getKeyCode();
    if(keyCode == KeyEvent.VK_SPACE){ 
      System.out.println("Space Pressed");
      repaint();
    }
  }
  @Override
  public void keyTyped(KeyEvent e) {}
  @Override
  public void keyPressed(KeyEvent e) {}
  @Override
  public void mouseClicked(MouseEvent e) { }
  @Override
  public void mouseEntered(MouseEvent e) {}
  //#endregion
}

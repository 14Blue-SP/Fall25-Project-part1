package chess;

import javax.swing.*;
import javax.swing.border.Border;

import chess.model.GameModel;

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

  //#region Event Listener Moves
  @Override
  public void mousePressed(MouseEvent e) {
    int file = e.getX() / Main.gridSize;
    int rank = e.getY() / Main.gridSize;
    System.out.printf("Mouse Pressed - {file: %d, rank: %d}%n",file,rank);
  }
  @Override
  public void mouseReleased(MouseEvent e) {
    int file = e.getX() / Main.gridSize;
    int rank = e.getY() / Main.gridSize;
    repaint();
  }
  @Override
  public void mouseDragged(MouseEvent e) {
    mouse[0]=-1; mouse[1]=-1;
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

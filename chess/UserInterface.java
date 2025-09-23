package chess;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
  int mouse[] = new int[2];

  public UserInterface(){
    Main.gridSize = (int)(Main.gridSize*0.9);
    setPreferredSize(new Dimension(Main.gridSize, Main.gridSize));
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
  }

  //#region Event Listener Moves
  @Override
  public void mousePressed(MouseEvent e) {
  }
  @Override
  public void keyTyped(KeyEvent e) {}
  @Override
  public void keyPressed(KeyEvent e) {}
  @Override
  public void keyReleased(KeyEvent e) {}
  @Override
  public void mouseDragged(MouseEvent e) {}
  @Override
  public void mouseMoved(MouseEvent e) {}
  @Override
  public void mouseClicked(MouseEvent e) { }
  @Override
  public void mouseReleased(MouseEvent e) {}
  @Override
  public void mouseEntered(MouseEvent e) {}
  @Override
  public void mouseExited(MouseEvent e) {}
  //#endregion
}

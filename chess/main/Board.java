package chess.main;

import java.awt.*;
import javax.swing.JPanel;

public class Board extends JPanel{
  protected int tileSize, files = 8, ranks = 8;
  
  public Board(int width){
    this.tileSize = (int)(width*0.9)/ files;
    this.setPreferredSize(new Dimension(files*tileSize, ranks*tileSize));
  }
  

  public void paintComponent(Graphics g){
    for(int rank = 0; rank < ranks; rank++){
      for(int file = 0; file < files; file++){
        g.setColor((rank + file) % 2 == 0 ? Color.white : Color.gray);
        g.fillRect(file*tileSize, rank*tileSize, tileSize, tileSize);
      }
    }
  }
}
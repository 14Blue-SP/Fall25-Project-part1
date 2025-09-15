package chess.gui;

import java.awt.*;
import javax.swing.*;

public class Main {
  public static void main(String[] args){
    JFrame frame = new JFrame();
    frame.setTitle("Chess Engine 1.0");
    frame.getContentPane().setBackground(Color.black);
    frame.setLayout(new GridBagLayout());
    frame.setMinimumSize(new Dimension(600,600));
    frame.setLocationRelativeTo(null);
    
    BoardView board = new BoardView(frame.getWidth());
    frame.add(board);
    
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
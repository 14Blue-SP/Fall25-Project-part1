package chess;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import chess.gui.BoardView;

public class Main {
  private static final String TITLE = "Chess Engine";
  private static final String VERSION = "4.73";
  private static BoardView boardView;

  public static void main(String[] args) {
    Const.frame.setTitle(TITLE + " " + VERSION);
    Const.frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    Const.frame.setLayout(new GridBagLayout());
    Const.frame.setMinimumSize(new Dimension(Const.WIDTH, Const.HEIGHT));
    //Const.frame.setLocation(0,0);
    Const.frame.setResizable(false);
    Const.frame.getContentPane().setBackground(Color.decode(Const.colors[0]));

    boardView = new BoardView();
    Const.frame.add(boardView);

    Const.frame.setVisible(true);
    Const.frame.pack();
    boardView.requestFocus();
  }
}

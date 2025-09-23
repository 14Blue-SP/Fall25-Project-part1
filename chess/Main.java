package chess;

import java.awt.*;
import javax.swing.*;

public class Main {
  //public static GameModel GM = GameModel.getInstance();
  public static String[] colors = {"#1B1B1B", "#808A9F", "#F4F7F5", "#C83E4D", "#04395E", "#031D44"};
  static Image pieceSheet = new ImageIcon("src/pieces.png").getImage();
  static int imageScale = pieceSheet.getWidth(null)/6;
  public static int gridSize=500;

  public static void main(String[] args) {
    //#region Window Code
    JFrame frame = new JFrame("SP-14 : Blue | Chess Engine");
    frame.getContentPane().setBackground(Color.decode(colors[0]));
    frame.setLayout(new GridBagLayout());
    frame.setMinimumSize(new Dimension(500,500));
    frame.setResizable(false);
    frame.setLocation(0,0);
    frame.add(new UserInterface());
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //#endregion

    //#region Run Code
    //#endregion
  }
}

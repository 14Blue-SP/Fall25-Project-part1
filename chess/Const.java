package chess;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Const {
  public static int WIDTH = 500;
  public static int HEIGHT = WIDTH;
  public static int ROWS = 8;
  public static int COLS = 8;
  public static int SIZE = (int)(WIDTH*0.9)/COLS;
  public static int MAX_DEPTH=4;
  
  public static String[] colors = {"#1B1B1B", "#808A9F", "#F4F7F5", "#C83E4D", "#04395E", "#031D44"};

  public static Image pieceSheet = new ImageIcon("src/pieces.png").getImage();
  public static int imageScale = pieceSheet.getWidth(null)/6;

  public static JFrame frame = new JFrame();
}

package chess.model;

import java.awt.Image;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;

import chess.move.Move;
import chess.move.PieceMove;
import chess.pieces.Piece;

public class GameModel {
  private static GameModel INSTANCE = new GameModel();
  private String[] board = new String[64];
  private List<GameModelListener> listeners = new ArrayList<>();

  public Image pieceImage = new ImageIcon("chess/pieces.png").getImage();
  public int imageScale = pieceImage.getWidth(null) / 6;

  public static GameModel getInstance(){
    return INSTANCE;
  }

  public void addListener(GameModelListener listener){
    listeners.add(listener);
  }

  public void notifyListeners(){
    for(GameModelListener listener : listeners){
      listener.gameStateChanged();
    }
  }

  public void newGame(){
    // Initialize board and pieces for a new game
    clearBoard();
    for(int c=0; c < 2; c++){
      // Place pawns
      for(int i=0;i<8;i++){
        setPiece(getIndex(i, c%2==0 ? 6:1), c%2==0 ? "P":"p");
      }
      // Place rooks
      setPiece(getIndex(0, c%2==0 ? 7:0), c%2==0 ? "R":"r");
      setPiece(getIndex(7, c%2==0 ? 7:0), c%2==0 ? "R":"r");
      // Place knights
      setPiece(getIndex(1, c%2==0 ? 7:0), c%2==0 ? "N":"n");
      setPiece(getIndex(6, c%2==0 ? 7:0), c%2==0 ? "N":"n");
      // Place bishops
      setPiece(getIndex(2, c%2==0 ? 7:0), c%2==0 ? "B":"b");
      setPiece(getIndex(5, c%2==0 ? 7:0), c%2==0 ? "B":"b");
      // Place queens
      setPiece(getIndex(3, c%2==0 ? 7:0), c%2==0 ? "Q":"q");
      // Place kings
      setPiece(getIndex(4, c%2==0 ? 7:0), c%2==0 ? "K":"k");
    }
  }

  public void clearBoard(){
    Arrays.fill(board, " ");
  }

  public String[] getBoard(){
    return board;
  }

  public void setPiece(int index, String piece){
    board[index] = piece;
  }

  public int getIndex(int col, int row){
    return row*8 + col;
  }

  public void printBoard(){
    for(int i = 0; i < board.length; i+=8){
      System.out.println(Arrays.toString(Arrays.copyOfRange(board, i, i+8)));
    } 
  }

  public Move makeMove(Piece piece, int file, int rank){
    // Logic to make a move on the board
    notifyListeners();
    return new PieceMove(getInstance());
  }
}

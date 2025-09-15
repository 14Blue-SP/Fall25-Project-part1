package chess.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import chess.model.GameModel;
import chess.move.Move;
import chess.move.PieceMove;
import chess.pieces.Piece;

public class Input extends MouseAdapter {
  
  BoardView boardView;
  
  public Input(BoardView boardView){
    this.boardView = boardView;
  }

  @Override
  public void mousePressed(MouseEvent e) {
    int file = e.getX() / boardView.tileSize;
    int rank = e.getY() / boardView.tileSize;
    Piece piece = boardView.getPieceAt(file, rank);
    if(piece != null){
      boardView.selectedPiece = piece;
    }
    //System.out.println("Mouse pressed at: " + boardView.getSpaceName(file, rank));
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if(boardView.selectedPiece != null){
      boardView.selectedPiece.xPos = e.getX() - boardView.tileSize / 2;
      boardView.selectedPiece.yPos = e.getY() - boardView.tileSize / 2;
      boardView.repaint();
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    int file = e.getX() / boardView.tileSize;
    int rank = e.getY() / boardView.tileSize;
    
    if(boardView.selectedPiece != null){
      PieceMove move = new PieceMove(GameModel.getInstance());
      move.move(boardView, boardView.selectedPiece, file, rank);
      
      if(GameModel.getInstance().isLegalMove(move)){
        GameModel.getInstance().makeMove(move);
      }
      boardView.selectedPiece = null;
    }
  }
}

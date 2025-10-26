package chess.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.JPanel;

import chess.Const;
import chess.model.BoardModel;
import chess.model.GameModel;
import chess.move.Move;
import chess.pieces.Piece;

public class BoardView extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
  private GameModel GM = GameModel.getInstance();
  private BoardModel board;
  private int mouse[] = { -1, -1 };
  private Square selectedSquare = null;

  public BoardView() {
    setPreferredSize(new Dimension(Const.COLS*Const.SIZE, Const.ROWS*Const.SIZE));
    addMouseListener(this);
    addMouseMotionListener(this);
    addKeyListener(this);
    board = GM.getBoardModel();
    GM.newGame();
  }

  @Override
  public void paintComponent(Graphics gfx) {
    super.paintComponent(gfx);

    drawBoard(gfx);
    drawHoveredSquare(gfx);
    drawLegalSquares(gfx);
    drawPieces(gfx);
    if (selectedSquare != null && selectedSquare.piece != null) {
      drawPiece(gfx, selectedSquare.piece);
    }

    if (GM.computerGame) {
      if (GM.isPlaying && (GM.whiteToMove==!GM.playerIsWhite)) {
        Square s = GM.getBoardModel().getElement(0, 0);
        GM.makeMove((GM.MinMax(Const.MAX_DEPTH, Integer.MAX_VALUE, Integer.MIN_VALUE, new Move(s,s), GM.whiteToMove)));
        GM.nextTurn();
        repaint();
      }
    }
  }

  //#region Paint Methods
  private void drawBoard(Graphics gfx) {
    for (int i=0; i<board.getBoard().length; i++) {
      int[] coords = board.getCoordinates(i);
       gfx.setColor(((coords[0]+coords[1])%2 == 0) == GM.playerIsWhite ? Color.decode(Const.colors[2]) : Color.decode(Const.colors[1]));
      gfx.fillRect(coords[0]*Const.SIZE, coords[1]*Const.SIZE, Const.SIZE, Const.SIZE);
      gfx.setColor(Color.decode(Const.colors[0]));
      gfx.drawRect(coords[0]*Const.SIZE, coords[1]*Const.SIZE, Const.SIZE, Const.SIZE);
    }
  }

  private void drawHoveredSquare(Graphics gfx) {
    gfx.setColor(Color.decode(Const.colors[3]));
    if (mouse[0] >= 0 && mouse[1] >= 0) {
      gfx.fillRect(mouse[0]*Const.SIZE, mouse[1]*Const.SIZE, Const.SIZE, Const.SIZE);
    }
    if (selectedSquare != null) {
      gfx.fillRect(selectedSquare.col*Const.SIZE, selectedSquare.row*Const.SIZE, Const.SIZE, Const.SIZE);
    }
  }

  private void drawLegalSquares(Graphics gfx) {
    Color color = Color.decode(Const.colors[3]);
    gfx.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 125));
    if (selectedSquare != null) {
      for (Move move : selectedSquare.piece.moves) {
        gfx.fillRect(move.target.col*Const.SIZE, move.target.row*Const.SIZE, Const.SIZE, Const.SIZE);
      }
    }
  }

  private void drawPieces(Graphics gfx) {
    for (Square square : board.getBoard()) {
      if (square.piece != null) {
        if (!square.equals(selectedSquare)) {
          square.piece.pos[0] = square.col * Const.SIZE;
          square.piece.pos[1] = square.row * Const.SIZE;
        }
        drawPiece(gfx, square.piece);
      }
    }
  }
  private void drawPiece(Graphics gfx, Piece p) {
    gfx.drawImage(Const.pieceSheet, p.pos[0], p.pos[1], p.pos[0]+Const.SIZE, p.pos[1]+Const.SIZE, p.spriteIndex * Const.imageScale, p.isWhite ? 0:1 * Const.imageScale, (p.spriteIndex+1) * Const.imageScale, ((p.isWhite ? 0:1)+1) * Const.imageScale, getFocusCycleRootAncestor());
  }
  //#endregion

  //#region Event Listener Methods
  @Override
  public void mousePressed(MouseEvent e) {
    if (!GM.isPlaying) {return;}
    int[] coordinate = {e.getX()/Const.SIZE, e.getY()/Const.SIZE};
    selectedSquare = board.getElement(coordinate[0], coordinate[1]);
    if (selectedSquare.piece != null && selectedSquare.piece.isWhite == GM.whiteToMove) {
      if (GM.computerGame) {
        if (GM.whiteToMove != GM.playerIsWhite){
          return;
        }
      }
      board.findMoves(selectedSquare);
    } else {
      selectedSquare = null;
    }
  }
  @Override
  public void mouseReleased(MouseEvent e) {
    if (!GM.isPlaying) {return;}
    int[] coordinate = {e.getX()/Const.SIZE, e.getY()/Const.SIZE};
    if (selectedSquare != null) {
      Square target = board.getElement(coordinate[0], coordinate[1]);
      Move move = new Move(selectedSquare, target);

      int moveIndex = selectedSquare.piece.moves.indexOf(move);
      if (moveIndex != -1) {
        move = selectedSquare.piece.moves.get(moveIndex);
        if(move.special.startsWith("=")){
          GM.promotionSelection(move);
        }
        GM.makeMove(move);
        GM.nextTurn();
      }
    }
    selectedSquare = null;
    repaint();
  }
  @Override
  public void mouseDragged(MouseEvent e) {
    mouse[0] = -1; mouse[1] = -1;
    if (selectedSquare != null) {
      selectedSquare.piece.pos[0] = e.getX() - Const.SIZE/2;
      selectedSquare.piece.pos[1] = e.getY() - Const.SIZE/2;
    }
    repaint();
  }
  @Override
  public void mouseMoved(MouseEvent e) {
    if (!GM.isPlaying) {return;}
    mouse[0] = e.getX() / Const.SIZE;
    mouse[1] = e.getY() / Const.SIZE;
    repaint();
  }
  @Override
  public void mouseExited(MouseEvent e) {
    mouse[0] = -1; mouse[1] = -1;
    repaint();
  }
  @Override
  public void keyReleased(KeyEvent e) {
    if (!GM.isPlaying) {return;}
    int keyCode = e.getKeyCode();
    if(keyCode == KeyEvent.VK_SPACE) {
      GM.undoMove();
      if (GM.computerGame) {
        GM.undoMove();
        if (GM.getMoves().isEmpty()) {GM.whiteToMove=true;}
        repaint();
        return;
      }
      GM.nextTurn();
      repaint();
    }
  }
  @Override
  public void keyTyped(KeyEvent e) {}
  @Override
  public void keyPressed(KeyEvent e) {}
  @Override
  public void mouseClicked(MouseEvent e) {}
  @Override
  public void mouseEntered(MouseEvent e) {}
  //#endregion
}

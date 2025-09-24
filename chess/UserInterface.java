package chess;

import javax.swing.*;
import javax.swing.border.Border;

import chess.model.GameModel;
import chess.move.Move;
import chess.pieces.Piece;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
  private GameModel GM = Main.GM;
  private int mouse[] = { -1, -1 };
  private List<Move> legalMoves = new ArrayList<>();

  public UserInterface() {
    Main.gridSize = (int) (Main.gridSize * 0.9) / GM.files;
    setPreferredSize(new Dimension(GM.files * Main.gridSize, GM.ranks * Main.gridSize));
    Border lineBorder = BorderFactory.createLineBorder(Color.decode(Main.colors[5]), 5);
    setBorder(lineBorder);
    addMouseListener(this);
    addMouseMotionListener(this);
    addKeyListener(this);
    setFocusable(true);
  }

  @Override
  public void paintComponent(Graphics gfx) {
    super.paintComponent(gfx);

    drawBoard(gfx);
    drawHoveredSquare(gfx);
    drawLegalSquares(gfx);
    drawPieces(gfx);
    drawSelectedPiece(gfx);
  }

  //#region Paint Methods
  private void drawBoard(Graphics gfx) {
    for (int rank = 0; rank < GM.ranks; rank++) {
      for (int file = 0; file < GM.files; file++) {
        gfx.setColor((file + rank) % 2 == 0 ? Color.decode(Main.colors[2]) : Color.decode(Main.colors[1]));
        gfx.fillRect(file * Main.gridSize, rank * Main.gridSize, Main.gridSize, Main.gridSize);
        gfx.setColor(Color.decode(Main.colors[0]));
        gfx.drawRect(file * Main.gridSize, rank * Main.gridSize, Main.gridSize, Main.gridSize);
      }
    }
  }

  private void drawHoveredSquare(Graphics gfx) {
    if (mouse[0] >= 0 && mouse[1] >= 0) {
      gfx.setColor(Color.decode(Main.colors[3]));
      gfx.fillRect(mouse[0] * Main.gridSize, mouse[1] * Main.gridSize, Main.gridSize, Main.gridSize);
    }
  }

  private void drawPieces(Graphics gfx) {
    for (Piece p : GM.getPieces()) {
      int spriteIndex = getSpriteIndex(p);
      drawSprite(gfx, p, spriteIndex);
    }
  }

  private void drawSelectedPiece(Graphics gfx) {
    Piece p = GM.selectedPiece;
    if (p == null) { return; }
    int spriteIndex = getSpriteIndex(p);
    drawSprite(gfx, p, spriteIndex);
  }

  private void drawLegalSquares(Graphics gfx) {
    Color color = Color.decode(Main.colors[3]);
    for (Move move : legalMoves) {
      gfx.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
      gfx.fillRect(move.coords[2] * Main.gridSize, move.coords[3] * Main.gridSize, Main.gridSize, Main.gridSize);
    }
  }

  private int getSpriteIndex(Piece piece){
    int spriteIndex = -1;
    switch (Character.toLowerCase(piece.type)) {
        case 'k': spriteIndex = 0; break;
        case 'q': spriteIndex = 1; break;
        case 'b': spriteIndex = 2; break;
        case 'n': spriteIndex = 3; break;
        case 'r': spriteIndex = 4; break;
        case 'p': spriteIndex = 5; break;
      }
    return spriteIndex;
  }

  private void drawSprite(Graphics gfx, Piece p, int spriteIndex){
    gfx.drawImage(Main.pieceSheet, p.pos[0], p.pos[1], p.pos[0] + Main.gridSize, p.pos[1] + Main.gridSize, spriteIndex * Main.imageScale, (p.isWhite ? 0 : 1) * Main.imageScale, (spriteIndex + 1) * Main.imageScale, ((p.isWhite ? 0 : 1) + 1) * Main.imageScale, getFocusCycleRootAncestor());
  }
  //#endregion

  // #region Event Listener Moves
  @Override
  public void mousePressed(MouseEvent e) {
    int file = e.getX() / Main.gridSize;
    int rank = e.getY() / Main.gridSize;

    Piece piece = GM.getPiece(file, rank);
    if (piece != null && piece.isWhite == GM.isWhiteTurn) {
      GM.selectedPiece = piece;
      GM.getPossibleMoves();
      legalMoves = new ArrayList<>(GM.PossibleMoves);
      legalMoves.removeIf(move -> move.coords[0] != file);
      legalMoves.removeIf(move -> move.coords[1] != rank);
      //System.out.println("legal Moves: " + legalMoves);
      //System.out.println("Possible Moves: " + GM.PossibleMoves);
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    int file = e.getX() / Main.gridSize;
    int rank = e.getY() / Main.gridSize;

    if (GM.selectedPiece != null) {
      Move move = new Move(GM.selectedPiece.coord[0], GM.selectedPiece.coord[1], file, rank);
      boolean isValidMove = false;

      for (Move m : legalMoves) {
        if (Arrays.equals(m.coords, move.coords)) {
          move = m;
          isValidMove = true;
          break;
        }
      }

      if (isValidMove) {
        System.out.println("Making Move: " + move);
        GM.MakeBoardMove(move);
        //GM.getState();
        //GM.printBoard();
        //GM.isWhiteTurn = !GM.isWhiteTurn;
        GM.latestMove = move;
      } else {
        GM.selectedPiece.pos[0] = GM.selectedPiece.coord[0] * Main.gridSize;
        GM.selectedPiece.pos[1] = GM.selectedPiece.coord[1] * Main.gridSize;
      }
    }
    legalMoves.clear();
    GM.selectedPiece = null;
    repaint();
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    mouse[0] = -1; mouse[1] = -1;
    if (GM.selectedPiece != null) {
      GM.selectedPiece.pos[0] = e.getX() - Main.gridSize / 2;
      GM.selectedPiece.pos[1] = e.getY() - Main.gridSize / 2;
    }
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
    mouse[0] = -1; mouse[1] = -1;
    repaint();
  }

  @Override
  public void keyReleased(KeyEvent e) {
    int keyCode = e.getKeyCode();
    if (keyCode == KeyEvent.VK_SPACE) {
      if (GM.latestMove != null) {
        System.out.println("Undo Move: " + GM.latestMove);
        GM.UndoBoardMove(GM.latestMove);
        GM.latestMove = null;
        //GM.printBoard();
        //GM.isWhiteTurn = !GM.isWhiteTurn;
      }
      repaint();
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }
  // #endregion
}

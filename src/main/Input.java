package main;

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener {

    Board board;

    public Input(Board board) {
        this.board = board;
    }

    // mouse motion
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        int xPos = e.getX();
        int yPos = e.getY();

        if (board.selectedPiece != null) {

            if (xPos > 0 && xPos < board.boardWidth)
                board.selectedPiece.xPos = xPos - board.tileSize / 2;
            if (yPos > 0 && yPos < board.boardHeight)
                board.selectedPiece.yPos = yPos - board.tileSize / 2;

                board.repaint();
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        System.out.println(board.ai.evaluate());

    }

    @Override
    public void mousePressed(MouseEvent e) {

        int col = e.getX() / board.tileSize;
        int row = e.getY() / board.tileSize;

        board.selectedPiece = board.getPiece(col, row);

        board.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        int col = e.getX() / board.tileSize;
        int row = e.getY() / board.tileSize;

        if (col < 0) col = 0;
        if (col >= board.cols) col = board.cols - 1;
        if (row < 0) row = 0;
        if (row >= board.rows) row = board.rows - 1;

        if (board.selectedPiece != null) {
            if (board.w2m == board.selectedPiece.isWhite) {
                board.selectedPiece.movePiece(col, row, false);
            } else {
                board.selectedPiece.xPos = board.selectedPiece.col* board.tileSize;
                board.selectedPiece.yPos = board.selectedPiece.row* board.tileSize;
            }
        }
        board.selectedPiece = null;

        board.repaint();
    }

    // key input
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}

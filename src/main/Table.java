package main;

import javax.swing.*;
import java.awt.*;

public class Table extends JPanel {
    Board board;
    public Table(Board board) {
        this.board = board;
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(board.getPreferredSize().width + board.tileSize, board.getPreferredSize().height + board.tileSize));
//        this.setBackground(new Color(141, 114, 40));
        repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(167, 88, 44));
        g2d.fillRect(0, 0, this.getPreferredSize().width, this.getPreferredSize().height);

        g2d.setFont(g2d.getFont().deriveFont(24f));
        g2d.setColor(Color.black);
// draw notation
        for (int r = 0; r < board.rows; r++) {
            g2d.drawString(String.valueOf(board.rows - r), board.boardWidth + board.tileSize - board.tileSize / 3, (r + 1) * board.tileSize + g2d.getFontMetrics(g2d.getFont()).getHeight() / 4);
            g2d.drawString(String.valueOf(board.rows - r), board.tileSize / 6, (r + 1) * board.tileSize + g2d.getFontMetrics(g2d.getFont()).getHeight() / 4);
        }
        for (int c = 0; c < board.cols; c++) {
            g2d.drawString(String.valueOf((char) ('a' + c)), (c + 1) * board.tileSize - g2d.getFontMetrics(g2d.getFont()).getHeight() / 8, board.boardHeight + board.tileSize - board.tileSize / 4 + 5);
            g2d.drawString(String.valueOf((char) ('a' + c)), (c + 1) * board.tileSize - g2d.getFontMetrics(g2d.getFont()).getHeight() / 8, board.tileSize / 6 + 15);
        }

    }


}

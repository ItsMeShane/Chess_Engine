package pieces;

import main.Board;

public class Rook extends Piece{

    public Rook(Board board, boolean isWhite, int x, int y) {
        super(board);
        this.name = 'r';
        this.val = board.rookVal;
        this.isWhite = isWhite;
        this.sprite = sprites[isWhite ? wRook : bRook];
        this.xPos = x * board.tileSize;
        this.yPos = y * board.tileSize;
        this.col = x;
        this.row = y;
        this.fenRepresentation = this.isWhite ? 'R' : 'r';

    }

    public boolean moveCollidesWithPieces(int col, int row) {
        // scan collision left
        if (this.col > col)
            for (int c = this.col - 1; c > col; c--)
                if (board.getPiece(c, this.row) != null)
                    return true;
        // scan collision right
        if (this.col < col)
            for (int c = this.col + 1; c < col; c++)
                if (board.getPiece(c, this.row) != null)
                    return true;
        // scan collision up
        if (this.row > row)
            for (int r = this.row - 1; r > row; r--)
                if (board.getPiece(this.col, r) != null)
                    return true;
        // scan collision down
        if (this.row < row)
            for (int r = this.row + 1; r < row; r++)
                if (board.getPiece(this.col, r) != null)
                    return true;
        return false;
    }

    public boolean validMovement(int col, int row) {
        return this.col == col || this.row == row;
    }

}

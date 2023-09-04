package pieces;

import main.Board;

public class Queen extends Piece{

    public Queen(Board board, boolean isWhite, int x, int y) {
        super(board);
        this.name = 'q';
        this.val = board.queenVal;
        this.isWhite = isWhite;
        this.sprite = sprites[isWhite ? wQueen : bQueen];
        this.xPos = x * board.tileSize;
        this.yPos = y * board.tileSize;
        this.col = x;
        this.row = y;
        this.fenRepresentation = this.isWhite ? 'Q' : 'q';

    }

    public boolean moveCollidesWithPieces(int col, int row) {
        // scan diagonal
        if (Math.abs(this.col - col) == Math.abs(this.row - row)) {
            // scan collision up left
            if (this.col > col && this.row > row)
                for (int i = 1; i < Math.abs(this.col - col); i++)
                    if (board.getPiece((this.col - i), (this.row - i)) != null)
                        return true;
            // scan collision up right
            if (this.col < col && this.row > row)
                for (int i = 1; i < Math.abs(this.col - col); i++)
                    if (board.getPiece((this.col + i), (this.row - i)) != null)
                        return true;
            // scan collision down left
            if (this.col > col && this.row < row)
                for (int i = 1; i < Math.abs(this.col - col); i++)
                    if (board.getPiece((this.col - i), (this.row + i)) != null)
                        return true;
            // scan collision down right
            if (this.col < col && this.row < row)
                for (int i = 1; i < Math.abs(this.col - col); i++)
                    if (board.getPiece((this.col + i), (this.row + i)) != null)
                        return true;
            // scan straight
        } else if (this.col == col || this.row == row) {
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
        }
        return false;
    }

    public boolean validMovement(int col, int row) {
        return this.col == col || this.row == row || Math.abs(this.col - col) == Math.abs(this.row - row);
    }
}

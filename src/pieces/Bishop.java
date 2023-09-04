package pieces;

import main.Board;

public class Bishop extends Piece {

    public Bishop(Board board, boolean isWhite, int x, int y) {
        super(board);
        this.name = 'b';
        this.val = board.bishopVal;
        this.isWhite = isWhite;
        this.sprite = sprites[isWhite ? wBishop : bBishop];
        this.xPos = x * board.tileSize;
        this.yPos = y * board.tileSize;
        this.col = x;
        this.row = y;
        this.fenRepresentation = this.isWhite ? 'B' : 'b';
    }

    public boolean moveCollidesWithPieces(int col, int row) {
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
        return false;
    }

    public boolean validMovement(int col, int row) {
        return Math.abs(this.col - col) == Math.abs(this.row - row);
    }

}
package pieces;

import main.Board;

public class Knight extends Piece{

    public Knight(Board board, boolean isWhite, int x, int y) {
        super(board);
        this.name = 'n';
        this.val = board.knightVal;
        this.isWhite = isWhite;
        this.sprite = sprites[isWhite ? wKnight : bKnight];
        this.xPos = x * board.tileSize;
        this.yPos = y * board.tileSize;
        this.col = x;
        this.row = y;
        this.fenRepresentation = this.isWhite ? 'N' : 'n';

    }

    public boolean validMovement(int col, int row) {
        return Math.abs(this.col - col) * Math.abs(this.row - row) == 2;
    }
}

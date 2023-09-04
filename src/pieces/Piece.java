package pieces;

import main.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Piece {

    protected final static int wKing = 0;
    protected final static int wQueen = 1;
    protected final static int wBishop = 2;
    protected final static int wKnight = 3;
    protected final static int wRook = 4;
    protected final static int wPawn = 5;
    protected final static int bKing = 6;
    protected final static int bQueen = 7;
    protected final static int bBishop = 8;
    protected final static int bKnight = 9;
    protected final static int bRook = 10;
    protected final static int bPawn = 11;

    public char fenRepresentation;

    public BufferedImage[] sprites = new BufferedImage[12];

    public Board board;

    public BufferedImage sprite;
    public int val;
    public char name;
    public boolean isWhite;

    public int xPos;
    public int yPos;

    public int col;
    public int row;

    public boolean isFirstMove = true;
    public int colorVal;



    //TODO: pawn promotion to queen, rook, bishop, knight
    //TODO: retest node count https://youtu.be/U4ogK0MIzqk?t=645
    //TODO: if nodes don't match figure out where mess up

    public Piece(Board board) {
        this.board = board;

        try {
            BufferedImage sheet = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("pieces.png")));
            int scale = sheet.getWidth() / 6;
            int index = 0;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 6; j++) {
                    BufferedImage image = new BufferedImage(board.tileSize, board.tileSize, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = image.createGraphics();
//                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2d.drawImage(sheet.getSubimage(j * scale, i * scale, scale, scale), 0, 0, board.tileSize, board.tileSize, null);
                    sprites[index] = image;
                    index++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void movePiece(int col, int row, boolean verified) {
        Piece pieceXY = board.getPiece(col, row);

        if (!verified)
            if (board.sameTeam(this, pieceXY) || !validMovement(col, row) || board.offScreen(this.xPos, this.yPos) || moveCollidesWithPieces(col, row) || inCheck(col, row)) {
                // returns piece to initial position
                this.xPos = this.col * board.tileSize;
                this.yPos = this.row * board.tileSize;
                return;
            }

        if (!board.sameTeam(this, pieceXY)) {
            board.capture(pieceXY);
        }

        if (this.name == 'p') {
            // en passant
            if (this.row + colorVal == row && Math.abs(this.col - col) == 1 && board.enPassantCol == col && board.enPassantRow == row) {
                board.capture(board.getPiece(col, row - colorVal));
            }
            if (Math.abs(this.row - row) == 2) {
                board.enPassantCol = this.col;
                board.enPassantRow = row + (this.isWhite ? 1 : -1);
            } else {
                board.enPassantCol = -1;
                board.enPassantRow = -1;
            }
        } else if (this.name == 'k') {
            if (validCastleMovement(col, row)) {
                if (this.col > col) {
                    board.getPiece(0, this.row).xPos = 3 * board.tileSize;
                    board.getPiece(0, this.row).col = 3;
                } else {
                    board.getPiece(board.cols - 1, this.row).xPos = 5 * board.tileSize;
                    board.getPiece(board.cols - 1, this.row).col = 5;
                }
            }
        }

        this.col = col;
        this.row = row;
        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;
        this.isFirstMove = false;

        if (this.name == 'p') {
            if (this.row == 0 || this.row == 7) {
                board.pieceList.add(new Queen(board, this.isWhite, this.col, this.row));
                board.capture(this);
            }
        }

        board.changeTurns();

        board.fen.generateFEN();

        String gameState = board.getGameState(this.isWhite);

//        board.halfMoveCount++;
//        System.out.println(board.halfMoveCount);

        if (gameState.equals("Continue")) {
            board.ai.aiMove();
        } else {
            board.gameState = gameState;
            board.repaint();
        }

//        System.out.println(board.fen.fenString);
//        System.out.println(board.ai.moveGenerationTest(1));

    }

    public boolean validMovement(int col, int row) {
        return false;
    }

    public boolean moveCollidesWithPieces(int col, int row) {
        return false;
    }

    public boolean inCheck(int col, int row) {
        return board.checkScanner.isKingChecked(col, row, this.isWhite);
    }


    public boolean canMakeMove(Piece pieceXY, int col, int row) {
        // if valid movement && (piece not null || not the same team) && doesn't collide with other pieces && not in check
        return validMovement(col, row) && (pieceXY == null || !board.sameTeam(pieceXY, this)) && !moveCollidesWithPieces(col, row) && !inCheck(col, row);
    }

    public boolean validCastleMovement(int c, int r) {
        return false;
    }

    public void paintHighlights(Graphics2D g2d) {

        for (int row = 0; row < board.rows; row++) {
            for (int col = 0; col < board.cols; col++) {
                Piece pieceXY = board.getPiece(col, row);
//                if (this.isWhite == board.w2m)
                    if (canMakeMove(pieceXY, col, row)) {
                        g2d.setColor(new Color(43, 178, 182, 176));
                        g2d.fillRect(col * board.tileSize, row * board.tileSize, board.tileSize, board.tileSize);
                    }

            }
        }

    }

}

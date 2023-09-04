package main;

import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Stack;

import static main.Main.ai_verses_ai;

public class Board extends JPanel {

    public int tileSize = 75;
    public int boardSize = 8;
    public int cols = boardSize;
    public int rows = boardSize;
    public int boardWidth = cols * tileSize;
    public int boardHeight = rows * tileSize;

    Color black = new Color(121, 61, 14);
    Color white = new Color(213, 116, 75);

    public ArrayList<Piece> pieceList = new ArrayList<>();
    public ArrayList<Piece> capturedPieceList = new ArrayList<>();

    public FEN fen = new FEN(this);

    public Input input = new Input(this);

    public Piece selectedPiece;

    public CheckScanner checkScanner = new CheckScanner(this);

    public int enPassantCol = -1;
    public int enPassantRow = -1;

    public boolean w2m;

    public AI ai = new AI(this);

    public String gameState = "";

    public Piece[] spareQueens = new Piece[cols * 2];
    public Piece[] spareRooks = new Piece[cols * 2];
    public Piece[] spareBishops = new Piece[cols * 2];
    public Piece[] spareKnights = new Piece[cols * 2];

    public Stack<String> fenStack = new Stack<>();

    public int halfMoveCount = 0;


    public final int pawnVal = 100;
    public final int knightVal = 300;
    public final int bishopVal = 300;
    public final int rookVal = 500;
    public final int queenVal = 900;


    public Board() {
        this.setPreferredSize(new Dimension(boardWidth, boardHeight));

        setUpPieces();

    }

    void addListeners() {
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        this.addKeyListener(input);
    }

    public void setUpPieces() {

        fen.loadFEN(fen.fenStartingPosition);

        //delete
        pieceList.add(wKing);
        pieceList.add(bKing);


        for (int i = 0; i < cols * 2; i++) {
            spareQueens[i] = new Queen(this, i < cols, -1, -1);
            spareRooks[i] = new Rook(this, i < cols, -1, -1);
            spareBishops[i] = new Bishop(this, i < cols, -1, -1);
            spareKnights[i] = new Knight(this, i < cols, -1, -1);
        }

    }

    int remainingQueens = spareQueens.length - 1;

    public Piece getSpareQueen(boolean isWhite) {
        return spareQueens[isWhite ? remainingQueens - cols : remainingQueens];
    }

    int remainingRooks = spareRooks.length - 1;

    public Piece getSpareRook(boolean isWhite) {
        return spareRooks[isWhite ? remainingRooks - cols : remainingRooks];
    }

    int remainingBishops = spareBishops.length - 1;

    public Piece getSpareBishop(boolean isWhite) {
        return spareBishops[isWhite ? remainingBishops - cols : remainingBishops];
    }

    int remainingKnights = spareKnights.length - 1;

    public Piece getSpareKnight(boolean isWhite) {
        return spareKnights[isWhite ? remainingKnights - cols : remainingKnights];
    }

    public Piece getPiece(int col, int row) {
        for (Piece p : pieceList)
            if (p != null)
                if (p.col == col && p.row == row)
                    return p;
        return null;
    }

    public void capture(Piece piece) {
        if (piece != null) {
            capturedPieceList.add(piece);
            pieceList.remove(piece);
        }
    }

    public void changeTurns() {
        w2m = !w2m;
    }


    public int playerPieceCount(boolean isWhite) {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Piece pieceXY = getPiece(c, r);
                if (pieceXY != null && pieceXY.isWhite == isWhite) {
                    count++;
                }
            }
        }
        return count;
    }


    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                g2d.setColor((c + r) % 2 == 1 ? black : white);
                g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
            }
        }
        if (!ai_verses_ai) {
//            if (false)
            if (selectedPiece != null) {
                g2d.setColor(new Color(83, 229, 55, 139));
                g2d.fillRect(selectedPiece.col * tileSize, selectedPiece.row * tileSize, tileSize, tileSize);
                selectedPiece.paintHighlights(g2d);
            }

            for (Piece piece : pieceList) {
                if (piece != null && piece != selectedPiece && !ai_verses_ai)
                    g2d.drawImage(piece.sprite, piece.xPos, piece.yPos, null);
            }
            if (selectedPiece != null && !ai_verses_ai) {
                g2d.drawImage(selectedPiece.sprite, selectedPiece.xPos - 5, selectedPiece.yPos - 5, selectedPiece.sprite.getWidth() + 10, selectedPiece.sprite.getHeight() + 10, null);
            }
        } else {
            try {
                for (Piece piece : pieceList)
                    if (piece != null)
                        g2d.drawImage(piece.sprite, piece.xPos, piece.yPos, null);
            } catch (ConcurrentModificationException cme) {
                System.out.println("fuck");
            }
        }
        if (!gameState.equals("")) {
            g2d.setColor(Color.green.darker());
            g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 33));
            g2d.drawString(gameState, boardWidth / 2 - (int) g2d.getFontMetrics(g2d.getFont()).getStringBounds(gameState, g2d).getWidth() / 2, boardHeight / 2 - tileSize / 2);
        }

    }


    public boolean sameTeam(Piece p1, Piece p2) {
        if (p1 == null || p2 == null)
            return false;
        return p1.isWhite == p2.isWhite;
    }

    public boolean noValidMoves(boolean isWhite) {
        return checkScanner.noValidMoves(isWhite);
    }

    public String getGameState(boolean isWhite) {
        if (noValidMoves(isWhite)) {
            Piece king = findKing(w2m);
            if (king != null)
                if (king.inCheck(king.col, king.row)) {
                    System.out.println(isWhite ? "White Wins!" : "Black Wins!");
                    return isWhite ? "White Wins!" : "Black Wins!";
                } else {
                    System.out.println("Stalemate!");
                    return "Stalemate!";
                }
        }
        if (lackOfMaterial(true) && lackOfMaterial(false)) {
            System.out.println("Lack of material!");
            return "Lack of material!";
        }
        return "Continue";
    }

    ArrayList<Character> pieceNames(boolean isWhite) {
        ArrayList<Character> names = new ArrayList<>();

        Piece pieceXY;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                pieceXY = getPiece(c, r);
                if (pieceXY != null && pieceXY.isWhite == isWhite) {
                    names.add(pieceXY.name);
                }
            }
        }

        return names;
    }

    boolean lackOfMaterial(boolean isWhite) {
        ArrayList<Character> names = pieceNames(isWhite);

        if (names.contains("queen") || names.contains("rook") || names.contains("pawn"))
            return false;

        return names.size() < 3;
    }


    private final Piece wKing = new King(this, true, 4, 7);
    private final Piece bKing = new King(this, false, 4, 0);

    public Piece findKing(boolean isWhite) {

        return isWhite ? wKing : bKing;

/*
        for (Piece k : pieceList)
            if (k != null)
                if (k.name == 'k' && k.isWhite == isWhite)
                    return k;
        return null;
        */
    }

    public boolean offScreen(int x, int y) {
        return !new Rectangle(boardWidth, boardHeight).contains(new Point(x + tileSize / 2, y + tileSize / 2));
    }

    public String location(int col, int row) {
        String loc = "";
        char c = 'a';
        for (int i = 0; i < col; i++) c++;
        loc += c;
        loc += rows - row;
        return loc;
    }


    public void makeMove(Move move) {
        if (move != null) {

            Piece piece = move.piece;
            Piece capture = move.capture;
            Piece promo = move.promo;
            int col = move.col;
            int row = move.row;

            piece.col = col;
            piece.row = row;
            piece.xPos = col * tileSize;
            piece.yPos = row * tileSize;

            capture(capture);
            capture(promo);

            changeTurns();
        }
    }


    public void unMakeMove(Move move) {
        if (move != null) {

            Piece piece = move.piece;
            Piece capture = move.capture;
            Piece promo = move.promo;
            int col = move.oldCol;
            int row = move.oldRow;

            piece.col = col;
            piece.row = row;
            piece.xPos = col * tileSize;
            piece.yPos = row * tileSize;

            if (capture != null) {
                pieceList.add(capture);
                capturedPieceList.remove(capture);
            }
            if (promo != null) {
                pieceList.add(promo);
                capturedPieceList.remove(promo);
            }

            changeTurns();

        }
    }


    Piece getCapturedPiece(char name, boolean isWhite) {
        Piece piece;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                piece = getPiece(c, r);
                if (piece != null && piece.name == name && piece.isWhite == isWhite) {
                    return piece;
                }
            }
        }

        return null;
    }


    public int pieceCount(boolean isWhite, char name) {
        int count = 0;
        for (Piece piece : pieceList)
            if (piece != null)
                if (piece.isWhite == isWhite && piece.name == name)
                    count++;
        return count;
    }


}
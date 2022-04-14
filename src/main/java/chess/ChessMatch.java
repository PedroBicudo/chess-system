package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

    private Board board;
    private int turn;
    private Color currentPlayer;

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public void initialSetup() {
        // Black
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK));

        // White
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);
        nextTurn();
        return (ChessPiece) capturedPiece;

    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);

        return board.piece(position).possibleMoves();
    }

    private void validateTargetPosition(Position source, Position target) {
        Piece piece = board.piece(source);
        if (!piece.possibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece on source position.");
        }

        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw  new ChessException("The chosen piece is not yours.");
        }

        if (!board.piece(position).isThereAnyPossibleMoves()) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    private Piece makeMove(Position source, Position target) {
        Piece piece = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);

        board.placePiece(piece, target);

        return capturedPiece;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] matrizChessPieces = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i=0; i < board.getRows(); i++) {
            for (int j=0; j < board.getColumns(); j++) {
                matrizChessPieces[i][j] = (ChessPiece) board.piece(i, j);
            }
        }

        return matrizChessPieces;
    }

    private void nextTurn() {
        turn++;
        currentPlayer = currentPlayer.equals(Color.WHITE)? Color.BLACK: Color.WHITE;
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

}

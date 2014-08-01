package com.chess.engine.classic.player;

import java.util.List;

import com.chess.engine.classic.Alliance;
import com.chess.engine.classic.board.Board;
import com.chess.engine.classic.board.Move;
import com.chess.engine.classic.board.Move.KingSideCastleMove;
import com.chess.engine.classic.board.Move.QueenSideCastleMove;
import com.chess.engine.classic.board.Tile;
import com.chess.engine.classic.pieces.King;
import com.chess.engine.classic.pieces.Piece;
import com.chess.engine.classic.pieces.Rook;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class BlackPlayer extends Player {

    public BlackPlayer(final Board board,
                       final List<Move> whiteStandardLegals,
                       final List<Move> blackStandardLegals) {
        super(board, blackStandardLegals, whiteStandardLegals);
    }

    @Override
    public List<Move> calculateKingCastles(final List<Move> playerLegals,
                                           final List<Move> opponentLegals) {

        final ImmutableList.Builder<Move> builder = new Builder<>();

        if(this.playerKing.isFirstMove() && !this.playerKing.isInCheck(playerLegals)) {
            //blacks king side castle
            if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(7);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if(Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() && Player.calculateAttacksOnTile(6, opponentLegals).isEmpty() &&
                            rookTile.getPiece() instanceof Rook) {
                        builder.add(new KingSideCastleMove(this.playerKing.getPiecePosition(), 6, this.playerKing, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
                    }
                }
            }
            //blacks queen side castle
            if(!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() &&
                    !this.board.getTile(3).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(0);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if(Player.calculateAttacksOnTile(1, opponentLegals).isEmpty() && Player.calculateAttacksOnTile(2, opponentLegals).isEmpty() &&
                       Player.calculateAttacksOnTile(3, opponentLegals).isEmpty() && rookTile.getPiece() instanceof Rook) {
                        builder.add(new QueenSideCastleMove(this.playerKing.getPiecePosition(), 2, this.playerKing, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
                    }
                }
            }
        }
        return builder.build();
    }

    @Override
    public WhitePlayer getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    public List<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public String toString() {
        return Alliance.BLACK.toString();
    }

    @Override
    protected King findKing() {
        for(final Piece p : this.board.getBlackPieces()) {
            if(p.isKing()) {
                return (King) p;
            }
        }
        throw new RuntimeException("Should not reach here! Black King could not be established!");
    }

}

package DataStructure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TicTacToe extends Board {

    private Tile bestMove;

    /**
     * The constructor that initializes the board with the tiles and it's neighbors
     */
    public TicTacToe() {
        super(3, Piece.PIECE1);
    }

    @Override
    void defaultPlayingField() {
    }
    //@Override
   // public Tile algorithmMove(int depth){
    //    return null;
   // }

    @Override
    public boolean validMove(Tile tile) {
        return tile != null && tile.getPiece() == Piece.NULL;
    }

    @Override
    public Status checkCurrentGameStatus() {
        Status status = Status.DRAW;
        for (Tile tile : getTiles()) {
            if (tile.getPiece() != Piece.NULL) {
                for (Neighbor direction : Neighbor.values()) {
                    if (tile.getNeighbor(direction) != null && tile.getNeighbor(direction).getNeighbor(direction) != null
                            && tile.getNeighbor(direction).getPiece() == tile.getPiece()
                            && tile.getNeighbor(direction).getNeighbor(direction).getPiece() == tile.getPiece()
                            ) {
                        return (tile.getPiece() == Piece.PIECE1) ? Status.PIECE1_WON : Status.PIECE2_WON;
                    }
                }
            } else {
                status = Status.NOT_FINISHED;
            }
        }
        return status;
    }

    @Override
    public int placeMove(Tile tile) {
        if(validMove(tile)) {
            tile.setPiece(getCurrentPlayer(), getProgression());
            swapPlayer();
        }
        return 0;
    }

    @Override
    public Tile algorithmMove(int depth) {

        miniMax(0, getCurrentPlayer());
        placeMove(bestMove);
        return bestMove;
    }

    private int miniMax(int depth, Piece toWin) {
        Status status = checkCurrentGameStatus();

        if(status == Status.PIECE1_WON){
            return (toWin == Piece.PIECE1) ? 10 : -10;
        } else if( status == Status.PIECE2_WON) {
            return (toWin == Piece.PIECE2) ? 10 : -10;
        }

        int min = 999;
        int max = -999;
        List<Tile> tiles = getEmptyTiles();
        if (tiles.isEmpty()){ return 0; }

        for(int i = 0; i < tiles.size(); i++){
            if(getCurrentPlayer() == toWin){
                placeMove(tiles.get(i));
                int score = miniMax((depth+1), toWin);
                max = Math.max(score, max);

                if(score >= 0 && depth == 0){
                    bestMove = tiles.get(i);
                }

                if(i == tiles.size()-1 && max < 0 && depth == 0){
                    bestMove = tiles.get(i);
                }
            }else {
                placeMove(tiles.get(i));
                int score = miniMax((depth+1), toWin);
                min = Math.min(score, min);

                if(min == -10){
                    tiles.get(i).setPiece(Piece.NULL, getProgression());
                    swapPlayer();
                    break;
                }
            }

            tiles.get(i).setPiece(Piece.NULL, getProgression());
            swapPlayer();
        }

        return (getCurrentPlayer() == toWin) ? max : min;
    }

    @Override
    public void printBoard() {
       /*
        | x | o |   |
        -------------
        | x | o | x |
        -------------
        | o |   | o |
        */

        System.out.println(
                "| "+getPrintableChar(getTileByPosition(1,1).getPiece())+" | "+getPrintableChar(getTileByPosition(2,1).getPiece())+" | "+getPrintableChar(getTileByPosition(3,1).getPiece())+ " |\n" +
                        "-------------\n" +
                        "| "+getPrintableChar(getTileByPosition(1,2).getPiece())+" | "+getPrintableChar(getTileByPosition(2,2).getPiece())+" | "+getPrintableChar(getTileByPosition(3,2).getPiece())+ " |\n" +
                        "-------------\n" +
                        "| "+getPrintableChar(getTileByPosition(1,3).getPiece())+" | "+getPrintableChar(getTileByPosition(2,3).getPiece())+" | "+getPrintableChar(getTileByPosition(3,3).getPiece())+ " |\n"
        );

    }

    public List<Tile> getEmptyTiles(){
        return Arrays.stream(getTiles()).filter(tile -> tile.getPiece() == Piece.NULL).collect(Collectors.toList());
    }

    private String  getPrintableChar(Piece piece){
        if(piece == Piece.NULL){
            return " ";
        } else {
            return (piece == Piece.PIECE1) ? "x" : "o";
        }
    }
}
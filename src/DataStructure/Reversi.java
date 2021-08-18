package DataStructure;

import java.util.AbstractMap;
import java.util.ArrayList;

public class Reversi extends Board {

    /**
     * The constructor that initializes the board with the tiles and it's neighbors
     *
     * @param startingPlayer The starting player for the board
     */
    public Reversi(Piece startingPlayer) {
        super(8, startingPlayer);
    }


    private int dangerZones[][] = {{2, 2}, {7, 2}, {2, 7}, {7, 7}};
    private int warningZones[][] = {{2, 1}, {7, 1}, {1, 2}, {8, 2}, {1, 7}, {8, 7}, {2, 8}, {7, 8}};
    private int successZones[][] = {{1, 1}, {8, 1}, {1, 8}, {8, 8}};

    /**
     * Black box. Just call it and it will place a move
     * @param depth The thoroughness of the algorithm
     */
    @Override
    public Tile algorithmMove(int depth) {

        Tile bestMove = null;
        int bestWeight = 0;

        ArrayList<AbstractMap.SimpleEntry<Tile, Integer>> moves = miniMax(depth);


        //System.out.print("algorithmMove(): Moves found: ");
        for (AbstractMap.SimpleEntry<Tile, Integer> entry:moves) {
            //System.out.print(entry.getKey().getPosXYText() + " Weight: " + entry.getValue() + " - ");


            if (bestMove == null) {
                bestMove = entry.getKey();
                bestWeight = entry.getValue();
                continue;
            }

            if (inZone(entry.getKey(), successZones)) {
                bestMove = entry.getKey();
                bestWeight = entry.getValue();
                break;
            }

            if (inZone(bestMove, dangerZones) && !inZone(entry.getKey(), dangerZones)) {
                bestMove = entry.getKey();
                bestWeight = entry.getValue();
                continue;
            }

            if (inZone(bestMove, warningZones) && !inZone(entry.getKey(), warningZones)){
                bestMove = entry.getKey();
                bestWeight = entry.getValue();
                continue;
            }

            if (entry.getValue() > bestWeight) {
                bestMove = entry.getKey();
                bestWeight = entry.getValue();
            }
        }


        if (bestMove != null) {
            System.out.println("[Reversi] Chosen move: " + bestMove.getPosXYText() + " Weight: " + bestWeight);
            placeMove(bestMove);
        } else
            System.out.println("[Reversi] No moves can be made");

        return bestMove;
    }

    private boolean inZone(Tile move, int[][] zone) {
        for (int[] aZone : zone) {
            if (move.getPosX() == aZone[0] && move.getPosY() == aZone[1])
                return true;
        }
        return false;
    }

    /**
     * Calculates all possible moves and then some.
     * @param depth Sets the maximum depth the algorithm will go
     * @return ArrayList<AbstractMap.SimpleEntry<Tile, Integer>> A key/value pair with as tile, the possible tile as move and the integer is it's calculated success
     */
    public ArrayList<AbstractMap.SimpleEntry<Tile, Integer>> miniMax (int depth) {

        // The array for remembering progress on a upper level
        int[] progress = new int[depth];
        for (int i = 0; i < depth; i++){
            progress[i] = 0;
        }

        int length = this.getTiles().length;
        int startProgression = this.getProgression();
        int totalWeight = 0;
        int i = 0;
        int count = 0;
        boolean keepGoing = true;

        Piece player = getCurrentPlayer();
        ArrayList<AbstractMap.SimpleEntry<Tile, Integer>> possibleMoves = new ArrayList<>();

        while (keepGoing) {
            int weight;

            count++;
            progress[this.getProgression() - startProgression] = i;

            // Break and give error message if going out of bounds
            if (i > length - 1) {
                System.out.println("[Reversi] Illegal index i=" + i + " depth: " + (this.getProgression() - startProgression));
                break;
            }

            // True if the weight is one or more
            if ((weight = placeMove(this.getTiles()[i])) > 0) {

                if (this.getProgression() - startProgression - 1 == 0) {
                    if (possibleMoves.size() > 0) {
                        // Add size of previous pass
                        possibleMoves.get(possibleMoves.size() - 1).setValue(totalWeight + possibleMoves.get(possibleMoves.size() - 1).getValue());
                        totalWeight = 0;
                    }

                    // Add new possible move
                    possibleMoves.add(new AbstractMap.SimpleEntry<>(this.getTiles()[i], weight));

                    //System.out.print(this.getTiles()[i].getPosXYText() + " W:" + weight + " D:" + (this.getProgression() - startProgression) + " C:" + i + " P:" + this.getCurrentPlayer() + " - ");
                }

                // If the max depth is reached, back back one step
                if (this.getProgression() - startProgression >= depth){
                    revertMove();
                    i = progress[this.getProgression() - startProgression]; // Get the progress of the previous move
                } else
                    i = 0;

                // The opponent takes weight of the total weight
                if (player == this.getCurrentPlayer())
                    totalWeight += weight;
                else
                    totalWeight -= weight;
            }

            while (i >= length - 1){ // Revert moves until length is less then the size of Tiles[]

                // Base case: if at depth 0 and the maximum length has been reached, break;
                if (this.getProgression() - startProgression == 0) {

                    // Add the last weight to the possible moves
                    if (possibleMoves.size() > 1)
                        possibleMoves.get(possibleMoves.size() - 1).setValue(totalWeight + possibleMoves.get(possibleMoves.size() - 1).getValue());

                    // Stop the loop
                    keepGoing = false;
                    break;
                }

                // Go back one step if whole board is weighed
                revertMove();
                i = progress[this.getProgression() - startProgression];
            }

            i++;
        }

        System.out.println("[Reversi] MiniMax moves found " + depth + " deep: " + count);

        return possibleMoves;
    }

    /**
     * Returns the number of opponent pieces the move can take
     * @param tile The tile you want to weigh
     * @return int The number of opponent pieces
     */
    public int moveWeight(Tile tile) {

        // Get the tile that needs to be checked
        if (tile.getPiece() != Piece.NULL || getCurrentPlayer() == Piece.NULL)
            return 0; // If the disc on the tile is not null or if the disc to place is null, return false;

        // Get the disc of the opponent
        Piece opponentDisc = (getCurrentPlayer() == Piece.PIECE1 ? Piece.PIECE2 : Piece.PIECE1);

        int totalWin = 0;
        for(Neighbor neighbor: Neighbor.values()) {
            Tile workingOn = tile.getNeighbor(neighbor);

            int opponentDiscCount = 0;

            // While not out of bounds and equals to the opponents disc
            while (workingOn != null && workingOn.getPiece() == opponentDisc) {
                workingOn = workingOn.getNeighbor(neighbor);
                opponentDiscCount++;
            }

            // If the direction consists of 1 or more opponent discs
            if (opponentDiscCount > 0 && workingOn != null && workingOn.getPiece() == getCurrentPlayer()) {
                totalWin = totalWin + opponentDiscCount;
            }
        }
        return totalWin;
    }

    @Override
    void defaultPlayingField() {
        // Starting board Reversi
        getTileByPosition(4, 4).setPiece(Piece.PIECE1, 0);
        getTileByPosition(5, 5).setPiece(Piece.PIECE1, 0);
        getTileByPosition(4, 5).setPiece(Piece.PIECE2, 0);
        getTileByPosition(5, 4).setPiece(Piece.PIECE2, 0);
    }

    @Override
    public boolean validMove(Tile tile) {
        return moveWeight(tile) > 0;
    }

    @Override
    public int placeMove(Tile tile) {
        // Get the tile that needs to be checked
        if (tile.getPiece() != Piece.NULL || getCurrentPlayer() == Piece.NULL)
            return 0; // If the disc on the tile is not null or if the disc to place is null return 0

        Piece opponentDisc = (getCurrentPlayer() == Piece.PIECE1 ? Piece.PIECE2 : Piece.PIECE1);

        this.incrementProgression();
        tile.setPiece(getCurrentPlayer(), this.getProgression()); // Set on an empty tile

        int opponentDiscCount = 0;
        for(Neighbor neighbor: Neighbor.values()) {

            // Work towards the opposing tile
            Tile workingOn = tile.getNeighbor(neighbor);
            while (workingOn != null && workingOn.getPiece() == opponentDisc){
                workingOn = workingOn.getNeighbor(neighbor);
            }

            // If the next tile has this player's disc then repeat the same loop with replacing the pieces until the player disc has been reached
            if (workingOn != null && workingOn != tile.getNeighbor(neighbor) && workingOn.getPiece() == getCurrentPlayer()) {
                workingOn = tile.getNeighbor(neighbor);
                while (workingOn != null && workingOn.getPiece() == opponentDisc){
                    workingOn.setPiece(getCurrentPlayer(), this.getProgression());
                    workingOn = workingOn.getNeighbor(neighbor);
                    opponentDiscCount++;
                }
            }
        }

        if (opponentDiscCount < 1)
            this.revertMove();

        this.swapPlayer();

        return opponentDiscCount;
    }

    @Override
    public Status checkCurrentGameStatus() {
        int score = 0; // If the score is bigger than 0 then PIECE1 won, if less then it's PIECE2 otherwise a DRAW

        for (Tile tile : getTiles()) {
            switch (tile.getPiece()){
                case NULL: // If the board is not full, the game is certainly not finished
                    return Status.NOT_FINISHED;
                case PIECE1:
                    score++;
                    break;
                case PIECE2:
                    score--;
                    break;
            }
        }

        if (score > 0)
            return Status.PIECE1_WON;
        else if (score < 0)
            return Status.PIECE2_WON;

        return Status.DRAW;
    }

    @Override
    public void printBoard() {
        String status = "";
        String player = "";

        switch (getCurrentPlayer()) {
            case PIECE1:
                player = "white(o) piece1";
                break;
            case PIECE2:
                player = "black(x) piece2";
                break;
        }

        switch (checkCurrentGameStatus()) {
            case PIECE1_WON:
                status = "white(o) won!";
                break;
            case PIECE2_WON:
                status = "black(x) won!";
                break;
            case NOT_FINISHED:
                status = "unfinished...";
                break;
            case DRAW:
                status = "draw!";
                break;
        }

        System.out.println("Board print - Step: " + this.getProgression() + " Status: " + status + " Current player: " + player);
        System.out.print("  1 2 3 4 5 6 7 8");

        int y = 1;
        for (Tile tile : getTiles()) {

            if (tile.getPosY() == y) {
                System.out.print("\n" + y);
                y++;
            }

            switch (tile.getPiece()) {
                case NULL:
                    System.out.print("  ");
                    break;
                case PIECE1:
                    System.out.print(" o"); // White
                    break;
                case PIECE2:
                    System.out.print(" x"); // Black
                    break;
            }
        }
        System.out.print("\n");
    }

}
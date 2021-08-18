package DataStructure;

public abstract class Board {
    private int dimension;
    private int progression;
    private Tile[] tiles;
    private Piece currentPlayer;

    /**
     *  The constructor that initializes the board with the tiles and it's neighbors
     *
     *  @param dimension The width/height of the square board
     */
    public Board(int dimension, Piece player) {
        this.setCurrentPlayer(player);
        this.dimension = dimension;
        this.progression = 0;
        this.tiles = new Tile[dimension * dimension];

        // Initialize all the tiles and their x/y coordinates
        int initX = 1, initY = 1;
        for (int i = 0; i < this.tiles.length; i++) {
            tiles[i] = new Tile(initX, initY);

            if (initX < dimension)
                initX++;
            else {
                initX = 1;
                initY++;
            }
        }

        // Set the neighbors
        for (int posY = 0; posY < dimension; posY++) {
            for (int posX = 0; posX < dimension; posX++) {
                if (posX > 0) {
                    tiles[posX + (posY * dimension)].setLeftNeighbor(tiles[posX - 1 + (posY * dimension)]);
                }
                if (posX < dimension - 1) {
                    tiles[posX + (posY * dimension)].setRightNeighbor(tiles[posX + 1 + (posY * dimension)]);
                }
                if (posY > 0) {
                    tiles[posX + (posY * dimension)].setTopNeighbor(tiles[posX + ((posY - 1) * dimension)]);
                }
                if (posY < dimension - 1) {
                    tiles[posX + (posY * dimension)].setBottomNeighbor(tiles[posX + ((posY + 1) * dimension)]);
                }
            }
        }

        System.out.println("[Board] Board created with dimensions " + dimension + "x" + dimension);
    }

    /**
     * @return Tile[] The tile matrix
     */
    public Tile[] getTiles (){
        return tiles;
    }

    /**
     * Resets the tiles to have no piece, then calls defaultPlayingField()
     */
    public void resetGame() {
        for (Tile tile : tiles) {
            tile.setPiece(Piece.NULL, 0);
        }
        resetProgression();
        defaultPlayingField();
    }

    /**
     * Override for custom starting board
     */
    abstract void defaultPlayingField();

    /**
     * @param tile The tile you want to check
     * @return A yes or no answer
     */
    public abstract boolean validMove(Tile tile);

    /**
     * Returns the a Status enumerable witch tells you the state of the game
     * @return Status()
     */
    public abstract Status checkCurrentGameStatus();

    /**
     * Places a tile on the board depending on the game type
     * @param tile The tile you want to place
     * @return The amount of pieces placed
     */
    public abstract int placeMove(Tile tile);

    /**
     * Prints all tiles with their x/y coordinates and the current piece FOR DEBUGGING
     */
    public void printBoard() {
        for (Tile tile : tiles) {
            System.out.print(tile.getPosXYText());
            System.out.print(" Piece:" + tile.getPiece());
            if (tile.getNeighbor(Neighbor.TOP) != null)
                System.out.print(" Top " + tile.getNeighbor(Neighbor.TOP).getPosXYText());
            if (tile.getNeighbor(Neighbor.LEFT) != null)
                System.out.print(" Left " + tile.getNeighbor(Neighbor.LEFT).getPosXYText());
            if (tile.getNeighbor(Neighbor.RIGHT) != null)
                System.out.print(" Right " + tile.getNeighbor(Neighbor.RIGHT).getPosXYText());
            if (tile.getNeighbor(Neighbor.BOTTOM) != null)
                System.out.print(" Bottom " + tile.getNeighbor(Neighbor.BOTTOM).getPosXYText());
            System.out.print("\n");
        }
    }

    /**
     * Returns a certain tile using it's x/y coordinate
     * @param x Give the X position of the tile
     * @param y Give the Y position of the tile
     * @return Tile() The tile you wanted
     */
    public Tile getTileByPosition (int x, int y){
        return tiles[(x - 1) + ((y - 1) * this.dimension)];
    }

    /**
     * Returns the index on Tiles
     * @param tile The tile you want the index of
     * @return The tile index
     */
    public int getTileIndex (Tile tile) {
        for (int i = 0; i < getTiles().length; i++) {
            if (getTiles()[i] == tile)
                return i;
        }
        return -1;
    }

    /**
     * Get the current game progress
     * @return Game progress
     */
    public int getProgression() {
        return progression;
    }

    /**
     * Sets the game progress to zero
     */
    public void resetProgression() {
        this.progression = 0;
    }

    /**
     * Increments the game progression
     */
    public void incrementProgression() {
        this.progression++;
    }

    /**
     * Decrements the game progression
     */
    private void decrementProgression() {
        this.progression--;
    }

    /**
     * Gets current player
     * @return  currentPlayer The current player
     */
    public Piece getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets current player
     * @param currentPlayer The player you want to set
     */
    public void setCurrentPlayer(Piece currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Swaps current player
     */
    public void swapPlayer() {
        if (this.currentPlayer == Piece.PIECE1)
            this.currentPlayer = Piece.PIECE2;
        else
            this.currentPlayer = Piece.PIECE1;
    }


    /**
     * Makes an AI move
     *
     * @param depth
     * @return move
     */
    public abstract Tile algorithmMove(int depth);

    /**
     * Undoes a move
     */
    public void revertMove() {
        if (progression < 1)
            return;

        swapPlayer();

        decrementProgression();
        for (Tile tile:getTiles()) {
            tile.revertPiece(progression);
        }
    }

    /**
     * Returns the board dimension
     * @return int Dimension
     */
    public int getDimension() {
        return dimension;
    }
}
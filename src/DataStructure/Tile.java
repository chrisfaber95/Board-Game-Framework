package DataStructure;

import java.util.AbstractMap.SimpleEntry;
import java.util.Stack;

public class Tile {
    private int posX, posY;
    private Tile topNeighbor, bottomNeighbor, leftNeighbor, rightNeighbor;
    private Stack<SimpleEntry<Integer, Piece>> pieceHistory;
    
    /**
     * Initializes the tile with it's empty piece, x and y positions
     * @param x The x position
     * @param y The y position
     */
    Tile(int x, int y) {
        this.posX = x;
        this.posY = y;
        this.pieceHistory = new Stack<>();
    }

    /**
     * Undoes a move on this tile
     * @param progression The state of advancement of the game
     * @return A true or false answer if it did
     */
    public boolean revertPiece (int progression) {
        if (this.pieceHistory.peek().getKey() > progression) {
            this.pieceHistory.pop();
            return true;
        }
        return false;
    }

    /**
     * Sets the piece
     * @param piece The piece you want to set
     * @param progress The current advancement of the game
      */
    public void setPiece (Piece piece, int progress) {
        this.pieceHistory.add(new SimpleEntry(progress, piece));
        ///this.piece = piece;
    }

    /**
     * Getter for the current piece
     * @return Piece The current piece
     */
    public Piece getPiece() {
        return  pieceHistory.peek().getValue();
    }

    /**
     * Getter for the tile position
     * @return int The x position
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Getter for the tile position
     * @return int The y position
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Returns a position x/y string
     * @return int The x position
     */
    public String getPosXYText() {
        return "X:" + posX + " Y:" + posY;
    }

    /**
     * Sets the top neighbor
     * @param topNeighbor Tile() Reference to the tile you want to set
     */
    void setTopNeighbor(Tile topNeighbor) {
        this.topNeighbor = topNeighbor;
    }

    /**
     * Sets the bottom neighbor
     * @param bottomNeighbor Tile() Reference to the tile you want to set
     */
    void setBottomNeighbor(Tile bottomNeighbor) {
        this.bottomNeighbor = bottomNeighbor;
    }

    /**
     * Sets the left neighbor
     * @param leftNeighbor Tile() Reference to the tile you want to set
     */
    void setLeftNeighbor(Tile leftNeighbor) {
        this.leftNeighbor = leftNeighbor;
    }

    /**
     * Sets the right neighbor
     * @param rightNeighbor Tile() Reference to the tile you want to set
     */
    void setRightNeighbor(Tile rightNeighbor) {
        this.rightNeighbor = rightNeighbor;
    }

    /**
     * Gets the neighbor via enum Neighbor
     * @param neighbor Enum The neighbor type
     * @return Tile() The requested neighbor
     */
    public Tile getNeighbor(Neighbor neighbor){
        switch (neighbor){
            case TOP:
                return topNeighbor;
            case TOPRIGHT:
                return (topNeighbor != null ? topNeighbor.getNeighbor(Neighbor.RIGHT) : null);
            case RIGHT:
                return rightNeighbor;
            case BOTTOMRIGHT:
                return (bottomNeighbor != null ? bottomNeighbor.getNeighbor(Neighbor.RIGHT) : null);
            case BOTTOM:
                return bottomNeighbor;
            case BOTTOMLEFT:
                return (bottomNeighbor != null ? bottomNeighbor.getNeighbor(Neighbor.LEFT) : null);
            case LEFT:
                return leftNeighbor;
            case TOPLEFT:
                return (topNeighbor != null ? topNeighbor.getNeighbor(Neighbor.LEFT) : null);
            default:
                return null;
        }
    }
}
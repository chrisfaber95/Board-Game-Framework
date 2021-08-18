package DatGui;

import DataStructure.Board;
import DataStructure.Piece;
import javafx.scene.layout.Pane;

import java.util.ArrayList;


public class GameController {
    private String playerName, ip;
    private int port;
    private Board board;
    private Gui gui;

    GameController (Board board, String name, String ip, int port) {
        this.playerName = name;
        this.ip = ip;
        this.port = port;
        this.board = board;
        this.board.resetGame();
        init();
    }

    GameController (Board board) {
        this.board = board;
        this.board.resetGame();
        init();
    }

    void tileClick (ArrayList<Pane> paneList, Pane pane) {
        Piece currentPlayer = this.board.getCurrentPlayer();
        for (int i = 0; i < paneList.size(); i++) {
            if (paneList.get(i) == pane){
                if (this.board.placeMove(this.board.getTiles()[i]) > 0)
                    pane.getChildren().add(gui.getColoredPiece(currentPlayer));
            }
        }
        gui.refreshPanes(this.board);
    }

    void algoButtonClick () {
        this.board.algorithmMove(3);
        gui.refreshPanes(this.board);
    }

    public void resetClick() {
        this.board.resetGame();
        gui.refreshPanes(this.board);
    }

    private void init() {
        gui = new Gui(this.board, this);
    }

    public void revertMove() {
        this.board.revertMove();
        gui.refreshPanes(board);
    }

    public void switchPlayer() {
        board.swapPlayer();
        gui.refreshPanes(board);
    }
}

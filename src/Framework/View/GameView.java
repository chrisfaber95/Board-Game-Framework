package Framework.View;

import DataStructure.Board;
import DataStructure.Piece;
import DataStructure.Tile;
import Framework.Controller.Controller;
import Framework.Controller.Gamecontroller;
import Framework.Model.GameModel;
import Framework.Model.PlayerModel;
import NetworkCore.NetworkController;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameView {

    private Label moveLabel, playerLabel, opponentLabel, pieceCount, winLabel;
    private GameModel gModel;
    private PlayerModel pModel;
    private NetworkController nModel;
    private Controller controller;
    private Gamecontroller gameControl;

    private boolean isAI;
    private int rowSize;
    private Board dataStructure;
    private BorderPane viewPane = new BorderPane();
    private GridPane grid;
    private ArrayList<Pane> paneList = new ArrayList<>();
    private String playerColor;
    private Label turnLabel;

    public GameView(MainView mainView) {
        this.controller = mainView.getController();
        this.gModel =  controller.GetgModel();
        this.pModel = controller.GetpModel();
        this.nModel = controller.GetnModel();

        this.gameControl = controller.getGController();
        this.isAI = gModel.isAI();

        dataStructure = controller.GetgModel().getBoard();
        dataStructure.resetGame();

        initBoardGrid();

        HBox bBox = new HBox();
        bBox.setPadding(new Insets(15, 12, 15, 12));
        bBox.setSpacing(10);
        bBox.setStyle("-fx-background-color: #336699;");

        moveLabel = new Label("Move: -");
        playerLabel = new Label("Player: " + controller.GetpModel().getPlayerName());
        pieceCount = new Label("");
        opponentLabel = new Label("");
        winLabel = new Label("");
        turnLabel = new Label("");

        bBox.getChildren().addAll(moveLabel, winLabel, playerLabel, pieceCount);

        // New stage
        Stage stage = new Stage();

        // Main pane
        //viewPane.setTop(hBox);
        viewPane.setBottom(bBox);
        viewPane.setCenter(grid);
    }



    public BorderPane getViewPane() {
        return viewPane;
    }

    public void initBoardGrid() {

        grid = new GridPane();
        grid.getStyleClass().add("game-grid");

        // Set all column constraints
        for(int i = 0; i < dataStructure.getDimension(); i++) {
            ColumnConstraints column = new ColumnConstraints(80);
            grid.getColumnConstraints().add(column);
        }

        // Set all row constraints
        for(int i = 0; i < dataStructure.getDimension(); i++) {
            RowConstraints row = new RowConstraints(80);
            grid.getRowConstraints().add(row);
        }

        // Make grid
        for (int y = 0; y < dataStructure.getDimension(); y++){
            for (int x = 0; x < dataStructure.getDimension(); x++){
                Pane pane = new Pane();
                if(!controller.GetgModel().isAI()) {
                    pane.setOnMouseReleased(e -> controller.getGController().tileClick(pane));
                }
                if (controller.GetgModel().getBoard().getTileByPosition(x + 1, y + 1).getPiece() == Piece.PIECE1)
                    pane.getChildren().add(getColoredPiece(Piece.PIECE1));

                if (controller.GetgModel().getBoard().getTileByPosition(x + 1, y + 1).getPiece() == Piece.PIECE2)
                    pane.getChildren().add(getColoredPiece(Piece.PIECE2));

                pane.getStyleClass().add("game-grid-cell");
                if (x == 0) {
                    pane.getStyleClass().add("first-column");
                }
                if (y == 0) {
                    pane.getStyleClass().add("first-row");
                }
                paneList.add(pane);
                grid.add(pane, x, y);
            }
        }

    }

    public void refreshBoard(){

    }


    public void buildBoard() {
        grid = new GridPane();
        grid.getStyleClass().add("game-grid");

        // Set all column constraints
        for(int i = 0; i < dataStructure.getDimension(); i++) {
            ColumnConstraints column = new ColumnConstraints(80);
            grid.getColumnConstraints().add(column);
        }

        // Set all row constraints
        for(int i = 0; i < dataStructure.getDimension(); i++) {
            RowConstraints row = new RowConstraints(80);
            grid.getRowConstraints().add(row);
        }

        // Make grid
        for (int y = 0; y < dataStructure.getDimension(); y++){
            for (int x = 0; x < dataStructure.getDimension(); x++){
                Pane pane = new Pane();
              //  pane.setOnMouseReleased(e -> controller.tileClick(paneList, pane));

                if (dataStructure.getTileByPosition(x + 1, y + 1).getPiece() == Piece.PIECE1)
                    pane.getChildren().add(getColoredPiece(Piece.PIECE1));

                if (dataStructure.getTileByPosition(x + 1, y + 1).getPiece() == Piece.PIECE2)
                    pane.getChildren().add(getColoredPiece(Piece.PIECE2));

                pane.getStyleClass().add("game-grid-cell");
                if (x == 0) {
                    pane.getStyleClass().add("first-column");
                }
                if (y == 0) {
                    pane.getStyleClass().add("first-row");
                }
                paneList.add(pane);
                grid.add(pane, x, y);
            }
        }

    }

    public void boardRefresh(int move, String name) {
        if (name.equals("MOVE")) {
            if (dataStructure.validMove(dataStructure.getTileByPosition((move % rowSize) + 1, (move / rowSize) + 1))) {
                dataStructure.placeMove(dataStructure.getTileByPosition((move % rowSize) + 1, (move / rowSize) + 1));
                buildBoard();
            }
            dataStructure.printBoard();
        }

        System.out.println(move);

    }

    public String getPlayerColor () {
        return playerColor;
    }

    public void setPlayerColor (String color) {
        playerColor = color;
    }

    public Node getColoredPiece(Piece piece) {

        Circle circle = new Circle(40, 40, 20);

        if (piece == Piece.PIECE2)
            circle.setFill(Color.RED);
        else if (piece == Piece.PIECE1)
            circle.setFill(Color.BLACK);
        else
            circle.setFill(Color.WHITE);

        Group group = new Group();
        group.getChildren().add(circle);

        return group;
    }

    public Board getDataStructure() {
        return dataStructure;
    }

    public void refresh(Board board) {

        int white = 0, black = 0;
        for (Tile tile :board.getTiles()) {
            if (tile.getPiece() == Piece.PIECE1)
                white++;
            if (tile.getPiece() == Piece.PIECE2)
                black++;
        }
        pieceCount.setText("White: " + white + " Black: " + black);
        moveLabel.setText("Move: " + board.getProgression());


        for (int i = 0; i < paneList.size(); i++) {

            if (board.getTiles()[i].getPiece() == Piece.PIECE1)
                paneList.get(i).getChildren().add(getColoredPiece(Piece.PIECE1));

            if (board.getTiles()[i].getPiece() == Piece.PIECE2)
                paneList.get(i).getChildren().add(getColoredPiece(Piece.PIECE2));

            if (board.getTiles()[i].getPiece() == Piece.NULL)
                paneList.get(i).getChildren().clear();
        }
    }


    public ArrayList<Pane> getPaneList(){
        return paneList;
    }

    public void setPlayerLabel (String name) {
        playerLabel.setText("Player: " + name);
    }

    public void setOpponentLabel (String name) {
        if (!name.equals(""))
            opponentLabel.setText("Opponent: " + name);
        else
            opponentLabel.setText("");
    }

    public void setWinLabel (String stat) {
        winLabel.setText("Status: " + stat);
    }

    public void setTile (int index, Piece piece) {
        paneList.get(index).getChildren().add(getColoredPiece(piece));
    }

    public int getTileIndex (Pane pane) {
        for (int i = 0; i < paneList.size(); i++) {
            if (paneList.get(i) == pane) {
                return i;
            }
        }
        return 0;
    }

    public void setTurnLabel (String name) {
        turnLabel.setText("Turn for: " + name);
    }
}
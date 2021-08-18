package DatGui;

import DataStructure.Board;
import DataStructure.Piece;
import DataStructure.Tile;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Gui {
    private GridPane grid;
    private ArrayList<Pane> paneList = new ArrayList<>();

    Label moveLabel, winLabel, playerLabel, pieceCount;

    Gui(Board board, GameController controller) {

        // Grid
        grid = new GridPane();
        grid.getStyleClass().add("game-grid");

        // Set all column constraints
        for(int i = 0; i < board.getDimension(); i++) {
            ColumnConstraints column = new ColumnConstraints(80);
            grid.getColumnConstraints().add(column);
        }

        // Set all row constraints
        for(int i = 0; i < board.getDimension(); i++) {
            RowConstraints row = new RowConstraints(80);
            grid.getRowConstraints().add(row);
        }

        // Make grid
        for (int y = 0; y < board.getDimension(); y++){
            for (int x = 0; x < board.getDimension(); x++){
                Pane pane = new Pane();
                pane.setOnMouseReleased(e -> controller.tileClick(paneList, pane));

                if (board.getTileByPosition(x + 1, y + 1).getPiece() == Piece.PIECE1)
                    pane.getChildren().add(getColoredPiece(Piece.PIECE1));

                if (board.getTileByPosition(x + 1, y + 1).getPiece() == Piece.PIECE2)
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

        // Top box
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: #336699;");

        Button algoButton = new Button("Make Move");
        algoButton.setOnAction(e -> controller.algoButtonClick());
        algoButton.setPrefSize(120, 20);

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> controller.resetClick());
        resetButton.setPrefSize(80, 20);

        Button revertMove = new Button("Revert Move");
        revertMove.setOnAction(e -> controller.revertMove());
        revertMove.setPrefSize(120, 20);

        Button switchPlayer = new Button("Switch Player");
        switchPlayer.setOnAction(e -> controller.switchPlayer());
        switchPlayer.setPrefSize(120, 20);

        // Add to box
        hBox.getChildren().addAll(algoButton, resetButton, revertMove, switchPlayer);

        // Bottom box
        HBox bBox = new HBox();
        bBox.setPadding(new Insets(15, 12, 15, 12));
        bBox.setSpacing(10);
        bBox.setStyle("-fx-background-color: #336699;");

        moveLabel = new Label("Move: 0");
        winLabel = new Label("Won: -");
        playerLabel = new Label("Player: -");
        pieceCount = new Label("Red: - Black: -");

        bBox.getChildren().addAll(moveLabel, winLabel, playerLabel, pieceCount);

        // New stage
        Stage stage = new Stage();

        // Main pane
        BorderPane border = new BorderPane();
        border.setTop(hBox);
        border.setBottom(bBox);
        border.setCenter(grid);

        // Add grid to Scene
        Scene scene = new Scene(border, 660, 760);
        scene.getStylesheets().add("grid.css");

        // Show window
        stage.setTitle("Game");
        stage.setScene(scene);
        stage.show();
    }


    void refreshPanes(Board board) {

        moveLabel.setText("Move: " + board.getProgression());

        if (board.getCurrentPlayer() == Piece.PIECE1)
            playerLabel.setText("Player: Black (" + board.getCurrentPlayer() + ")");
        else
            playerLabel.setText("Player: Red (" + board.getCurrentPlayer() + ")");

        winLabel.setText("Status: " + board.checkCurrentGameStatus());


        int black = 0, red = 0;
        for (Tile tile:board.getTiles()) {
            if (tile.getPiece() == Piece.PIECE1)
                black++;
            if (tile.getPiece() == Piece.PIECE2)
                red++;
        }
        pieceCount.setText("Red: " + red + " Black: " + black);

        for (int i = 0; i < paneList.size(); i++) {

            if (board.getTiles()[i].getPiece() == Piece.PIECE1)
                paneList.get(i).getChildren().add(getColoredPiece(Piece.PIECE1));

            if (board.getTiles()[i].getPiece() == Piece.PIECE2)
                paneList.get(i).getChildren().add(getColoredPiece(Piece.PIECE2));

            if (board.getTiles()[i].getPiece() == Piece.NULL)
                paneList.get(i).getChildren().clear();
        }
    }


    Node getColoredPiece(Piece piece) {

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
}

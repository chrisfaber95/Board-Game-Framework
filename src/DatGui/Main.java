package DatGui;

import DataStructure.Piece;
import DataStructure.Reversi;
import DataStructure.TicTacToe;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Add the tile label
        Text sceneTitle = new Text("Vul uw gegevens in");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        // Add name the name box
        Label nameLabel = new Label("Naam:");
        TextField nameBox = new TextField();
        grid.add(nameLabel, 0, 1);
        grid.add(nameBox, 1, 1, 2, 1);

        // Add IP and port boxes
        Label addressLabel = new Label("IP en poort:");
        TextField ipBox = new TextField();
        Spinner<Integer> portSpinner = new Spinner<Integer>(1, 65000, 500, 1);
        grid.add(addressLabel, 0, 2);
        grid.add(ipBox, 1, 2);
        grid.add(portSpinner, 2, 2);

        // Add Game type selector
        Label gameTypeLabel = new Label("Spel type:");
        ObservableList<String> options = FXCollections.observableArrayList("Reversi", "Boter, Kaas en Eieren");
        final ComboBox comboBox = new ComboBox(options);
        comboBox.getSelectionModel().select(0);
        grid.add(gameTypeLabel, 0, 3);
        grid.add(comboBox, 1, 3, 1, 1);

        // Add submit button
        Button login = new Button("Inloggen");
        Button offline = new Button("Offline");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(login);
        hbBtn.getChildren().add(offline);
        grid.add(hbBtn, 2, 5);

        // Add message label
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        // Button handler for login in on a server
        login.setOnAction(e -> {
            if (comboBox.getSelectionModel().getSelectedItem() == null || ipBox.getText().equals("") || nameBox.getText().equals("")) {
                actiontarget.setText("Vul alles in!");
                actiontarget.setFill(Color.FIREBRICK);
                return;
            }
            if (!validate(ipBox.getText())) {
                actiontarget.setText("Het IP adres is niet geldig!");
                actiontarget.setFill(Color.FIREBRICK);
                return;
            }

            if (comboBox.getValue().equals("Boter, Kaas en Eieren"))
                new GameController(new TicTacToe(), nameBox.getText(), ipBox.getText(), portSpinner.getValue());
            if (comboBox.getValue().equals("Reversi"))
                new GameController(new Reversi(Piece.PIECE1), nameBox.getText(), ipBox.getText(), portSpinner.getValue());
        });

        // Button handler for an offline game
        offline.setOnAction(e -> {
            if (comboBox.getSelectionModel().getSelectedItem() == null ){
                actiontarget.setText("Selecteer een spel modus!");
                actiontarget.setFill(Color.FIREBRICK);
                return;
            }

            if (comboBox.getValue().equals("Boter, Kaas en Eieren"))
                new GameController(new TicTacToe());
            if (comboBox.getValue().equals("Reversi"))
                new GameController(new Reversi(Piece.PIECE1));
        });

        // Add grid to Scene
        Scene scene = new Scene(grid, 480, 275);

        // Show window
        primaryStage.setTitle("Game Thingy");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    private static boolean validate(final String ip) {
        return PATTERN.matcher(ip).matches();
    }
}

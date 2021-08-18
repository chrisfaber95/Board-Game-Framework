package Framework.View;

import Framework.Controller.Controller;
import Framework.Controller.NetworkListener;
import NetworkCore.NetworkController;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class LobbyView {
    //private Button SubscribeGame;
    private RadioButton gButton;
    private RadioButton pButton;
    private VBox playerPane;
    private BorderPane viewPane = new BorderPane();
    private Controller controller;

    public LobbyView(Controller controller){
        this.controller = controller;

        VBox playerPane = new VBox();
        VBox gamePane = new VBox();
        viewPane.setTop(null);
        viewPane.setRight(gamePane);

        // Playerlist left side
        setupPlayerBox();

        // Gamelist left side
        ArrayList<String> gameList = controller.getNetworkController().getGames();
        for(String game : gameList) {
            gButton = new RadioButton();
            gButton.setText(game);
            gamePane.getChildren().add(gButton);
        }

        HBox subscribe = new HBox();
        viewPane.setBottom(subscribe);
        for(String games : gameList){
            Button subscribeGame = new Button();
            subscribeGame.setText("Start " + games + " as Player");
            subscribeGame.setOnAction(e -> controller.subscribeToGame(games, false));
            subscribe.getChildren().add(subscribeGame);
            subscribeGame = new Button();
            subscribeGame.setText("Start " + games + " as AI");
            subscribeGame.setOnAction(e -> controller.subscribeToGame(games, true));
            subscribe.getChildren().add(subscribeGame);
        }
    }

    public void setupPlayerBox(){
        viewPane.setLeft(null);

        playerPane = new VBox();
        Text title = new Text();
        title.setText("Playerlist:");
        title.setTextAlignment(TextAlignment.CENTER);
        title.setUnderline(true);
        playerPane.getChildren().add(title);

        ArrayList<String> playerList = controller.getNetworkController().getPlayers();
        for(String player : playerList) {

            pButton = new RadioButton();
            pButton.setText(player);
            playerPane.getChildren().add(pButton);
        }
        BorderPane.setAlignment(playerPane, Pos.CENTER);
        viewPane.setLeft(playerPane);

        Button refreshPlayers = new Button();
        refreshPlayers.setText("Refresh Playerlist");
        refreshPlayers.setOnAction(e -> setupPlayerBox());
        playerPane.getChildren().add(refreshPlayers);
    }

    public BorderPane getViewPane() {
        return viewPane;
    }
}

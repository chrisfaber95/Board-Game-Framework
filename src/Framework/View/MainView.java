package Framework.View;

import Framework.Controller.Controller;
import Framework.Model.GameModel;
import Framework.Model.PlayerModel;
import NetworkCore.NetworkController;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class MainView{

    private static BorderPane root = new BorderPane();
    private Controller controller;
    private Scene scene;
    private GameView game;
    private LoginView login;
    private LobbyView lobby;

    public MainView() {
        setupMainView();

    }


    private void setupMainView(){
        scene = new Scene(root, 800, 800);
        scene.getStylesheets().add(getClass().getResource("grid.css").toExternalForm());
        System.out.println("Scene build");
    }



    private void setupLoginLayout() {
        login = new LoginView(controller);
        clearRoot();
        root.setCenter(login.getViewPane());

    }

    private void setupGameMenu() {

        lobby = new LobbyView(controller);
        clearRoot();
        root.setCenter(lobby.getViewPane());
    }

    private void setupGame() {
        String gameName = controller.GetgModel().getGameName();
        game = new GameView(this);
        clearRoot();
        root.setCenter(game.getViewPane());


        root.setCenter(game.getViewPane());
        Button forfeit = new Button();
        forfeit.setText("forfeit");

        forfeit.setOnAction(e -> controller.forfeitMatch());
        root.setBottom(forfeit);
//        scene = new Scene(root, 800, 800);
    }


    public Scene getScene() {
        return scene;
    }

    public void resetView() {

        switch (controller.GetpModel().getCurrentPage()){
            case "Login":
                clearRoot();
                setupLoginLayout();
                break;

            case  "GameMenu":
                clearRoot();
                setupGameMenu();
                break;

            case "Loading":
                clearRoot();
                loadingScreen();
                break;

            case "Game":
                clearRoot();
                BuildGameBoard();
                break;

            default:
                clearRoot();
                setupLoginLayout();
                break;

        }
    }

    public void refreshGameBoard(int move, String name){
        clearRoot();
        game.boardRefresh(move, name);
        root.setCenter(game.getViewPane());
    }

    public void BuildGameBoard(){
        setupGame();
    }

    public void loadingScreen(){
        Text loading = new Text();
        loading.setText("Waiting for opponent");
        root.setCenter(loading);
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    public GameView getGameBoard(){
        return game;
    }

    public LobbyView getLobby() {
        return lobby;
    }

    public LoginView getLogin() {
        return login;
    }

    private void clearRoot(){
        root.setTop(null);
        root.setCenter(null);
        root.setBottom(null);
        root.setLeft(null);
        root.setRight(null);
    }



    public Controller getController() {
        return controller;
    }
}



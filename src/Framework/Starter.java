package Framework;

import Framework.Controller.Controller;
import Framework.Controller.NetworkListener;
import Framework.Model.GameModel;
import Framework.Model.PlayerModel;
import Framework.View.MainView;
import NetworkCore.NetworkController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Starter extends Application{

    //private Model control = new Model();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        GameModel gameModel = new GameModel();
        PlayerModel playerModel = new PlayerModel();
        MainView mainview = new MainView();
        NetworkController networkController = new NetworkController();

        Controller controller = new Controller(gameModel, playerModel, mainview, networkController);



        NetworkListener listener = new NetworkListener();

        networkController.addNetEventListener(listener);
        listener.setController(controller);
        try {
            primaryStage.setScene(mainview.getScene());
            primaryStage.show();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}


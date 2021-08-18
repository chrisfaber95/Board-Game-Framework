package Framework.Controller;

import Framework.Model.GameModel;
import Framework.View.MainView;
import Framework.Model.PlayerModel;
import NetworkCore.DemoListener;
import NetworkCore.NetMessage;
import NetworkCore.NetworkController;

import java.util.ArrayList;
import java.util.HashMap;

public class Controller {
    private final NetworkController networkController;
    private NetworkListener networkModel;
    private GameModel gameModel;
    private PlayerModel playerModel;
    private MainView view;
    private Gamecontroller gControl;

    public Controller(GameModel gameModel, PlayerModel playerModel, MainView mainView, NetworkController networkController) {
        this.gameModel = gameModel;
        this.playerModel = playerModel;
        this.view = mainView;
        this.networkController = networkController;
        view.setController(this);
        view.resetView();
    }

    public NetworkController getNetworkController() {
        return networkController;
    }

    public void setupNetworkConnection(String ip, int port, String username){
        networkController.setHost(ip);
        networkController.setPort(port);

        networkController.start();
        networkController.login(username);
        networkController.isConnected();

        setPlayerName(username);
    }

    public void setPlayerName(String playerName) {
        playerModel.setPlayerName(playerName);
    }

    public void setCurrentPage(String currentPage) {
        playerModel.setCurrentPage(currentPage);
    }

    public String getPlayerName() {
        return playerModel.getPlayerName();
    }

    public String getCurrentPage() {
        return playerModel.getCurrentPage();
    }




    public ArrayList<String> getPlayerList() {
        return networkController.getPlayers();
    }

    public ArrayList<String> getGameList() {
        return networkController.getGames();
    }

    public void backToLobby() {
        gameModel.setWaitForGame(true);
        playerModel.setCurrentPage("Gamemenu");
        view.resetView();
    }


    /*
    Button init
     */
    public void loginButtonClick(String IP, String Port, String username){
        if(IP.isEmpty() || Port.isEmpty() || username.isEmpty()){
            view.getLogin().getAlert().setText("Everything need to be filled in!");
        }
        else{
            setupNetworkConnection(IP, Integer.parseInt(Port), username);
            setCurrentPage("GameMenu");
            view.resetView();
        }

    }

    public void subscribeToGame(String gameName, boolean b){
        playerModel.setCurrentPage("Loading");
        gameModel.setGameName(gameName);
        gameModel.setAI(b);
        getNetworkController().subscribe(gameName);
        gControl = new Gamecontroller(this);
        view.resetView();
    }

    public void forfeitMatch(){
        networkController.forfeit();
        playerModel.setCurrentPage("GameMenu");
        gameModel = new GameModel();
        gControl = null;
    }

/*
Get Models/Controllers/Views
 */
    public GameModel GetgModel() {
        return gameModel;
    }

    public PlayerModel GetpModel() {
        return playerModel;
    }

    public NetworkController GetnModel() {
        return networkController;
    }

    public Gamecontroller getGController(){
        return gControl;
    }

    public MainView getView() {
        return view;
    }

    public void challengedBy(NetMessage msg) {
    }

    public void challengeCancelled(NetMessage msg) {
    }

    public void youWin(NetMessage msg) {
        GetpModel().setCurrentPage("Game");
        gameModel = new GameModel();
        view.resetView();
    }

    public void youLose(NetMessage msg) {
        GetpModel().setCurrentPage("Game");
        gameModel = new GameModel();
        view.resetView();
    }

    public void youTied(NetMessage msg) {
        GetpModel().setCurrentPage("Game");
        gameModel = new GameModel();
        view.resetView();
    }

    public void gotError(NetMessage msg) {
    }
}

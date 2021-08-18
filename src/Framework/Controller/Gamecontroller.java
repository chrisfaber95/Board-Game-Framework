package Framework.Controller;

import DataStructure.Piece;
import DataStructure.Reversi;
import DataStructure.TicTacToe;
import DataStructure.Tile;
import NetworkCore.NetMessage;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

import java.util.ArrayList;


public class Gamecontroller {

    private Controller controller;

    public Gamecontroller(Controller controller){
        this.controller = controller;

    }

    public void updateBoard(NetMessage payload){
      //  controller.getView().refreshGameBoard();
    }

    public void setFirstPlayer( NetMessage msg) {
    }


    public void setYourTurn() {
        //controller.GetgModel().setYourTurn(true);
    }

    public void swapPlayer(){
        controller.getView().getGameBoard().getDataStructure().swapPlayer();
    }

    public void placeMoveAI(NetMessage payload){
    //    controller..getGameBoard().getDataStructure().placeMove();
    }


    public void startMatch(NetMessage msg) {
        controller.GetgModel().setOpponent(msg.getPayload().get("OPPONENT"));
        controller.GetgModel().setFirstPlayer(msg.getPayload().get("PLAYERTOMOVE"));
        switch(controller.GetgModel().getGameName()){
            case  "Reversi":
                    controller.GetgModel().setBoard(new Reversi(Piece.PIECE2));
                break;
            case  "Tic-Tac-Toe":
                controller.GetgModel().setBoard(new TicTacToe());
                break;
        }
        controller.GetgModel().setWaitForGame(false);
        controller.setCurrentPage("Game");
        controller.getView().resetView();
/*
        if (msg.getPayload().get("PLAYERTOMOVE").equals(controller.getPlayerName())) {
            Platform.runLater(() -> controller.getView().getGameBoard().setPlayerLabel(controller.getPlayerName() + " (Black)"));
            Platform.runLater(() -> controller.getView().getGameBoard().setOpponentLabel(msg.getPayload().get("OPPONENT") + " (White)"));
            controller.getView().getGameBoard().setPlayerColor("Black");
        } else {
            Platform.runLater(() -> controller.getView().getGameBoard().setPlayerLabel(controller.getPlayerName() + " (White)"));
            Platform.runLater(() -> controller.getView().getGameBoard().setOpponentLabel(msg.getPayload().get("OPPONENT") + " (Black)"));
            controller.getView().getGameBoard().setPlayerColor("White");
        }
        Platform.runLater(() -> controller.getView().getGameBoard().setTurnLabel(msg.getPayload().get("PLAYERTOMOVE") + " (Black)"));
*/
    }

    public void placeMove(NetMessage msg) {
        if (!msg.getPayload().get("PLAYER").equals(controller.getPlayerName())){
            //controller.GetgModel().getBoard().placeMove(controller.GetgModel().getBoard().getTiles()[Integer.parseInt(msg.getPayload().get("MOVE"))]);

            int index = Integer.parseInt(msg.getPayload().get("MOVE"));
                if (index >= 0 && index < controller.GetgModel().getBoard().getTiles().length) {
                    controller.GetgModel().getBoard().placeMove(controller.GetgModel().getBoard().getTiles()[index]);
                }

            Platform.runLater(() -> controller.getView().getGameBoard().refresh(controller.GetgModel().getBoard()));
        }
    }

    public void placeMoveAI(){

        controller.GetgModel().setMyTurn(true);
        if (controller.GetgModel().isAI()) {
            Tile tile = controller.GetgModel().getBoard().algorithmMove(3);
            controller.getNetworkController().doMove(controller.GetgModel().getBoard().getTileIndex(tile));
            Platform.runLater(() -> controller.getView().getGameBoard().refresh(controller.GetgModel().getBoard()));
            controller.GetgModel().setMyTurn(false);

      //      if ((controller.GetgModel().getBoard().getCurrentPlayer() == Piece.PIECE1 && controller.getView().getGameBoard().getPlayerColor().equals("Black")) ||
       //             (controller.GetgModel().getBoard().getCurrentPlayer() == Piece.PIECE2 && controller.getView().getGameBoard().getPlayerColor().equals("White")))
       //         controller.GetgModel().getBoard().swapPlayer();

       //     controller.getNetworkController().doMove(controller.GetgModel().getBoard().getTileIndex(tile));

        }


    }

    public void tileClick(Pane pane) {
        if(!controller.GetgModel().isAI() && controller.GetgModel().getBoard().placeMove(controller.GetgModel().getBoard().getTiles()[controller.getView().getGameBoard().getTileIndex(pane)]) > 0){
            controller.getView().getGameBoard().refresh(controller.GetgModel().getBoard());

            if(controller.getNetworkController() != null &&controller.getNetworkController().isConnected()){
                controller.getNetworkController().doMove(controller.getView().getGameBoard().getTileIndex(pane));
            }
        }
    }

    /*
    Game Model Setters
     */

    public void setGameName(String gameName) {
        controller.GetgModel().setGameName(gameName);
    }

    public void setPoints(int[] points){
        controller.GetgModel().setPoints(points);
    }

    /*
    Game Model Getters
     */


    public int[] getPoints() {
        return controller.GetgModel().getPoints();
    }

    public String getGameName() {
        return controller.GetgModel().getGameName();
    }



}

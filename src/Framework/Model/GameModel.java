package Framework.Model;

import DataStructure.Board;
import Framework.Controller.NetworkListener;

public class GameModel {
    private int[] points;
    private boolean isAI;
    private Board board;
    private String gameName;
    private boolean waitForGame;
    private String opponent;
    private String firstPlayer;
    private boolean myTurn;

    public GameModel() {
        waitForGame = true;
        myTurn = false;
    }

    public boolean getWaitForGame(){
        return waitForGame;
    }

    public void setWaitForGame(boolean waiting){
        this.waitForGame = waiting;
    }


    public int[] getPoints(){
        return points;
    }
    public void setPoints(int[] points) {
        this.points = points;
    }


    public String getGameName() {
        return gameName;
    }
    public void setGameName(String gamename) {
        this.gameName = gamename;
    }


    public boolean isAI() {
        return isAI;
    }

    public void setAI(boolean AI) {
        isAI = AI;
    }

    public String getOpponent() {
        return opponent;
    }
    public void setOpponent(String name){
        opponent = name;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setFirstPlayer(String firstPlayer) {
        
        this.firstPlayer = firstPlayer;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }
    public boolean getMyTurn() {
        return myTurn;
    }
}
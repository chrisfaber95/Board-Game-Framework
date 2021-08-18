package Framework.Controller;
import Framework.Controller.Controller;
import Framework.Model.PlayerModel;
import NetworkCore.NetEvent;
import NetworkCore.NetEventListener;
import NetworkCore.NetMessage;
import javafx.application.Platform;
//import NetworkMan.msgType;

import java.util.ArrayList;
import java.util.HashMap;

public class NetworkListener implements NetEventListener {
    private Controller controller;

    @Override
    public void onDataReceived(NetEvent event) {
        NetMessage msg = event.netMessage();
        switch(msg.getType()) {
            case MATCH:
                Platform.runLater(() -> controller.getGController().startMatch(msg));
                break;
            case YOURTURN:
                Platform.runLater(() -> controller.getGController().placeMoveAI());
                break;
            case MOVE:
                Platform.runLater(() -> controller.getGController().placeMove(msg));
                break;
            case CHALLENGE:
                Platform.runLater(() -> controller.challengedBy(msg));
                break;
            case CHALLENGE_CANCELLED:
                Platform.runLater(() -> controller.challengeCancelled(msg));
                break;
            case WIN:
                Platform.runLater(() -> controller.youWin(msg));
                break;
            case LOSS:
                Platform.runLater(() -> controller.youLose(msg));
                break;
            case DRAW:
                Platform.runLater(() -> controller.youTied(msg));
                break;
            case ERR:
                Platform.runLater(() -> controller.gotError(msg));
                break;
            case NULL:
                break;
            default:
                break;
        }
    }

    public void setController(Controller controller){
        this.controller = controller;
    }
}

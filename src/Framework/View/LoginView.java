package Framework.View;

import Framework.Controller.Controller;
import com.sun.javafx.tools.packager.Main;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.swing.text.View;

public class LoginView {
    private Text alert;
    private TextField PortField;
    private TextField IPField;
    private TextField username;
    private Button login;
    private BorderPane viewPane = new BorderPane();

    public LoginView(Controller controller){

        VBox vBox = new VBox();
        username = new TextField();
        username.setPromptText("Username");

        IPField = new TextField();
        IPField.setPromptText("IP");

        PortField = new TextField();
        PortField.setPromptText("Port");

        login = new Button();
        login.setOnAction(e -> controller.loginButtonClick(IPField.getText(), PortField.getText(), username.getText()));
        login.setText("Connect");

        alert = new Text();

        vBox.getChildren().addAll(username, IPField, PortField, login, alert);


        HBox bBox = new HBox();
        Image reversi = new Image(getClass().getResourceAsStream("reversi.jpg"));
        ImageView reversiImage = new ImageView();
        reversiImage.setFitWidth(200);
        reversiImage.setFitHeight(200);
        reversiImage.setImage(reversi);

        Image ttt = new Image(getClass().getResourceAsStream("tictactoe.jpg"));
        ImageView tttImage = new ImageView();
        tttImage.setFitWidth(200);
        tttImage.setFitHeight(200);
        tttImage.setImage(ttt);

        bBox.getChildren().addAll(reversiImage, tttImage);

        viewPane.setBottom(bBox);
        viewPane.setCenter(vBox);

        viewPane.getStyleClass().add("loginPage");
    }

    public Text getAlert() {
        return alert;
    }

    public BorderPane getViewPane() {
        return viewPane;
    }
}

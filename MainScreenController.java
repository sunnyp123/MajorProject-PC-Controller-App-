package application;
import file.AvatarFile;
import application.ipaddress.GetFreePort;
import application.ipaddress.GetMyIpAddress;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
public class MainScreenController implements Initializable {
    public static MainScreenController mainScreenController;
    public static ServerSocket serverSocket = null;
    public static Socket clientSocket = null;
    public static InputStream inputStream = null;
    public static OutputStream outputStream = null;
    public static ObjectOutputStream objectOutputStream = null;
    public static ObjectInputStream objectInputStream = null;
    @FXML
    private TilePane tilePane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label ipAddressLabel;
    @FXML
    private Label portNumberLabel;
    @FXML
    private Label connectionStatusLabel;
    @FXML
    private Button resetButton;
    @FXML
    private Label messageLabel;
    @FXML
    private Button backButton;
    @FXML
    private Button rootButton;
    @FXML
    Label pathLabel;
	 public MainScreenController getMainScreenController() {
	        return MainScreenController.mainScreenController;
	    }
	    
	    public void setMainScreenController(MainScreenController mainScreenController) {
	        MainScreenController.mainScreenController = mainScreenController;
	    }

		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			setConnectionDetails();
		}
private void setConnectionDetails() {
	String ipAddresses[] = new GetMyIpAddress().ipAddress();
	String connectionStatus = "Not Connected";
	int port = new GetFreePort().getFreePort();
	String ipAddress = ipAddresses[0];
    if (ipAddresses[1] != null) {
        ipAddress = ipAddress + " | " + ipAddresses[1];
    }
    ipAddressLabel.setText(ipAddress);
    portNumberLabel.setText(Integer.toString(port));
    connectionStatusLabel.setText(connectionStatus);
    if (ipAddresses[0].equals("127.0.0.1")) {
        showMessage("Connect your PC to Android phone hotspot or" +
                " connect both devices to a local network.");
    } else {
        try {
            serverSocket = new ServerSocket(port);
            startServer(port);
        } catch(Exception e) {
            showMessage("Error in initializing server");
            e.printStackTrace();
        }
    }
}
public void showMessage(String message) {
    Platform.runLater(() -> {
        messageLabel.setText(message);
    });
}
private void startServer(int port) throws Exception {
    new Service<Void>() {

        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    new Server().connect(resetButton, connectionStatusLabel,
                            messageLabel, port);
                    return null;
                }
                
            };
        }
        
    }.start();
}
}

package application;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.InetAddress;
import javafx.application.Platform;
import application.MouseKeyboardControl;

public class Server {
private Label messageLabel;
public void connect(Button resetButton,Label connectionStatusLabel,Label messageLabel,int port) {
	this.messageLabel=messageLabel;
	MouseKeyboardControl mouseControl = new MouseKeyboardControl();
	 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
     int screenWidth = (int) screenSize.getWidth();
     int screenHeight = (int) screenSize.getHeight();
     try {
    	 MainScreenController.clientSocket=MainScreenController.serverSocket.accept();
    	 Platform.runLater(() -> {
             resetButton.setDisable(true);
         });
    	 InetAddress remoteInetAddress = 
                 MainScreenController.clientSocket.getInetAddress();
         String connectedMessage = "Connected to: " + remoteInetAddress;
         Platform.runLater(() -> {
             connectionStatusLabel.setText(connectedMessage);
         });
         showMessage(connectedMessage);
         //connecting socket to application peer to peer.
         new ClientToAndroid().connect(remoteInetAddress, port);
         MainScreenController.inputStream = 
                 MainScreenController.clientSocket.getInputStream();
         MainScreenController.outputStream = 
                 MainScreenController.clientSocket.getOutputStream();
         MainScreenController.objectOutputStream = 
                 new ObjectOutputStream(MainScreenController.outputStream);
         MainScreenController.objectInputStream = 
                 new ObjectInputStream(MainScreenController.inputStream);
         String message;
         while(true) {
        	 try {
        		 message = (String)MainScreenController.objectInputStream.readObject();
        		 int keyCode;
        		 if(message!=null) {
        			 switch(message) {
        			 case "LEFT_CLICK":
                         mouseControl.leftClick();
                         break;
                     case "RIGHT_CLICK":
                         mouseControl.rightClick();
                         break;
                     case "DOUBLE_CLICK":
                         mouseControl.doubleClick();
                         break;
                     case "MOUSE_WHEEL":
                         int scrollAmount = 
                                 (int) MainScreenController.objectInputStream.readObject();
                         mouseControl.mouseWheel(scrollAmount);
                         break;
                     case "MOUSE_MOVE":
                         int x = (int) MainScreenController.objectInputStream.readObject();
                         int y = (int) MainScreenController.objectInputStream.readObject();
                         Point point = MouseInfo.getPointerInfo().getLocation(); 
                         // Get current mouse position
                         float nowx = point.x;
                         float nowy = point.y;
                         mouseControl.mouseMove((int) (nowx + x), (int) (nowy + y));
                         break;
                     case "MOUSE_MOVE_LIVE":
                         // need to adjust coordinates 
                         float xCord = (float) MainScreenController.objectInputStream.readObject();
                         float yCord = (float) MainScreenController.objectInputStream.readObject();
                         xCord = xCord * screenWidth;
                         yCord = yCord * screenHeight;
                         mouseControl.mouseMove((int) xCord, (int) yCord);
                         break;
                     case "KEY_PRESS":
                         keyCode = (int) MainScreenController.objectInputStream.readObject();
                         mouseControl.keyPress(keyCode);
                         break;
                     case "KEY_RELEASE":
                         keyCode = (int) MainScreenController.objectInputStream.readObject();
                         mouseControl.keyRelease(keyCode);
                         break;
                     case "CTRL_ALT_T":
                         mouseControl.ctrlAltT();
                         break;
                     case "CTRL_SHIFT_Z":
                         mouseControl.ctrlShiftZ();
                         break;
                     case "ALT_F4":
                         mouseControl.altF4();
                         break;
                     case "TYPE_CHARACTER": 
                         //handle StringIndexOutOfBoundsException here when pressing soft enter key
                         char ch = ((String) MainScreenController.objectInputStream.readObject()).charAt(0);
                         mouseControl.typeCharacter(ch);
                         break;
                     case "TYPE_KEY": 
                         keyCode = (int) MainScreenController.objectInputStream.readObject();
                         mouseControl.typeCharacter(keyCode);
                         break;
                     case "LEFT_ARROW_KEY":
                         mouseControl.pressLeftArrowKey();
                         break;
                     case "DOWN_ARROW_KEY":
                         mouseControl.pressDownArrowKey();
                         break;
                     case "RIGHT_ARROW_KEY":
                         mouseControl.pressRightArrowKey();
                         break;
                     case "UP_ARROW_KEY":
                         mouseControl.pressUpArrowKey();
                         break;
                     case "F5_KEY":
                         mouseControl.pressF5Key();
                         break;
        			 }
        		 }
        		 else {
        			 Platform.runLater(() -> {
        				 resetButton.setDisable(false);
        				 connectionStatusLabel.setText("Disconnected");
        			 });
        			 connectionClosed();
        			 break;
        		 }
        	 }catch(Exception e) {
        		 e.getStackTrace();
        		 connectionClosed();
                 ClientToAndroid.closeConnectionToAndroid();
                 Platform.runLater(() -> {
                     resetButton.setDisable(false);
                     connectionStatusLabel.setText("Disconnected");
                 });
                 break;
        	 }
         }
     }catch(Exception e) {
    	 e.printStackTrace();
     }
}
private void connectionClosed() {
    try {
        MainScreenController.objectInputStream.close();
        MainScreenController.clientSocket.close();
        MainScreenController.serverSocket.close();
        MainScreenController.inputStream.close();
        MainScreenController.outputStream.close();
        MainScreenController.objectOutputStream.close();
    } 
    catch(Exception e) {
        e.printStackTrace();
    }
}
private void showMessage(String message) {
    Platform.runLater(() -> {
        messageLabel.setText(message);
    });
}
}

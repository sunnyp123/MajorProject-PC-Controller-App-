package application;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
		     FXMLLoader fxmlLoader = new FXMLLoader(
		                getClass().getResource("MainScreens.fxml")
		        );
		     Parent root = (Parent)fxmlLoader.load();
		     MainScreenController mainScreenController = 
		                (MainScreenController) fxmlLoader.getController();
		        mainScreenController.setMainScreenController(mainScreenController);
		       
		     Scene scene = new Scene(root);
		        
		        stage.setScene(scene);
		        stage.setTitle("RemoteControlPC");
		        stage.setOnCloseRequest(e -> {
		            Platform.exit();
		            System.exit(0);
		        });
		        stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	
}

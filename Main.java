import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("scientific.fxml"));
        Scene scene = new Scene(root, 420, 585);
        scene.getStylesheets().add("lightmode.css");

        primaryStage.setTitle("QuickMaths V1.3 SCIENTIFIC");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

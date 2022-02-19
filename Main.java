import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println('-' == '~');
        Parent root = FXMLLoader.load(getClass().getResource("simple.fxml"));
        Scene scene = new Scene(root, 460, 575);
        scene.getStylesheets().add("lightmode.css");

        primaryStage.setTitle("QuickMaths V1.5 STANDARD");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

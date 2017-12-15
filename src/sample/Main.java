package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        initScene(primaryStage);
        primaryStage.show();
    }

    private void initScene(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("PandaLavanda super-puper laba3 app");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        primaryStage.setScene(
                new Scene(root, screenSize.width - 100, screenSize.height - 100)
        );
    }


    public static void main(String[] args) {
        launch(args);
    }
}

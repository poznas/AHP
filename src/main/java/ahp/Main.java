package ahp;

import ahp.controllers.RootLayoutController;
import ahp.models.AHP;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    public static Stage primaryStage;
    public static Scene rootScene;
    public static BorderPane rootLayout;

    public static RootLayoutController rootLayoutController;

    public static File currentFile;
    public static ahp.models.AHP AHP;

    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        Main.primaryStage.setTitle("AHP");

        Main.primaryStage.getIcons().add( new Image(
                String.valueOf(getClass().getResource("/images/ahp_icon.jpg"))
        ));
        AHP = new AHP();
        RootLayoutController.init();
    }
}

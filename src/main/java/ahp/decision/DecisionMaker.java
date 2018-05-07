package ahp.decision;

import ahp.Main;
import ahp.controllers.DialogAskDecisionMaker;
import ahp.controllers.RootLayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DecisionMaker {
    public static double ask(String alternativeI, String alternativeJ, String criterionId) {
        DialogAskDecisionMaker controller = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/dialog_ask_decision_maker.fxml"));
            AnchorPane dialogPane = loader.load();

            Stage dialogStage = RootLayoutController.getDialogStage("Choose preference", dialogPane);

            controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setAlternatives(alternativeI, alternativeJ, criterionId);

            dialogStage.showAndWait();

            if( controller.preference == 1 ){
                return controller.value;
            }else if( controller.preference == 2){
                return 1.0 / controller.value;
            } else{
                throw new Exception("ojojojojoj");
            }
        } catch (IOException | Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public static class Exception extends java.lang.Exception{
        Exception(String message) {
            super(message);
        }
    }
}

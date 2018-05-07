package ahp.controllers;

import ahp.decision.Scale;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

public class DialogAskDecisionMaker {
    public RadioButton alternative1;
    public RadioButton alternative2;

    public int preference = 0;
    public double value = 0.0;
    public Label criterionLabel;
    private Stage dialogStage;

    public void handleIndifference(ActionEvent actionEvent) { handleButton(Scale.values[0], false); }
    public void handleOption1(ActionEvent actionEvent) { handleButton(Scale.values[1], true); }
    public void handleModeratePreference(ActionEvent actionEvent) { handleButton(Scale.values[2], true); }
    public void handleOption3(ActionEvent actionEvent) { handleButton(Scale.values[3], true); }
    public void handleOption5(ActionEvent actionEvent)  { handleButton(Scale.values[5], true); }
    public void handleOption7(ActionEvent actionEvent)  { handleButton(Scale.values[7], true); }
    public void handleStrongPreference(ActionEvent actionEvent)  { handleButton(Scale.values[4], true); }
    public void handleVeryStrongPreference(ActionEvent actionEvent)  { handleButton(Scale.values[6], true); }
    public void handleExtremePreference(ActionEvent actionEvent)  { handleButton(Scale.values[8], true); }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAlternatives(String alternativeI, String alternativeJ, String criterionId ) {
        alternative1.setText(alternativeI);
        alternative2.setText(alternativeJ);
        criterionLabel.setText(criterionId);
    }

    private void handleButton(double value, boolean warn ) {
        if( warn ){
            if( alternative1.isSelected() && !alternative2.isSelected() ){
                preference = 1;
            }else if( alternative2.isSelected() && !alternative1.isSelected() ){
                preference = 2;
            }else{
                RootLayoutController.showErrorAlert(
                        "you must choose one alternative!\n",dialogStage);
                return;
            }
        }else{
            preference = 1;
        }
        this.value = value;
        dialogStage.close();
    }
}

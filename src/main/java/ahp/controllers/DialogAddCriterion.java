package ahp.controllers;

import ahp.models.AHP;
import ahp.Main;
import ahp.models.PairwiseComparison;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;

public class DialogAddCriterion {
    public AnchorPane dialogRoot;
    public VBox mainBox;
    public TextField criterionsField;
    public TextField parentIdField;
    public RadioButton alternativesRadioButton;
    public RadioButton headRadioButton;
    public Button okButton;
    public TextField idField;
    public RadioButton cutPreviousParents;
    private Stage dialogStage;
    private PairwiseComparison criterion;
    private boolean okClicked = false;

    public void handleCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void handleOK(ActionEvent actionEvent) {
        handleOK();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        dialogRoot.setOnKeyPressed((event) -> {
            if(event.getCode() == KeyCode.ENTER) {
                handleOK();
            }
        });
        idField.setText("");
        parentIdField.setText("");
        criterionsField.setText("");
    }

    private void handleOK() {
        if( isInputValid() ){
            okClicked = true;
            dialogStage.close();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";
        //id
        String id = idField.getText().trim();
        if( id.equals("")){
            errorMessage += "please fill \"id\" field";
        }else if( AHP.idOccupied(id) ){
            errorMessage += "id: [" + id + "] already exists!\n";
        }else{
            criterion.id = id;
        }
        //parents
        if( headRadioButton.isSelected() ){
            criterion.setParent(Main.AHP.headId);
        }else{
            String[] parents = parentIdField.getText().split(",");
            if( parents.length < 1 ){
                errorMessage += "Please fill parents field\n";
            }
            for( int i=0; i<parents.length; i++ ){
                parents[i] = parents[i].trim();
                if( !AHP.idOccupied(parents[i])){
                    errorMessage += "parent: [" + parents[i] + "] does not exist!\n";
                }else if( Main.AHP.alternatives.contains(parents[i]) ){
                    errorMessage += "alternative: [" + parents[i] + "] is not allowed to be parent!\n";
                }
            }
            criterion.parents = Arrays.asList(parents);
        }
        //alternatives
        if( alternativesRadioButton.isSelected() ){
            criterion.alternatives = Main.AHP.alternatives;
        }else{
            String[] childrenCriterions = criterionsField.getText().split(",");
            if( childrenCriterions.length < 1 ){
                errorMessage += "Please fill criterions field\n";
            }
            for( int i=0; i<childrenCriterions.length; i++ ){
                childrenCriterions[i] = childrenCriterions[i].trim();
                if( !AHP.idOccupied(childrenCriterions[i])){
                    errorMessage += "criterion: [" + childrenCriterions[i] + "] does not exist!\n";
                }else if( Main.AHP.alternatives.contains(childrenCriterions[i])){
                    errorMessage += "alternative: [" + childrenCriterions[i] + "] is not allowed to be criterion!\n";
                }
            }
            criterion.alternatives = Arrays.asList(childrenCriterions);
        }
        criterion.cutPreviousParents = cutPreviousParents.isSelected();
        return RootLayoutController.showErrorAlert(errorMessage,dialogStage);
    }

    public void setCriterion(PairwiseComparison criterion) {
        this.criterion = criterion;
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}

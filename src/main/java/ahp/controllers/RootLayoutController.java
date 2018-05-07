package ahp.controllers;

import ahp.models.AHP;
import ahp.Main;
import ahp.graph.Graph;
import ahp.models.PairwiseComparison;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class RootLayoutController {

    public Pane overlay;

    public void handleOpen(ActionEvent actionEvent) {
        FileChooser fileChooser = getFileChooser();
        fileChooser.setTitle("Open AHP");
        setExtension(fileChooser,"JSON files (*.json)", "*.json");
        File file = fileChooser.showOpenDialog(Main.primaryStage);
        if( file != null ){
            Main.currentFile = file;
            AHP.loadFromJson();
        }
    }

    public void handleSaveAs(ActionEvent actionEvent) {
        FileChooser fileChooser = getFileChooser();
        setExtension(fileChooser,"JSON files (*.json)", "*.json");
        fileChooser.setTitle("Save Project");

        if( Main.currentFile != null ){
            fileChooser.setInitialFileName(Main.currentFile.getName());
        }else{
            fileChooser.setInitialFileName("AHP.json");
        }
        File file = fileChooser.showSaveDialog(Main.primaryStage);
        if( file != null ){
            Main.currentFile = file;
            AHP.saveToJson();
        }
    }

    private static void setExtension( FileChooser fileChooser, String description, String ext ){
        FileChooser.ExtensionFilter extensionFilter =
                new FileChooser.ExtensionFilter(description, ext);
        fileChooser.getExtensionFilters().add(extensionFilter);
    }

    private static FileChooser getFileChooser() {
        FileChooser fileChooser = new FileChooser();

        return fileChooser;
    }

    public void handleEditBasics(ActionEvent actionEvent) {
        AHP.Rebuilder rebuilder = new AHP.Rebuilder(Main.AHP.headId, Main.AHP.alternatives);
        if (showEditBasicsDialog()){
            AHP.initComparisons();
            rebuilder.invoke();
            Graph.draw();
            PairwiseComparison.reset(false);
        }
    }

    public void handleAddCriterion(ActionEvent actionEvent) {
        if( Main.AHP.alternatives == null ){ return; }
        PairwiseComparison criterion = new PairwiseComparison();
        if (showAddCriterionDialog(criterion)){
            PairwiseComparison.addCriterion(criterion);
        }
    }

    private boolean showAddCriterionDialog(PairwiseComparison criterion) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/dialog_add_criterion.fxml"));
            AnchorPane dialogPane = loader.load();

            Stage dialogStage = getDialogStage("Add Criterion", dialogPane);

            DialogAddCriterion controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCriterion(criterion);

            dialogStage.showAndWait();
            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean showEditBasicsDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/dialog_edit_basics.fxml"));
            AnchorPane dialogPane = loader.load();

            Stage dialogStage = getDialogStage("Edit Basics", dialogPane);

            DialogEditBasicsController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void init() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/fxml/root_layout.fxml"));

        try {
            Main.rootLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.rootScene = new Scene(Main.rootLayout);
        Main.primaryStage.setScene(Main.rootScene);
        Main.rootLayoutController = loader.getController();
        Main.primaryStage.show();
    }

    public static Stage getDialogStage(String title, AnchorPane dialogPane){
        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(Main.primaryStage);
        Scene dialogScene = new Scene(dialogPane);
        dialogStage.setScene(dialogScene);

        return dialogStage;
    }

    public static boolean showErrorAlert(String errorMessage, Stage dialogStage) {
        if( errorMessage.equals("")){
            return true;
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    public void handleResetPreferences(ActionEvent actionEvent) {
        //make every weight vector and weight ratios null
        PairwiseComparison.reset(true);
    }

    public void handleWeightVector(ActionEvent actionEvent) {
        System.out.println(Arrays.toString(PairwiseComparison.get(Main.AHP.headId).getWeightVector()));
    }
}

package sample;

import classes.*;
import crypto.Algorithm;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.*;
import serialization.Serializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CreationMenuController {

    public ComboBox<String> timeInput;
    public TextField bpmInput;
    public ComboBox<String> keyInput;
    public ComboBox<String> noteInput;
    public CheckBox pluginCheckBox;


    public void initialize(){
        setTimeSigns();
        setKeys();
        setBpmInputListener();
    }


    private void setBpmInputListener(){
        bpmInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")){
                bpmInput.setText(newValue.replaceAll("[^\\d]",""));
            }
        });
        bpmInput.setText("120");
    }

    private void setTimeSigns(){
        ArrayList<String> timesList = new ArrayList<>();
        timesList.add("4/4");
        timesList.add("3/4");
        timesList.add("5/4");
        timesList.add("7/8");
        timesList.add("9/8");


        ObservableList<String> observableListOfObjects = FXCollections.observableList(timesList);
        timeInput.setItems(observableListOfObjects);


        timeInput.getSelectionModel().selectFirst();
    }

    private void setKeys(){
        Utilities.fillNoteComboBox(noteInput);

        ArrayList<String> keysList = new ArrayList<>();
        keysList.add("maj");
        keysList.add("min");

        ObservableList<String> observableListOfObjects2 = FXCollections.observableList(keysList);
        keyInput.setItems(observableListOfObjects2);

        keyInput.getSelectionModel().selectFirst();

    }

    private FXMLLoader getLoader(){
        return new FXMLLoader(getClass().getResource("main.fxml"));
    }

    public void create(ActionEvent actionEvent) {
        Stage currStage = (Stage) timeInput.getScene().getWindow();

        Parent root;
        FXMLLoader loader = getLoader();


        try {
            root = loader.load();

            MainController controller = loader.getController();

            controller.createComposition(Integer.parseInt(bpmInput.getText()),
                    noteInput.getValue() + keyInput.getValue(), timeInput.getValue());

            Stage stage = new Stage();

            stage.setScene(new Scene(root, 1920,1080));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        currStage.close();
    }


    private File loadPlugin(Stage currStage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open decryption plugin");

        return fileChooser.showOpenDialog(currStage);
    }

    public void load(ActionEvent actionEvent) {
        Stage currStage = (Stage) timeInput.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open saved composition");
        FileChooser.ExtensionFilter extFiler = new FileChooser.ExtensionFilter("MM files, XML files (*.mm, *.xml)","*.mm","*.xml");
        fileChooser.getExtensionFilters().add(extFiler);

        File file = fileChooser.showOpenDialog(currStage);

        if (file != null) {


            if(pluginCheckBox.isSelected()){
                File plugin = loadPlugin(currStage);

                Algorithm algorithm = PluginLoader.load(plugin.getAbsolutePath());
                algorithm.decrypt(file);
            }

            File newFile = new File(file.getAbsolutePath());

            //file = new File(file.getName().replace(".xml",".mm"));

            Composition composition = null;

            if(newFile.getName().contains(".xml")) {
                composition = Serializer.deserializeXML(file);
            }else if (newFile.getName().contains(".mm"))
                composition = Serializer.deserialize(file);

            FXMLLoader loader = getLoader();

            Parent root;


            try {
                root = loader.load();

                MainController controller = loader.getController();

                controller.createComposition(composition);

                Stage stage = new Stage();

                stage.setScene(new Scene(root, 1920,1080));
                stage.show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            currStage.close();


        }
    }
}

package sample;
import classes.*;
import crypto.Algorithm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.*;
import serialization.Serializer;

import java.io.File;
import java.util.ArrayList;

public class MainController {
    public GridPane mainGrid;

    public ListView<Canvas> melodyCanvasesList;
    public ListView<Canvas> harmonyCanvasesList;


    public ComboBox<String> noteInput;
    public ComboBox<String> durationInput;
    public ComboBox<String> octaveInput;

    private Composition composition;
    public CheckBox pluginCheckBox;

    public void initialize(){
        melodyCanvasesList.setFocusTraversable(false);
        harmonyCanvasesList.setFocusTraversable(false);

        Utilities.fillNoteComboBox(noteInput);
        Utilities.fillDurationComboBox(durationInput);

        ArrayList<String> octavesList = new ArrayList<>();
        octavesList.add("4");
        octavesList.add("5");

        ObservableList<String> observableListOfObjects3 = FXCollections.observableList(octavesList);
        octaveInput.setItems(observableListOfObjects3);
        octaveInput.getSelectionModel().selectFirst();

    }

    public void createComposition(int bpm, String key, String timeSignature){
        composition = new Composition(bpm,key,timeSignature);
        updateLists();
    }

    public void createComposition(Composition composition){
        this.composition = composition;
        this.composition.load();
        updateLists();
    }

    private void updateLists(){
        melodyCanvasesList.setItems(composition.getMelody().getStaffCanvases());
        harmonyCanvasesList.setItems(composition.getHarmony().getStaffCanvases());
    }

    public void add_note(ActionEvent actionEvent) {
        String duration = durationInput.getValue();
        String name = noteInput.getValue() + octaveInput.getValue();
        composition.getMelody().addNote(duration,name);
//        updateLists();
    }

    public void add_chord(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setStyle("-fx-border-style: solid inside; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 5;");

        gridPane.add(new Text("Сhord duration:"),0,0);

        ComboBox<String> durationInput = new ComboBox<>();
        Utilities.fillDurationComboBox(durationInput);
        gridPane.add(durationInput, 0, 1);

        gridPane.add(new Text("Сhord notes:"),0,2);

        TextField notesField = new TextField();
        notesField.setEditable(false);
        gridPane.add(notesField,0,3);

        gridPane.add(new Text("Add note:"),1,0);

        ComboBox<String> noteInput = new ComboBox<>();
        ComboBox<Integer> octaveInput = new ComboBox<>();
        Utilities.fillNoteComboBox(noteInput);
        ArrayList<Integer> octavesList = new ArrayList<>();
        octavesList.add(4);
        octavesList.add(5);
        ObservableList<Integer> observableListOfObjects = FXCollections.observableList(octavesList);
        octaveInput.setItems(observableListOfObjects);

        gridPane.add(noteInput, 1, 1);
        gridPane.add(octaveInput, 2, 1);


        Button addNoteButton = new Button("Add note");
        addNoteButton.setOnAction(event -> {
            String name = noteInput.getValue() + octaveInput.getValue();
            notesField.setText(notesField.getText() + name + " ");
        });
        addNoteButton.setAlignment(Pos.BASELINE_LEFT);
        gridPane.add(addNoteButton, 1, 2);

        Button deleteNoteButton = new Button("Delete note");
        deleteNoteButton.setOnAction(event -> {
            String notes[] = notesField.getText().split(" ");

            StringBuilder newStr = new StringBuilder();

            for (int i = 0; i< notes.length - 1; i++){
                newStr.append(notes[i]).append(" ");
            }
            notesField.setText(newStr.toString());
        });

        gridPane.add(deleteNoteButton, 2, 2);

        Button addChord = new Button("Add chord");
        addChord.setOnAction(event -> {
            String duration = durationInput.getValue();
            composition.getHarmony().addChord(duration,notesField.getText());
            stage.close();
        });

        gridPane.add(addChord, 0, 4);

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> stage.close());

        gridPane.add(cancelButton, 2, 4);

        stage.setScene(new Scene(gridPane));
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

//        updateLists();
    }

    private File openPluginFile(Stage currStage){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pick encryption plugin");

        return fileChooser.showOpenDialog(currStage);
    }


    private File saveFile(Stage currStage){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save composition");
        FileChooser.ExtensionFilter extFiler = new FileChooser.ExtensionFilter("MM files, XML files (*.mm), (*.xml)","*.mm", "*.xml");
        fileChooser.getExtensionFilters().add(extFiler);

        return fileChooser.showSaveDialog(currStage);
    }

    public void save(ActionEvent actionEvent) {
        Stage currStage = (Stage) harmonyCanvasesList.getScene().getWindow();

        File file = saveFile(currStage);

        Serializer.serialize(composition,file);

   /*     if(file.getName().contains(".xml")) {
            Serializer.serializeXML(composition,file);
        }else if (file.getName().contains(".mm")){
            Serializer.serialize(composition, file);
        } else
            Serializer.serialize(composition, new File(file.getAbsolutePath() + ".mm"));*/

        if(pluginCheckBox.isSelected()){
            try {
                File encryptionPlugin = openPluginFile(currStage);

                Algorithm algorithm = PluginLoader.load(encryptionPlugin.getAbsolutePath());

                algorithm.encrypt(file);

            } catch (NullPointerException e){
                e.printStackTrace();
            }

        }

    }
}


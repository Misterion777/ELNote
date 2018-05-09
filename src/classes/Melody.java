package classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.xml.bind.annotation.XmlType;

import java.util.ArrayList;



@XmlType
public class Melody extends CompositionUnit {

    public Melody(int bpm, String timeSignature, String key) {
        super(bpm, timeSignature, key);
    }
    public Melody(){}

    public void openEditMenu(int x){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        GridPane gridPane = Utilities.createGrid();

        gridPane.add(new Text("Change note:"),0,0,2,1);
        gridPane.add(new Text("Change duration:"),2,0);

        ComboBox<String> noteInput = new ComboBox<>();
        ComboBox<Integer> octaveInput = new ComboBox<>();
        ComboBox<String> durationInput = new ComboBox<>();

        Utilities.fillNoteComboBox(noteInput);
        Utilities.fillDurationComboBox(durationInput);
        ArrayList<Integer> octavesList = new ArrayList<>();
        octavesList.add(4);
        octavesList.add(5);
        ObservableList<Integer> observableListOfObjects = FXCollections.observableList(octavesList);
        octaveInput.setItems(observableListOfObjects);

        String currNote = unitsMap.get(x).toString();

        octaveInput.getSelectionModel().select(Character.getNumericValue(currNote.charAt(currNote.length() - 1)));
        noteInput.getSelectionModel().select(currNote.replaceAll("\\d",""));

        gridPane.add(noteInput, 0, 1);
        gridPane.add(octaveInput, 1, 1);
        gridPane.add(durationInput, 2, 1);
        Button editButton = new Button("Edit note");
        editButton.setOnAction(event -> {
            String name = noteInput.getValue() + octaveInput.getValue();
            String duration = durationInput.getValue();

            editUnit(x, name, duration);
            stage.close();

        });
        Button deleteButton = new Button("Delete note");
        deleteButton.setOnAction(event -> {
            deleteUnit(x);
            stage.close();

        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            stage.close();
        });

        cancelButton.setAlignment(Pos.BASELINE_RIGHT);
        gridPane.add(editButton, 0, 2,2,1);
        gridPane.add(deleteButton, 0, 3,2,1);
        gridPane.add(cancelButton, 2, 3);


        stage.setScene(new Scene(gridPane));
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public void addNote(String duration, String name){
        Note newNote = new Note(bpm, name, duration);

        super.addUnit(newNote);
    }





}

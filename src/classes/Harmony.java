package classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


@XmlType
public class Harmony extends CompositionUnit {

    public Harmony(int bpm, String timeSignature, String key) {
        super(bpm, timeSignature, key);
    }
    public Harmony(){}

    @Override
    public void openEditMenu(int x) {


        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        GridPane gridPane = Utilities.createGrid();


        gridPane.add(new Text("Сhord duration:"),0,0);

        ComboBox<String> durationInput = new ComboBox<>();
        Utilities.fillDurationComboBox(durationInput);
        gridPane.add(durationInput, 0, 1);

        gridPane.add(new Text("Сhord notes:"),0,2);

        TextField notesField = new TextField();
        notesField.setText(unitsMap.get(x).toString());
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
        octaveInput.getSelectionModel().selectFirst();

        gridPane.add(noteInput, 1, 1);
        gridPane.add(octaveInput, 2, 1);


        Button addNoteButton = new Button("Add note");
        addNoteButton.setOnAction(event -> {
            String name = noteInput.getValue() + octaveInput.getValue();
            notesField.setText(notesField.getText() + name + " ");
        });
        Button deleteNoteButton = new Button("Delete note");
        deleteNoteButton.setOnAction(event -> {
            String notes[] = notesField.getText().split(" ");

            StringBuilder newStr = new StringBuilder();

            for (int i = 0; i< notes.length - 1; i++){
                newStr.append(notes[i]).append(" ");
            }
            notesField.setText(newStr.toString());
        });

        addNoteButton.setAlignment(Pos.BASELINE_LEFT);
        gridPane.add(addNoteButton, 1, 2);
        gridPane.add(deleteNoteButton, 2, 2);

        Button changeChord = new Button("Change chord");
        changeChord.setOnAction(event -> {
            String duration = durationInput.getValue();
            editUnit(x, duration, notesField.getText());
            stage.close();
        });

        Button deleteChord = new Button("Delete chord");
        deleteChord.setOnAction(event -> {
            deleteUnit(x);
            stage.close();
        });

        gridPane.add(changeChord, 0, 4);
        gridPane.add(deleteChord, 1, 4);

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            stage.close();
        });

        gridPane.add(cancelButton, 2, 4);

        stage.setScene(new Scene(gridPane));
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public void addChord(String duration, String notes){
        Chord newChord = new Chord(bpm, duration,notes);

        super.addUnit(newChord);
    }




}

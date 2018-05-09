package classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utilities {


    public static GridPane createGrid(){
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setStyle("-fx-border-style: solid inside; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 5;");
        return gridPane;
    }

    public static void drawClef(GraphicsContext gc, int x, int y){
        String path = "/assets/clef.png";
        Image image = new Image(path);
        gc.drawImage(image,x,y, 100,100);
    }

    public static void drawLines(GraphicsContext gc){
        double width = gc.getCanvas().getWidth();
        gc.setLineWidth(2);
        for (int i = 0; i<5;i++){
            gc.strokeLine(0,i*20 + 40,width,i*20 + 40);
        }
    }


    public static void fillNoteComboBox(ComboBox<String> box){
        String NOTES = "CDEFGAB";
        ArrayList<String> notesList = new ArrayList<>();

        for(int i = 0; i < NOTES.length(); i++){
            notesList.add(NOTES.charAt(i) + "b");
            notesList.add(Character.toString(NOTES.charAt(i)));
            notesList.add(NOTES.charAt(i) + "#");
        }


        notesList.remove("Cb");
        notesList.remove("E#");
        notesList.remove("Fb");
        notesList.remove("B#");

        ObservableList<String> observableListOfObjects = FXCollections.observableList(notesList);
        box.setItems(observableListOfObjects);

        box.getSelectionModel().selectFirst();
    }

    public static void fillDurationComboBox(ComboBox<String> box){
        ArrayList<String> durationsList = new ArrayList<>();
        durationsList.add("whole");
        durationsList.add("half");
        durationsList.add("quarter");
        durationsList.add("eighth");
        durationsList.add("sixteenth");

        ObservableList<String> observableListOfObjects2 = FXCollections.observableList(durationsList);
        box.setItems(observableListOfObjects2);

        box.getSelectionModel().selectFirst();

    }


    public static Object[] reverse (Object[] arr){
        List<Object> list = Arrays.asList(arr);
        Collections.reverse(list);
        return list.toArray();
    }

}



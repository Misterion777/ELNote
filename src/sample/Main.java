package sample;


import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Canvas canvas = new Canvas(400, 400);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.CORNSILK);
        gc.fillRect(0, 0, 400, 400);

        canvas.setStyle("-fx-effect: innershadow(gaussian, #039ed3, 10, 1.0, 0, 0);");

        /*ObjectProperty<Canvas> selectedCanvas = new SimpleObjectProperty<>();
        selectedCanvas.addListener((obs, oldCanvas, newCanvas) -> {
            if (oldCanvas != null) {
                oldCanvas.setStyle("");
            }
            if (newCanvas != null) {
                newCanvas.setStyle("-fx-effect: innershadow(gaussian, #039ed3, 10, 1.0, 0, 0);");
            }
        });

        canvas.setOnMouseClicked(e -> selectedCanvas.set(canvas));
*/


        FXMLLoader loader = new FXMLLoader(getClass().getResource("creation_menu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setUserData(loader);
        primaryStage.setTitle("Music Man Yo");
        primaryStage.setScene(scene);
        primaryStage.show();


//        HBox root = new HBox(10, canvas);
//        primaryStage.setScene(new Scene(root, 800, 500));
//        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

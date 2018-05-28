package sample;

import classes.Composition;
import classes.PluginLoader;
import crypto.Algorithm;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import serialization.Serializer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

interface Step<T, U> {
    U execute(T input);
}

class StepOne implements Step<Stage, File> {
    @Override
    public File execute(Stage input) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save composition");
        FileChooser.ExtensionFilter extFiler = new FileChooser.ExtensionFilter("MM files, XML files (*.mm), (*.xml)","*.mm", "*.xml");
        fileChooser.getExtensionFilters().add(extFiler);

        return fileChooser.showSaveDialog(input);
    }
}

class StepTwo implements Step<File, File> {
    private Composition composition;
    StepTwo(Composition composition){
        this.composition = composition;
    }
    @Override
    public File execute(File input) {
        return Serializer.serialize(composition, input);
    }
}

class StepThree implements Step<File, Boolean> {
    private boolean encryptionNeeded;
    private Stage currStage;
    StepThree(Stage currStage, boolean encryptionNeeded){
        this.currStage = currStage;
        this.encryptionNeeded = encryptionNeeded;
    }

    @Override
    public Boolean execute(File input) {
        if(encryptionNeeded){
            try {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Pick encryption plugin");

                File encryptionPlugin = fileChooser.showOpenDialog(currStage);

                Algorithm algorithm = PluginLoader.load(encryptionPlugin.getAbsolutePath());

                algorithm.encrypt(input);

            } catch (NullPointerException e){
                e.printStackTrace();
            }

            return true;

        }
        return false;
    }

}

public class Pipeline {
    private List<Step> pipelineSteps = new ArrayList<>();
    private Object firstStepInput;

    public Pipeline(Stage stage, Composition composition, boolean encryptionNeeded){
        firstStepInput = stage;

        pipelineSteps.add(new StepOne());
        pipelineSteps.add(new StepTwo(composition));
        pipelineSteps.add(new StepThree(stage, encryptionNeeded));
    }

    public void addStep(Step step) {
        pipelineSteps.add(step);
    }

    public void execute() {
        for (Step step : pipelineSteps) {
            Object out = step.execute(firstStepInput);
            firstStepInput = out;
        }
        System.out.println("Encrypted: " + firstStepInput);
    }
}
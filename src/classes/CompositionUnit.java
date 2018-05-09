package classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sample.MainController;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


@XmlType
public abstract class CompositionUnit extends MusicUnit {

    private static final int INTERVAL = 60;
    private static final int STARTX = 10;

    private transient ObservableList<Canvas> staffCanvases;

    @XmlElement
    private int lastX;

    @XmlElement
    public Map<Integer,SingleUnit> unitsMap;

    @XmlElement
    private String timeSignature;

    @XmlElement
    private String key;


    CompositionUnit(int bpm, String timeSignature, String key){
        super(bpm);

        this.key = key;
        this.timeSignature = timeSignature;
        this.unitsMap = new LinkedHashMap<>();


        setBarDuration(timeSignature);
        initDraw();
    }

    CompositionUnit(){}

    private int getLastX() {
        return lastX;
    }

    public ObservableList<Canvas> getStaffCanvases() {
        return staffCanvases;
    }

    private GraphicsContext getCurrentContext(){
        return staffCanvases.get(staffCanvases.size() - 1).getGraphicsContext2D();
    }

    private Image getTimeSignatureImage(){
        String path = ASSETS + timeSignature.replace('/','_') + ".png";
        return new Image(path);
    }

    private void addCanvas(){
        Canvas canvas = new Canvas(1600, 160);
        canvas.setOnMouseClicked(event -> {
            Integer x = (int) event.getX();
            System.out.println("clicked: " + x);
            x = isNoteExist(x);
            if (x != null )
                openEditMenu(x);
        });
        staffCanvases.add(canvas);
    }

    public abstract void openEditMenu(int x);


    private void createStaff(GraphicsContext gc){

        Utilities.drawLines(gc);

        Utilities.drawClef(gc,0,30);
        incrementLastX(80);

        createSigns(gc);

        gc.drawImage(getTimeSignatureImage(),lastX + 10,35,50,90);
        incrementLastX(80);
    }

    private void createSigns(GraphicsContext gc){

        String[] signs = { "F5", "C5", "G5", "D5", "A4", "E5", "B4"};

        int iter = 0;
        switch (key){
            case "Dmin":
            case "Fmaj":
            case "Emin":
            case "Gmaj":
                iter = 1;
                break;
            case "Bbmaj":
            case "Gmin":
            case "Bmin":
            case "Dmaj":
                iter = 2;
                break;
            case "Ebmaj":
            case "Cmin":
            case "F#min":
            case "Amaj":
                iter = 3;
                break;
            case "Abmaj":
            case "Fmin":
            case "C#min":
            case "Emaj":
                iter = 4;
                break;
            case "Dbmaj":
            case "Bbmin":
            case "G#min":
            case "Bmaj":
                iter = 5;
                break;
            case "Gbmaj":
            case "Ebmin":
            case "D#min":
            case "F#maj":
                iter = 6;
                break;
            case "Cbmaj":
            case "C#maj":
                iter = 7;
                break;
        }

        String sign = "b";

        if (!key.contains("b") && key.charAt(0) != 'F'){
            sign = "#";

        }else{
            signs = (String[]) Utilities.reverse(signs);
        }

        for(int i = 0; i< iter; i++){
            String name = signs[i].charAt(0) + sign + signs[i].charAt(1);
            Note note = new Note(name);
            note.drawSignature(gc,getLastX() + i*15);
        }
        incrementLastX(iter * 15);
    }


    private void incrementLastX(int value){
        lastX += value;
        checkLine();
    }
    private void checkBar(){
        if (isBarFull()){
            getCurrentContext().strokeLine(lastX,40,lastX,120);
            lastX +=15;
        }

    }

    private void checkLine(){
        if(lastX > getCurrentContext().getCanvas().getWidth()){
            addCanvas();
            Utilities.drawLines(getCurrentContext());
            lastX = STARTX;
        }
    }

    private Integer isNoteExist(int x){
        for(int i = x - 20; i < x + 20; i++){
            if (unitsMap.containsKey(i)){
                return i;
            }
        }
        return null;
    }

    public void initDraw(){
        staffCanvases = FXCollections.observableArrayList();
        redraw();
    }

    private void redraw(){
        staffCanvases.clear();
        fullDuration = 0;
        lastX = STARTX;
        addCanvas();
        createStaff(getCurrentContext());

        drawMap();
    }

    private void drawMap(){
        Map<Integer, SingleUnit> newMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, SingleUnit> singleUnit: unitsMap.entrySet()) {
            singleUnit.getValue().draw(getCurrentContext(),lastX);
            newMap.put(getLastX(), singleUnit.getValue());
            fullDuration += singleUnit.getValue().getDurationEighth();
            incrementLastX(INTERVAL);
            checkBar();
        }
        unitsMap = newMap;

    }

    void editUnit(int x, String newName, String newDuration){

        unitsMap.get(x).change(newName, newDuration);
        redraw();
    }

    void deleteUnit(int x){

        unitsMap.remove(x);
        redraw();
    }

    void addUnit(SingleUnit newUnit){
        newUnit.draw(getCurrentContext(), getLastX());

        unitsMap.put(getLastX(), newUnit);
        fullDuration += newUnit.getDurationEighth();

        incrementLastX(INTERVAL);
        checkBar();
    }



}

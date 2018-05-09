package classes;

import javafx.scene.canvas.GraphicsContext;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlType
@XmlRootElement
public class SingleUnit extends MusicUnit {

    @XmlElement
    private double durationSec;
    @XmlElement
    private double durationEighth;

    public SingleUnit(int bpm, String duration){
        super(bpm);
        setDurations(duration);
    }
    public SingleUnit(){}

    public  void draw(GraphicsContext gc, int x){}
    public void change(String newName, String newDuration){}
    public String toString(){return null;}


    public double getDurationEighth() {
        return durationEighth;
    }

    public void setDurations(String duration){

        switch (duration) {
            case "quarter":
                durationEighth = 2;
                durationSec = quarterDuration;
                break;
            case "half":
                durationEighth = 4;
                durationSec = quarterDuration * 2;
                break;
            case "whole":
                durationEighth = 8;
                durationSec = quarterDuration * 4;
                break;
            case "eighth":
                durationEighth = 1;
                durationSec = quarterDuration / 2;
                break;
            case "sixteenth":
                durationEighth = 0.5;
                durationSec = quarterDuration / 4;
                break;

        }
    }




}

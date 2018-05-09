package classes;

import sample.MainController;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType
abstract class MusicUnit implements Serializable{

    static final String ASSETS = "/assets/";
    static final String NOTES = "CDEFGAB";

    @XmlElement
    int bpm;
    @XmlElement
    private int barDuration;
    @XmlElement
    int quarterDuration;
    @XmlElement
    double fullDuration;

    MusicUnit(int bpm){
        this.bpm = bpm;
        quarterDuration = 60 / bpm;
        fullDuration = 0;

    }

    MusicUnit(){};


    void setBarDuration(String timeSignature){
        barDuration = Character.getNumericValue(timeSignature.charAt(0));

        if(timeSignature.charAt(2) == '4'){
            barDuration *= 2;
        }
    }


    boolean isBarFull() {
        return !(fullDuration == 0) && fullDuration % barDuration == 0;

    }
}

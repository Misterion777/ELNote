package classes;

import java.io.Serializable;
import javax.xml.bind.annotation.*;

@XmlType
@XmlRootElement
public class Composition implements Serializable {

    @XmlElement
    private Melody melody;
    @XmlElement
    private Harmony harmony;

    @XmlElement
    private int bpm;

    @XmlElement
    private String key;

    @XmlElement
    private String timeSignature;

    public Composition(){}
    public Composition(int bpm, String key, String timeSignature){
        this.bpm = bpm;
        this.key = key;
        this.timeSignature = timeSignature;
        createMelody();
        createHarmony();
    }

    public void load(){
        getMelody().initDraw();
        getHarmony().initDraw();
    }

    private void createMelody(){
        melody = new Melody(bpm,timeSignature,key);
    }

    private void createHarmony(){
        harmony = new Harmony(bpm,timeSignature,key);
    }

    public Melody getMelody() {
        return melody;
    }

    public Harmony getHarmony() {
        return harmony;
    }


}


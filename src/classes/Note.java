package classes;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlType
@XmlRootElement
public class Note extends SingleUnit {
/*    private static final int OCTAVE = 4;
    private static final int OCTAVE_HALFSTEPS = 12;
    private static final double COEFFICIENT = 1.05946309435929;
    private static final int FREQUENCY_A_4 = 440;
    private static final String NOTES = "cdefgab";

    private double pitch;*/


    /*private Image noteImage;
    private Image durationImage;
    private Image signatureImage;*/

//    static final long serialVersionUID = -895341052826385482L;
    @XmlElement
    private int staffLocation;
    @XmlElement
    private String name;
    @XmlElement
    private String duration;
    @XmlElement
    private Boolean isInChord;


    public Note(int bpm,String name, String duration) {
        super(bpm,duration);
        isInChord = false;
        change(name,duration);
    }

    public Note(){}

    public String toString(){
        return duration + " " + name ;
    }

    public Integer getOctave(){
        return Character.getNumericValue(name.charAt(name.length() - 1));
    }

    public void destroyDurationImage() {

       this.isInChord = true;
    }

    public Note(String name){
        super(1,"");
        this.name = name;
        int octave = getOctave();
        setStaffLocation(octave);

    }

    public int getStaffLocation() {
        return staffLocation;
    }

    public void setStaffLocation(int octave){
        staffLocation = ((octave == 4) ? 130 : 60) - NOTES.indexOf(name.charAt(0)) * 10;
    }

    private Image getNoteImage(){
        String notePath = ASSETS;

        switch(duration){
            case "half":
                notePath += "note_whole.png";
                break;
            case "whole":
                notePath += "note_whole.png";
                break;
            default:
                notePath +="note.png";
                break;
        }

        return new Image(notePath);
    }

    public Image getDurationImage(){
        String durationPath = ASSETS;

        switch(duration){
            case "half":
                durationPath += "quarter.png";
                break;
            case "whole":
                durationPath +="empty.png";
                break;
            case "null":
                durationPath += "empty.png";
            default:
                durationPath += duration + ".png";
                break;

        }

        if(isInChord)
            durationPath = ASSETS + "empty.png";

        return new Image(durationPath);
    }

    public Image getSignatureImage() {
        String path = ASSETS + "empty.png";
        if (name.length() == 3){
            path = ASSETS + name.charAt(1) + ".png";
        }
        return new Image(path);
    }

    public void draw(GraphicsContext gc, int x){
        gc.drawImage(getNoteImage(),x, staffLocation,20, 20);

        drawSignature(gc, x);

        gc.drawImage(getDurationImage(),x + 18, staffLocation - 40,20,50);
    }

    @Override
    public void change(String name, String duration) {
        this.name = name;
        this.duration = duration;
        setDurations(duration);
        int octave = getOctave();
        setStaffLocation(octave);
    }

    public void drawSignature(GraphicsContext gc, int x){
        gc.drawImage(getSignatureImage(), x -20, staffLocation - 5, 30, 30);
    }

    /*private void setPitch (){
        int halfsteps = getHalfsteps();
        System.out.println("Halfsteps: " + halfsteps);
        pitch = calcPitch(halfsteps);
        System.out.println("Pitch: " + pitch + "Hz");
    }

    private int getHalfsteps(){
        int halfsteps = 0;

        halfsteps += 2 * (NOTES.indexOf(name.charAt(0)) - 5);

        //+ one halfstep
        if (name.charAt(0) > 'b' && name.charAt(0) < 'f'){
            halfsteps += 1;
        }

        //sharps and flats
        if (name.length() == 2){
            if (name.charAt(1) == '#'){
                halfsteps += 1;
            }else if (name.charAt(1) == 'b'){
                halfsteps -= 1;
            }
        }

        int diff = (Character.getNumericValue(octave) - OCTAVE) * OCTAVE_HALFSTEPS;

        halfsteps += diff;

        System.out.println("half" + halfsteps);
        return halfsteps;
    }

    private double calcPitch(int halfsteps){

        System.out.println(COEFFICIENT);
        return FREQUENCY_A_4 * Math.pow(COEFFICIENT,halfsteps);
    }


    public void play() throws LineUnavailableException {
        float frequency = 44100;
        int volume = 100;
        byte[] buf = new byte[1];
        AudioFormat af = new AudioFormat(frequency,8,1,true,false);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        for (int i = 0; i< getDurationSec() *frequency/1000; i++){
            double angle = i/(frequency/pitch)*2.0*Math.PI;
            buf[0] = (byte) (Math.sin(2*angle)*volume);
            sdl.write(buf,0,1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }*/
}

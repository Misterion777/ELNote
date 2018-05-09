package classes;

import javafx.scene.canvas.GraphicsContext;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;

@XmlType
@XmlRootElement
public class Chord extends SingleUnit {

    @XmlElement
    private ArrayList<Note> notes;

    public Chord(int bpm, String duration, String notes) {
        super(bpm,duration);
        setNotes(notes,duration);
    }

    public Chord(){}

    public void setNotes(String notes, String duration) {
        setDurations(duration);
        this.notes = new ArrayList<>();
        int maxLocation = getMaxStaffLocation(notes,duration);
        for (String name: notes.split(" ")) {

            Note newNote = new Note(bpm, name,duration);
            if (newNote.getStaffLocation() != maxLocation)
                newNote.destroyDurationImage();

            this.notes.add(newNote);
        }
    }

    private int getMaxStaffLocation(String notes, String duration){
        int maxLocation = 500;
        for (String name: notes.split(" ")) {
            Note newNote = new Note(bpm,name, duration);
            if (newNote.getStaffLocation() < maxLocation)
                maxLocation = newNote.getStaffLocation();
        }
        return maxLocation;
    }

    @Override
    public void draw(GraphicsContext gc, int x) {
        for (Note note: notes) {
            note.draw(gc,x);
        }
    }

    @Override
    public void change(String newNotes, String newDuration) {
        setNotes(newNotes,newDuration);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Note note: notes) {
            str.append(note.toString()).append(" ");
        }
        return str.toString();
    }
}

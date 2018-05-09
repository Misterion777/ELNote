package classes;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import classes.Composition;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;


public class Serializer {

    public static void serialize(Composition composition, File file){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(composition);
            out.close();
            fileOutputStream.close();
            System.out.println("success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serializeXML(Composition composition, File file){


        try {
            JAXBContext context = JAXBContext.newInstance(Composition.class);

            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


            m.marshal(composition, file);
            System.out.println("success");
        }catch (JAXBException e){
            e.printStackTrace();
        }

    }

    public static Composition deserializeXML(File file) {

        Composition composition = null;
        try {
            JAXBContext context = JAXBContext.newInstance(Composition.class);

            Unmarshaller unmarshaller = context.createUnmarshaller();

            composition = (Composition) unmarshaller.unmarshal(file);

            System.out.println("success");
        }catch (JAXBException e){
            e.printStackTrace();
        }
        return composition;

    }

    public static Composition deserialize(File file){
        Composition composition = null;
        try {
            FileInputStream filein = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(filein);
            composition = (Composition) in.readObject();
            in.close();
            filein.close();
            System.out.println("success!");
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException c){
            System.out.println("class not found");
            c.printStackTrace();
        }

        return composition;
    }

}

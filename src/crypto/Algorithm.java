package crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public abstract class Algorithm {

    private String keyFile;
    private String name;


    Algorithm(String name){
        this.name = name;
        keyFile = "key" + name;
        if (!isKeyGenerated())
            generateKey();
    }


    private boolean isKeyGenerated(){
        File file = new File(keyFile);
        return file.length() != 0;
    }

    private void generateKey(){

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(name);

            keyGen.init(256);
            SecretKey secretKey = keyGen.generateKey();
            byte[] encoded = secretKey.getEncoded();
            FileOutputStream outputStream = new FileOutputStream(keyFile);
            outputStream.write(encoded);

        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }


    private SecretKey readKey(){
        try {
            File file = new File(keyFile);
            FileInputStream inputStream = new FileInputStream(file);
            byte[] inputBytes = new byte[(int) file.length()];
            inputStream.read(inputBytes);

            inputStream.close();

            return new SecretKeySpec(inputBytes, name);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void encrypt(File file){

        try{

            Cipher cipher = Cipher.getInstance(name);
            cipher.init(Cipher.ENCRYPT_MODE, readKey());

            FileInputStream inputStream = new FileInputStream(file);
            byte[] inputBytes = new byte[(int) file.length()];
            inputStream.read(inputBytes);

            inputStream.close();

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(outputBytes);

            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }

    }


    public void decrypt(File file){

        try{
            Cipher cipher = Cipher.getInstance(name);
            cipher.init(Cipher.DECRYPT_MODE, readKey());

            FileInputStream inputStream = new FileInputStream(file);
            byte[] inputBytes = new byte[(int) file.length()];
            inputStream.read(inputBytes);

            inputStream.close();

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(outputBytes);

            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }

    }
}

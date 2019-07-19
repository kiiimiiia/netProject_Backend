package net.net.base;


//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Convertor {

    public static String encodeImage(byte[] image) {
        return String.valueOf(Base64.getEncoder().encode(image));
    }

    public static byte[] decodeImage(String imageString) {
        return Base64.getDecoder().decode(imageString);
    }

    public static byte[] bufImageToByteArray(String filename) {
        byte[] imageInByte = null;
        try {

            BufferedImage originalImage = ImageIO.read(new File(
                    filename));

            // convert BufferedImage to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "jpg", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return imageInByte;
    }

    public static void main(String[] args) {
        System.out.println(encodeImage((bufImageToByteArray("F:\\7.jpg"))));
    }


}


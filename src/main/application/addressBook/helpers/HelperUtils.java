package addressBook.helpers;

import addressBook.controllers.MainController;
import addressBook.models.Contact;
import javafx.beans.property.StringProperty;

import java.beans.*;
import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelperUtils {
    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public static <M> ArrayList<M> xmlEncode(String fileUrl) {
        FileInputStream fis = null;
        ArrayList<M> resultList = new ArrayList<>();

        try {
            fis = new FileInputStream( fileUrl );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        XMLDecoder decoder = new XMLDecoder(fis);

        try {
            for (;;) {
                resultList.add((M) decoder.readObject());
            }
        } catch (ArrayIndexOutOfBoundsException exc) {
            // do nothing
        }

        decoder.close();

        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public static <M> void xmlDecode(String fileName, List<M> objects) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);

            XMLEncoder encoder = new XMLEncoder(fos);

            encoder.setExceptionListener(new ExceptionListener() {
                public void exceptionThrown(Exception e) {
                    System.out.println("Exception! :" + e.toString());
                }
            });

            encoder.setPersistenceDelegate(LocalDate.class,
                    new PersistenceDelegate() {
                        @Override
                        protected Expression instantiate(Object localDate, Encoder encdr) {
                            return new Expression(localDate,
                                    LocalDate.class,
                                    "parse",
                                    new Object[]{localDate.toString()});
                        }
                    });

            for (M obj: objects) {
                encoder.writeObject(obj);
            }

            encoder.close();

            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

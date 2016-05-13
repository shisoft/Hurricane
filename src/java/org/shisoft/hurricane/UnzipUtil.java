package org.shisoft.hurricane;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by shisoft on 16-5-8.
 */
public class UnzipUtil {
    public static void unzip(byte[] data, String destDirectory) throws IOException {
        try
        {
            byte[] buf = new byte[1024];
            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(new ByteArrayInputStream(data));

            zipentry = zipinputstream.getNextEntry();
            while (zipentry != null)
            {
                //for each entry to be extracted
                String entryName = zipentry.getName().replace("./", "/");
                int n;
                FileOutputStream fileoutputstream;
                File newFile = new File(entryName);
                String directory = newFile.getParent();

                if (directory == null)
                {
                    if(newFile.isDirectory())
                        break;
                }

                String fullPath = destDirectory + entryName;
                String parentDir = new File(fullPath).getParent();

                new File(parentDir).mkdirs();

                fileoutputstream = new FileOutputStream(fullPath);

                while ((n = zipinputstream.read(buf, 0, 1024)) > -1)
                    fileoutputstream.write(buf, 0, n);

                fileoutputstream.close();
                zipinputstream.closeEntry();
                zipentry = zipinputstream.getNextEntry();

            }//while

            zipinputstream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static  void writeBytesToFile (byte [] myByteArray, String path) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(myByteArray);
        fos.close();
    }
}

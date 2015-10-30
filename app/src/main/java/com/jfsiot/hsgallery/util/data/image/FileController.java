package com.jfsiot.hsgallery.util.data.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import timber.log.Timber;

/**
 * Created by SSS on 2015-10-04.
 */
public class FileController {

    public File makeDir(String path){
        File dir = new File(path);
        if(!dir.exists() && dir.mkdirs()){
            return dir;
        }
        return null;
    }

    public File makeFile(String dirPath, String fileName){
        File dir = new File(dirPath);
        if(dir.isDirectory()){
            File file = new File(dirPath, fileName);
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return file;
            }
        }
        return null;
    }

    public boolean moveFile(String fromPath, String toPath){
        File fromFile = new File(fromPath);
        File toFile = new File(toPath);
        return renameFile(fromFile, toFile);
    }

    public boolean renameFile(String fromPath, String toName){
        File file = new File(fromPath);
        return renameFile(file, new File(file.getParent(), toName));
    }

    public boolean renameFile(File fromFile, File toFile){
        return fromFile.exists() && fromFile.renameTo(toFile);
    }

    public boolean deleteFile(String path){
        File file = new File(path);
        return file.exists() && file.delete();
    }

    public boolean copyFile(String fromPath, String toPath){
        File from = new File(fromPath);
        File to = new File(toPath);
        if(from.exists()){
            try {
                copyDirectory(from, to);
                return true;
            } catch (IOException e) { e.printStackTrace(); }
        }

        return false;
    }
    public void copyDirectory(File sourceLocation , File targetLocation) throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
                throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
            }

            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                    new File(targetLocation, children[i]));
            }
        } else {

            // make sure the directory we plan to store the recording in exists
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                throw new IOException("Cannot create dir " + directory.getAbsolutePath());
            }

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            Timber.d("copy target:%s", targetLocation.getPath());
            in.close();
            out.close();
        }
    }
}

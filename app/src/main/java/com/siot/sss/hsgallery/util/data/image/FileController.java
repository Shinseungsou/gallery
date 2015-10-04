package com.siot.sss.hsgallery.util.data.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by SSS on 2015-10-04.
 */
public class FileController {

    public File makeDir(String path){
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
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
        File file = new File(fromPath);
        if(file.exists()){
            try {
                FileInputStream fileIn = new FileInputStream(file);
                FileOutputStream fileOut = new FileOutputStream(toPath);
                int readcount=0;
                byte[] buffer = new byte[1024];
                while((readcount = fileIn.read(buffer,0,1024))!= -1){
                    fileOut.write(buffer,0,readcount);
                }
                fileOut.close();
                fileIn.close();
                return true;
            } catch (IOException e) { e.printStackTrace(); }
        }

        return false;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javafx.util.Callback;

public class ZipHelper {
    
    /**
     * Constructor
     * 
     * @param callback 
     */
    public ZipHelper(Callback callback) {
        this.callback = callback;
    }
    
    public void zipDir(String dirName, String nameZipFile) throws IOException {
        ZipOutputStream zip = null;
        FileOutputStream fW = null;
        fW = new FileOutputStream(nameZipFile);
        zip = new ZipOutputStream(fW);
        
        length = folderSize(new File(dirName));
        
        addFolderToZip("", dirName, zip);
        zip.close();
        fW.close();
    }

    private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws IOException {
        File folder = new File(srcFolder);
        if (folder.list().length == 0) {
            addFileToZip(path , srcFolder, zip, true);
        }
        else {
            for (String fileName : folder.list()) {
                if (path.equals("")) {
                    addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip, false);
                } 
                else {
                     addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip, false);
                }
            }
        }
    }

    private void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag) throws IOException {
        File folder = new File(srcFile);
        if (flag) {
            zip.putNextEntry(new ZipEntry(path + "/" +folder.getName() + "/"));
        }
        else {
            if (folder.isDirectory()) {
                addFolderToZip(path, srcFile, zip);
            }
            else {
                byte[] buf = new byte[1024];
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
                while ((len = in.read(buf)) > 0) {
                    zip.write(buf, 0, len);
                    
                    long[] p = {(long)len, length};
                    
                    callback.call(p);
                }
            }
        }
    }
    
    private long folderSize(File directory) {
        long l = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                l += file.length();
            else
                l += folderSize(file);
        }
        return l;
    }
    
    private long length = 0L;
    private Callback callback;
}

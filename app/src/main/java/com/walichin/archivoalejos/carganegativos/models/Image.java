package com.walichin.archivoalejos.carganegativos.models;

import android.graphics.Bitmap;

/**
 * Created by bpncool on 2/23/2016.
 */
public class Image {

    private final String filename;
    private final String filepath;
    private final String fileserver;
    private final Bitmap bitmap;

    public Image(String filename, String filepath, String fileserver, Bitmap bitmap) {
        this.filename = filename;
        this.filepath = filepath;
        this.fileserver = fileserver;
        this.bitmap = bitmap;
    }

    public String getFilename() {
        return filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getFileserver() {
        return fileserver;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}

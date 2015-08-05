package com.siot.sss.hsgallery.app.model;

/**
 * Created by SSS on 2015-08-04.
 */

public class ImageSource {
    public String id;
    public String imageId;
    public String data;
    public String size;

    public ImageSource(String id, String data, String imageId, String size){
        this.id = id;
        this.data = data;
        this.imageId = imageId;
        this.size = size;
    }
}

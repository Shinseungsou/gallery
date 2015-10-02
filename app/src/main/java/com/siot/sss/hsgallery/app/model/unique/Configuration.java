package com.siot.sss.hsgallery.app.model.unique;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
  * Created by SSS on 2015-08-06.
  */
@Data
public class Configuration {
    @Getter @Setter private Integer width;
    @Getter @Setter private Integer height;
    @Getter @Setter private Float density;

    public enum GalleryMode{
        DIR, PIC
    }
    @Getter @Setter private GalleryMode galleryMode;

    private static Configuration instance = null;
    public static synchronized Configuration getInstance(){
        if(Configuration.instance == null) Configuration.instance = new Configuration();
        return Configuration.instance;
    }
    public Configuration(){
        this.galleryMode = GalleryMode.DIR;
    }

    public Float getWidthDP(){
        return width / density;
    }
    public Float getHeightDP(){
        return height / density;
    }

}

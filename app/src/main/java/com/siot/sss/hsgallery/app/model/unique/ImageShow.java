package com.siot.sss.hsgallery.app.model.unique;

import com.siot.sss.hsgallery.app.model.ImageSource;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
  * Created by SSS on 2015-08-06.
  */
@Data
public class ImageShow {
    @Getter @Setter private ImageSource imageSource;

    private static ImageShow instance = null;
    public static synchronized ImageShow getInstance(){
        if(ImageShow.instance == null) ImageShow.instance = new ImageShow();
        return ImageShow.instance;
    }
}

package com.siot.sss.hsgallery.app.model.unique;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
  * Created by SSS on 2015-08-06.
  */
@Data
public class DisplayWindow {
    @Getter @Setter private Integer width;
    @Getter @Setter private Integer height;
    @Getter @Setter private Float density;

    private static DisplayWindow instance = null;
    public static synchronized DisplayWindow getInstance(){
        if(DisplayWindow.instance == null) DisplayWindow.instance = new DisplayWindow();
        return DisplayWindow.instance;
    }

    public Float getWidthDP(){
        return width / density;
    }
    public Float getHeightDP(){
        return height / density;
    }

}

package com.siot.sss.hsgallery.app.model;

/**
 * Created by SSS on 2015-08-06.
 */
public class UseLog {
    public Integer id;
    public String date;
    public String name;
    public String pictureId;

    public UseLog(Integer id, String date, String name, String pictureId){
        this.id = id;
        this.date = date;
        this.name= name;
        this.pictureId = pictureId;
    }
    public UseLog(String date, String name, String pictureId){
        this.date = date;
        this.name= name;
        this.pictureId = pictureId;
    }
}

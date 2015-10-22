package com.siot.sss.hsgallery.util.data.db;

import android.content.Context;

import com.siot.sss.hsgallery.app.model.ImageData;
import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.util.data.image.ImageShow;
import com.siot.sss.hsgallery.util.data.db.table.DBOpenHelper;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by SSS on 2015-08-09.
 */
public class UseLogManager {
    private static UseLogManager instance;

    private Context context;

    public static synchronized UseLogManager getInstance(){
        if(instance == null) return UseLogManager.instance  = new UseLogManager();
        return UseLogManager.instance;
    }
    public String currentTime(){
        Calendar c = Calendar.getInstance();
        Date date = new Date(c.get(Calendar.YEAR)-1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

        return android.text.format.DateFormat.format("yyyy/MM/dd hh:mm", date).toString();
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void addLog(UseLog.Type type){
//            DBOpenHelper helper = new DBOpenHelper(context);
//            helper.open();
//            helper.insertColumnUseLog(
//                new UseLog(
//                    this.currentTime(),
//                    ImageShow.getInstance().getImageData().displayName,
//                    ImageShow.getInstance().getImageData().id,
//                    UseLog.getTypeString(type),
//                    ImageShow.getInstance().getImageData().title,
//                    ImageShow.getInstance().getImageData().data,
//                    ImageShow.getInstance().getImageData().bucketId,
//                    ImageShow.getInstance().getImageData().bucketDisplayName,
//                    ImageShow.getInstance().getImageData().width,
//                    ImageShow.getInstance().getImageData().height
//                ));
//            helper.close();
    }

    public void addLog(ImageData imageData, UseLog.Type type){
        DBOpenHelper helper = new DBOpenHelper(context);
        helper.open();
        helper.insertColumnUseLog(
            new UseLog(
                this.currentTime(),
                imageData.displayName,
                imageData.id,
                UseLog.getTypeString(type),
                ImageShow.getInstance().getImageData().title,
                ImageShow.getInstance().getImageData().data,
                ImageShow.getInstance().getImageData().bucketId,
                ImageShow.getInstance().getImageData().bucketDisplayName,
                ImageShow.getInstance().getImageData().width,
                ImageShow.getInstance().getImageData().height
            ));
        helper.close();
    }
    public void addLogUpdate(String name, UseLog.Type type){
        DBOpenHelper helper = new DBOpenHelper(context);
        helper.open();
        helper.insertColumnUseLog(
            new UseLog(
                this.currentTime(),
                name,
                "rename to",
                UseLog.getTypeString(type),
                ImageShow.getInstance().getImageData().title,
                ImageShow.getInstance().getImageData().data,
                ImageShow.getInstance().getImageData().bucketId,
                ImageShow.getInstance().getImageData().bucketDisplayName,
                ImageShow.getInstance().getImageData().width,
                ImageShow.getInstance().getImageData().height
            ));
        helper.close();
    }


}
